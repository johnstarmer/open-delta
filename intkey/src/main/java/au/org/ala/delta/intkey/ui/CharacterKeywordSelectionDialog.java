package au.org.ala.delta.intkey.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;

import org.jdesktop.application.Application;
import org.jdesktop.application.Resource;
import org.jdesktop.application.ResourceMap;

import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.model.Character;

public class CharacterKeywordSelectionDialog extends KeywordSelectionDialog {

    private List<Character> _selectedCharacters;

    
    @Resource
    String title;

    public CharacterKeywordSelectionDialog(Dialog owner, IntkeyContext context, String directiveName) {
        super(owner, context, directiveName);
        init(context);
    }

    public CharacterKeywordSelectionDialog(Frame owner, IntkeyContext context, String directiveName) {
        super(owner, context, directiveName);
        _directiveName = directiveName;
        init(context);
    }

    private void init(IntkeyContext context) {
        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(CharacterKeywordSelectionDialog.class);
        resourceMap.injectFields(this);

        setTitle(String.format(title, _directiveName));
        _selectedCharacters = new ArrayList<Character>();
        
        List<String> characterKeywords = context.getCharacterKeywords();

        DefaultListModel model = new DefaultListModel();
        for (String keyword : characterKeywords) {
            model.addElement(keyword);
        }
        _list.setModel(model);
    }

    @Override
    protected void okBtnPressed() {
        for (Object o : _list.getSelectedValues()) {
            String keyword = (String) o;
            _selectedCharacters.addAll(_context.getCharactersForKeyword(keyword));
        }
        Collections.sort(_selectedCharacters);
        this.setVisible(false);
    }

    @Override
    protected void cancelBtnPressed() {
        this.setVisible(false);
    }

    @Override
    protected void listBtnPressed() {
        if (_list.getSelectedValue() != null) {
            
            List<Character> characters = new ArrayList<Character>();
            String selectedKeyword = (String) _list.getSelectedValue();
            characters.addAll(_context.getCharactersForKeyword(selectedKeyword));

            CharacterSelectionDialog charDlg = new CharacterSelectionDialog(this, characters, _directiveName, selectedKeyword);
            charDlg.setVisible(true);

            List<Character> charsSelectedInDlg = charDlg.getSelectedCharacters();
            if (charsSelectedInDlg.size() > 0) {
                _selectedCharacters.clear();
                _selectedCharacters.addAll(charsSelectedInDlg);
                this.setVisible(false);
            }
        }
    }

    @Override
    protected void imagesBtnPressed() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void searchBtnPressed() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void helpBtnPressed() {
        // TODO Auto-generated method stub

    }

    public List<Character> getSelectedCharacters() {
        return _selectedCharacters;
    }

}