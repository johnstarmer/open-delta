package au.org.ala.delta.editor.ui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;

import au.org.ala.delta.editor.model.CharacterPredicate;
import au.org.ala.delta.editor.ui.dnd.SimpleTransferHandler;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.SearchDirection;
import au.org.ala.delta.ui.SearchController;
import au.org.ala.delta.ui.SearchDialog;
import au.org.ala.delta.ui.SearchOptions;

/**
 * The CharacterTree extends JTree to provide 1-click cell editing and to implement the ReorderableList interface to help our Drag and Drop implementation.
 * 
 * A CharacterTree can behave as both a list of Characters and a list of States depending on the selection and what is being dragged and dropped.
 */
public class CharacterTree extends JTree implements ReorderableList {

	private static final long serialVersionUID = 1462521823171738637L;
	private boolean _doubleProcessingMouseEvent = false;

	/** Handles drag and drop of states */
	private TransferHandler _stateTransferHandler;

	/** Handles drag and drop of characters */
	private TransferHandler _characterTransferHandler;

	private ReorderableList _characterListBehaviour;

	private ReorderableList _stateListBehaviour;

	private ReorderableList _reorderableListBehaviour;

	private class StateListBehaviour implements ReorderableList {

		private int _selectedCharacter;

		@Override
		public int getSelectedIndex() {
			TreePath selectedPath = getSelectionPath();
			// We record the selected character to help us when dragging
			// states.
			_selectedCharacter = pathToCharacterNumber(selectedPath);

			if (selectedPath.getPathCount() >= 3) {
				int stateNum = ((MultistateStateNode) selectedPath.getPathComponent(2)).getStateNo();
				return stateNum - 1;
			}

			return -1;
		}

		@Override
		public void setSelectedIndex(int index) {

		}

		@Override
		public int getDropLocationIndex(javax.swing.TransferHandler.DropLocation dropLocation) {
			if (dropLocation == null) {
				return -1;
			}

			return dropLocationToStateNumber((DropLocation) dropLocation) - 1;
		}

		private int dropLocationToStateNumber(DropLocation location) {
			TreePath path = location.getPath();
			if (path.getPathCount() == 2 && pathToCharacterNumber(path) == _selectedCharacter) {

				if (location.getChildIndex() >= 0) {
					return location.getChildIndex() + 1;
				}
			} else if (path.getPathCount() == 1 && location.getChildIndex() == _selectedCharacter) {
				// get character number. move to last state position.
				TreeNode node = (TreeNode) getModel().getChild(path.getLastPathComponent(), _selectedCharacter - 1);
				return node.getChildCount() + 1;
			}

			return -1;
		}

		@Override
		public void setSelectionAction(Action action) {
		}
	}

	private class CharacterListBehaviour implements ReorderableList {
		@Override
		public int getSelectedIndex() {
			TreePath selectedPath = getSelectionPath();
			return pathToCharacterNumber(selectedPath) - 1;
		}

		@Override
		public void setSelectedIndex(int index) {
			Object[] nodes = new Object[2];
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) getModel().getRoot();
			nodes[0] = root;
			nodes[1] = root.getChildAt(index);
			TreePath path = new TreePath(nodes);

			setSelectionPath(path);
		}

		@Override
		public int getDropLocationIndex(javax.swing.TransferHandler.DropLocation dropLocation) {
			if (dropLocation == null) {
				return -1;
			}
			return dropLocationToCharacterNumber((DropLocation) dropLocation) - 1;
		}

		/**
		 * Turns a DropLocation into the character number of the character at the location.
		 * 
		 * @param location
		 *            the current drop location.
		 * @return the character number representing the drop position.
		 */
		private int dropLocationToCharacterNumber(DropLocation location) {
			TreePath path = location.getPath();

			if (path.getPathCount() == 2) {
				return pathToCharacterNumber(path);
			} else {
				return location.getChildIndex() + 1;
			}
		}

		@Override
		public void setSelectionAction(Action action) {
		}
	}

	public CharacterTree() {

		setToggleClickCount(0);
		_characterListBehaviour = new CharacterListBehaviour();
		_stateListBehaviour = new StateListBehaviour();

		_reorderableListBehaviour = _characterListBehaviour;

		addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				if (_doubleProcessingMouseEvent) {
					return;
				}
				if (!isEditing()) {
					int selectedRow = getClosestRowForLocation(e.getX(), e.getY());

					if ((selectedRow >= 0) && (e.getClickCount() == 2) && SwingUtilities.isLeftMouseButton(e)) {
						Action action = getActionMap().get("SelectionAction");
						if (action != null) {
							action.actionPerformed(new ActionEvent(this, -1, ""));
						}
					}
				}
			}
		});

		addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {

				TreePath selection = e.getNewLeadSelectionPath();
				if (selection != null) {
					Object lastComponent = selection.getLastPathComponent();
					if (lastComponent instanceof DefaultMutableTreeNode) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastComponent;

						if (node.isLeaf()) {
							_reorderableListBehaviour = _stateListBehaviour;
							CharacterTree.super.setTransferHandler(_stateTransferHandler);
						} else {
							_reorderableListBehaviour = _characterListBehaviour;
							CharacterTree.super.setTransferHandler(_characterTransferHandler);
						}
					}
				}
			}
		});

		ActionMap actionMap = Application.getInstance().getContext().getActionMap(this);

		javax.swing.Action find = actionMap.get("find");
		if (find != null) {
			getActionMap().put("find", find);
		}

		javax.swing.Action findNext = actionMap.get("findNext");
		if (findNext != null) {
			getActionMap().put("findNext", findNext);
		}

	}

	private SearchDialog _search;

	@org.jdesktop.application.Action
	public void find() {
		if (_search == null) {
			_search = new SearchDialog(new CharacterSearchController());
		}
		_search.setVisible(true);
	}
	
	@org.jdesktop.application.Action
	public void findNext() {
		if (_search == null) {
			find();
			return;
		}
		
		_search.findNext();
	}

	/**
	 * This is a done to initiate a cell edit from a single click with drag and drop enabled.
	 */
	protected void processMouseEvent(MouseEvent e) {

		super.processMouseEvent(e);
		_doubleProcessingMouseEvent = false;
		TreePath selectedPath = getSelectionPath();
		if (selectedPath != null) {
			Object lastComponent = selectedPath.getLastPathComponent();
			if (lastComponent instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastComponent;

				if (node.isLeaf()) {
					int row = getRowForPath(selectedPath);
					Rectangle bounds = getRowBounds(row);
					int pos = e.getX() - bounds.x;
					if (pos >= 0 && pos < 20) {
						_doubleProcessingMouseEvent = true;
						super.processMouseEvent(e);
					}
				}
			}
		}
	}

	@Override
	public int getSelectedIndex() {
		return _reorderableListBehaviour.getSelectedIndex();
	}

	@Override
	public void setSelectedIndex(int index) {
		_reorderableListBehaviour.setSelectedIndex(index);
	}

	@Override
	public int getDropLocationIndex(javax.swing.TransferHandler.DropLocation dropLocation) {
		return _reorderableListBehaviour.getDropLocationIndex(dropLocation);
	}

	@Override
	public void setSelectionAction(Action action) {
		getActionMap().put("SelectionAction", action);
	}

	/**
	 * Returns the character number of the Character at the supplied TreePath.
	 * 
	 * @param location
	 *            the current drop location.
	 * @return the character number representing the drop position.
	 */
	private int pathToCharacterNumber(TreePath path) {
		if (path != null) {
			Object[] nodes = path.getPath();
			if (nodes[1] instanceof CharacterTreeNode) {
				return ((CharacterTreeNode) nodes[1]).getCharacter().getCharacterId();
			}
		}
		return -1;
	}

	public void setCharacterTransferHandler(TransferHandler handler) {
		_characterTransferHandler = handler;
	}

	public void setStateTransferHandler(TransferHandler handler) {
		_stateTransferHandler = handler;
	}

	@Override
	public void setTransferHandler(TransferHandler handler) {
		if (handler instanceof SimpleTransferHandler<?>) {
			Class<?> transferClass = ((SimpleTransferHandler<?>) handler).getTransferClass();
			if (au.org.ala.delta.model.Character.class.equals(transferClass)) {
				setCharacterTransferHandler(handler);
			} else if (Integer.class.equals(transferClass)) {
				setStateTransferHandler(handler);
			} else {
				super.setTransferHandler(handler);
			}
		}
	}

	class CharacterSearchController implements SearchController {
		
		private SearchCharacterPredicate _predicate;
		private Character _lastResult;
		private ResourceMap _messages;
		
		
		public CharacterSearchController() {
			SingleFrameApplication application = (SingleFrameApplication)Application.getInstance();
			_messages = application.getContext().getResourceMap();			
		}
		
		private Character findImpl(SearchCharacterPredicate predicate, int startFrom) {
			CharacterTreeModel model = (CharacterTreeModel) getModel();
			Character result = model.firstCharacter(predicate, startFrom, predicate.getOptions().getSearchDirection());
			if (result == null && predicate.getOptions().isWrappedSearch()) {
				int restartFrom = 1;
				if (predicate.getOptions().getSearchDirection() == SearchDirection.Backward) {
					restartFrom = model.getNumberOfCharacters();
				}
				result = model.firstCharacter(predicate, restartFrom, predicate.getOptions().getSearchDirection());
			}

			if (result != null) {
				selectCharacter(result);
			} else {
				clearSelection();
			}
			
			return result;			
		}
		
		private void selectCharacter(Character ch) {
			int index = ch.getCharacterId() - 1;
			setSelectedIndex(index);
			TreePath path = getPathForRow(index);
			makeVisible(path);
			scrollPathToVisible(path);			
		}
		
		@Override
		public String getTitle() {
			return _messages.getString("findCharacter.title");
		}
		
		@Override
		public JComponent getOwningComponent() {
			return CharacterTree.this;
		}

		@Override
		public boolean findNext(SearchOptions options) {
			
			
			int delta = options.getSearchDirection() == SearchDirection.Forward ? 1 : -1;
			
			int startFrom = getSelectedIndex() + delta;
			if (_lastResult != null) {
				startFrom = _lastResult.getCharacterId() + delta;
			}
			
			CharacterTreeModel model = (CharacterTreeModel) getModel();
			if (startFrom < 1) {
				startFrom = options.isWrappedSearch() ? model.getNumberOfCharacters() : 1;
			} else if (startFrom > model.getNumberOfCharacters()) {
				startFrom = options.isWrappedSearch() ? 1 : model.getNumberOfCharacters();
			}
			
			_predicate = new SearchCharacterPredicate(options);
			
			Character result = findImpl(_predicate, startFrom);
			if (result == null && _lastResult != null && !options.isWrappedSearch()) {
				selectCharacter(_lastResult);
			} else {
				_lastResult = result;
			}
			
			return _lastResult != null;
		}

	}

	class SearchCharacterPredicate implements CharacterPredicate {

		private String _term;
		private SearchOptions _options;

		public SearchCharacterPredicate(SearchOptions options) {
			_options = options;
			_term = options.getSearchTerm();
			if (!options.isCaseSensitive()) {
				_term = _term.toLowerCase();
			} 
		}

		@Override
		public boolean test(Character character) {
			String desc = character.getDescription();
			if (!_options.isCaseSensitive()) {
				return desc.toLowerCase().contains(_term);
			}

			return desc.contains(_term);
		}
		
		public SearchOptions getOptions() {
			return _options;
		}
		
		public String getTerm() {
			return _term;
		}
		
	}

}
