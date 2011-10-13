package au.org.ala.delta.intkey;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import au.org.ala.delta.intkey.model.IntkeyDataset;
import au.org.ala.delta.model.Item;

public interface IntkeyUI {

    void handleNewDataset(IntkeyDataset dataset);

    void handleDatasetClosed();

    void handleUpdateAll();

    void handleIdentificationRestarted();

    void displayRTFReport(String rtfSource, String title);

    void displayErrorMessage(String message);

    void displayInformationMessage(String message);

    void displayBusyMessage(String message);

    void removeBusyMessage();

    /**
     * Display information for the supplied taxa
     * 
     * @param taxa
     *            the list of taxa
     * @param imagesAutoDisplayText
     *            for the first taxon whose information is shown, display all
     *            images whose subjects contain this text. If the text is an
     *            empty string, display all available images for the taxon. If
     *            the text is null, do not display any images.
     * @param otherItemsAutoDisplayText
     *            for the first taxon whose information is shown, display all
     *            non-image information items (with the exception of linked
     *            files that are not RTF files) whose subjects contain the
     *            supplied text. If the text is an empty string, display all
     *            available non-image information items (aside from linked files
     *            that are not RTF files). If the text is null, do not display
     *            any non-image information items
     * @param closePromptAfterAutoDisplay
     *            if true, and imagesAutoDisplay text and or
     *            otherItemsAutoDisplayText are not null, close the window after
     *            displaying images and non-image information items
     */
    void displayTaxonInformation(List<Item> taxa, String imagesAutoDisplayText, String otherItemsAutoDisplayText, boolean closePromptAfterAutoDisplay);

    void addToolbarButton(boolean advancedModeOnly, boolean normalModeOnly, boolean inactiveUnlessUsedCharacters, String imageFileName, List<String> commands, String shortHelp, String fullHelp);

    void addToolbarSpace();

    void clearToolbar();

    void IllustrateCharacters(List<au.org.ala.delta.model.Character> characters);

    void IllustrateTaxa(List<Item> taxa);

    void displayContents(LinkedHashMap<String, String> contentsMap);

    void displayFile(File file, String description);

    boolean isLogVisible();

    void setLogVisible(boolean visible);

    void updateLog();

    void quitApplication();

    List<Item> getSelectedTaxa();
}
