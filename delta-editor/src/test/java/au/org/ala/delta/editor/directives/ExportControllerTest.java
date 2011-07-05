package au.org.ala.delta.editor.directives;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import au.org.ala.delta.DeltaTestCase;
import au.org.ala.delta.editor.DeltaEditor;
import au.org.ala.delta.editor.model.EditorDataModel;
import au.org.ala.delta.editor.slotfile.DeltaVOP;
import au.org.ala.delta.editor.slotfile.model.DirectiveFile;
import au.org.ala.delta.editor.slotfile.model.DirectiveFile.DirectiveType;
import au.org.ala.delta.editor.slotfile.model.SlotFileDataSet;
import au.org.ala.delta.editor.slotfile.model.SlotFileDataSetFactory;

/**
 * Tests the ExportController class.  The SuppressWarnings annotation is to prevent warnings
 * about accessing the AppContext which is required to do the thread synchronization 
 * necessary to make the tests run in a repeatable manner.
 */
public class ExportControllerTest extends DeltaTestCase {

	/**
	 * Allows us to manually set the data set to be returned from the
	 * getCurrentDataSet method.
	 */
	private class DeltaEditorTestHelper extends DeltaEditor {
		private EditorDataModel _model;
		public void setModel(EditorDataModel model) {
			_model = model;
		}
		
		@Override
		public EditorDataModel getCurrentDataSet() {
			return _model;
		}
	}
	
	
	/** The instance of the class we are testing */
	private ExportController exporter;
	
	/** The data set we are exporting */
	private SlotFileDataSet _dataSet;
	
	@Before
	public void setUp() throws Exception {
		// Sure hope this won't throw a headless exception at some point...
		DeltaEditorTestHelper helper = new DeltaEditorTestHelper();
	
		File f = copyURLToFile("/SAMPLE.DLT");
			
		DeltaVOP vop = new DeltaVOP(f.getAbsolutePath(), false);
		
		SlotFileDataSetFactory factory = new SlotFileDataSetFactory(vop);
		
		_dataSet = (SlotFileDataSet)factory.createDataSet("test");
		EditorDataModel model = new EditorDataModel(_dataSet);
		helper.setModel(model);
		
		exporter = new ExportController(helper);
	}
	
	@Test
	public void testSilentExport() throws Exception {
		
		for (int i=1; i<=_dataSet.getDirectiveFileCount(); i++) {
			DirectiveFile directiveFile = _dataSet.getDirectiveFile(i);
			DirectiveFileInfo test = new DirectiveFileInfo(directiveFile.getFileName(), DirectiveType.CONFOR, directiveFile);
			
			List<DirectiveFileInfo> files = Arrays.asList(new DirectiveFileInfo[] {test});
			File tempDir = new File("/tmp");
			System.out.println(i+" : "+directiveFile.getShortFileName());
			exporter.new DoExportTask(tempDir, files).doInBackground();
		}
	}
	
	/**
	 * This tests the toint directives file which has:
	 * SHOW (DIRARG_TEXT)
	 * LISTING FILE (DIRARG FILE)
	 * HEADING (DIRARG_TEXT)
	 * REGISTRATION SUBHEADING (DIRARG_TEXT)
	 * TRANSLATE INTO (DIRARG_TRANSLATION)
	 * CHARACTERS FOR SYNONYMY (DIRARG_CHARLIST)
	 * OMIT PERIOD FOR CHARACTERS (DIRARG_CHARLIST)
	 * OMIT OR FOR CHARACTERS (DIRARG_CHARLIST)
	 * OMIT INNER COMMENTS (DIRARG_NONE)
	 * EXCLUDE CHARACTERS (DIRARG_CHARLIST)
	 * CHARACTER RELIABILIITES (DIRARG_CHARREALLIST)
	 * NEW PARAGRAPHS AT CHARACTERS (DIRARG_CHARLIST)
	 * ITEM SUBHEADINGS (DIRARG_CHARTEXTLIST)
	 * INPUT FILE (DIRARG_FILE)
	 * INTKEY OUTPUT FILE (DIRARG_FILE)
	 * 
	 * It also includes a commented out directive.
	 * 
	 */
	@Test
	public void testExportToInt() throws Exception {
		// toint is directive file 13 in the sample dataset.
		exportAndCheck(13, "toint");
	}
	
	/**
	 * This tests the toint directives file which has:
	 * COMMENT (DIRARG_COMMENT)
	 * PRINT WIDTH (DIRARG_INTEGER)
	 * TYPESETTING MARKS (DIRARG_TEXTLIST)
	 */
	@Test
	public void testExportMarkRtf() throws Exception {
		exportAndCheck(6, "markrtf");
	}
	
	/**
	 * This tests the empchari directives file which has:
	 * COMMENT (DIRARG_COMMENT)
	 * EMPHASIZE CHARACTERS (DIRARG_ITEMCHARLIST)
	 */
	@Test
	public void testExportEmpChari() throws Exception {
		exportAndCheck(28, "empchari");
	}
	

	/**
	 * This tests the dist directives file which has:
	 * LISTING FILE (DIRARG FILE)
	 * ITEMS FILE (DIRARG FILE)
	 * OUTPUT FILE (DIRAG FILE)
	 * MINIMUM NUMBER OF COMPARISONS 7
	 */
	@Test
	public void testExportDist() throws Exception {
		exportAndCheck(19, "dist");
	}
	
	/**
	 * This tests the key4 directives file which has:
	 * COMMENT (DIRARG_COMMENT)
	 * HEADING (DIRARG_TEXT)
	 * PRESET CHARACTERS (DIRARG_PRESET)
	 * NO BRACKETTED KEY (DIRARG_NONE)
	 * PRINT WIDTH (DIRARG_INTEGER)
	 */
	@Test
	public void testExportKey4() throws Exception {
		exportAndCheck(36, "key4");
	}
	
	/**
	 * This tests the key2 directives file which has:
	 * COMMENT (DIRARG_COMMENT)
	 * HEADING (DIRARG_TEXT)
	 * RBASE (DIRARG_REAL)
	 * NO BRACKETTED KEY (DIRARG_NONE)
	 * PRINT WIDTH (DIRARG_INTEGER)
	 */
	@Test
	public void testExportKey2() throws Exception {
		exportAndCheck(34, "key2");
	}
	
	/**
	 * This tests the key6 directives file which has:
	 * COMMENT (DIRARG_COMMENT)
	 * HEADING (DIRARG_TEXT)
	 * RBASE (DIRARG_REAL)
	 * VARWT (DIRARG_REAL)
	 * NUMBER OF CONFIRMATORY CHARACTERS (DIRARG_INTEGER)
	 * TREAT CHARACTERS AS VARIABLE (DIRARG_ITEM_CHARLIST)
	 * 
	 * It also tests the behaviour of the itemcharlist export with a 
	 * directive file type of Key (item descriptions are output for CONFOR
	 * types, but the item number is used for KEY).
	 */
	@Test
	public void testExportKey6() throws Exception {
		exportAndCheck(37, "key6");
	}
	
	private void exportAndCheck(int directiveFileNum, String directiveFileName) throws Exception {
		File directory = FileUtils.getTempDirectory();
		
		export(directory, directiveFileNum);
		
		String[] directives = read(directiveFileName);
		
		String actual = FileUtils.readFileToString(new File(directory, directiveFileName));
		actual = actual.replace("\r\n", "\n");
		String[] actualDirectives = actual.split("\\*");
		
		int i=0;
		for (String directive : directives) {
			assertEquals(directive, actualDirectives[i++]);
		}
	}
	
	/**
	 * Exports the directive file identified by directiveFileNum into the
	 * supplied directory.
	 * @param directory the directory to export the directives to.
	 * @param directiveFileNum the directives file to export.
	 * @throws Exception if there is an error.
	 */
	private void export(File directory, int directiveFileNum) throws Exception {
		DirectiveFile directiveFile = _dataSet.getDirectiveFile(directiveFileNum);
		DirectiveFileInfo test = new DirectiveFileInfo(directiveFile.getFileName(), DirectiveType.CONFOR, directiveFile);
		
		List<DirectiveFileInfo> files = Arrays.asList(new DirectiveFileInfo[] {test});
		
		exporter.new DoExportTask(directory, files).doInBackground();
	}
	
	private String[] read(String fileName) throws Exception {
		
		URL expected = getClass().getResource("expected_results/"+fileName);
	
		File f = new File(expected.toURI());
		String buffer = FileUtils.readFileToString(f);
		buffer = buffer.replace("\r\n", "\n");
		return buffer.toString().split("\\*");
	}
	
}
