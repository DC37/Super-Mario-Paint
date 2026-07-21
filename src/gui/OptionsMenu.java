package gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backend.editing.CommandInterface;
import backend.editing.commands.MultiplyTempoCommand;
import backend.songs.TimeSignature;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class OptionsMenu {
    
    private static final Logger log = LoggerFactory.getLogger(OptionsMenu.class);

    /** This is the text that labels the slider. */
    private String txt = "Default Note Volume";

    /** This is the place where we type in the amount to adjust tempo by. */
    private TextField tempoField;

    /** A slider that changes the default volume of placed notes. */
    private Slider defaultVolume;

    /** A drop down menu to choose the soundfont in the AppData folder. */
    private ComboBox<String> soundfontsMenu;
    
    /** A checkbox to set the sequence's soundset to the one selected. */
    private CheckBox bindBox;
    
    /** Where the new time signature is entered. */
    private TextField timesigField;
    
    /** Make the bars visible */
    private CheckBox barsVisibleBox;
    
    /** Make the bars visible */
    private CheckBox numsVisibleBox;
    
    SMPFXController controller;
    Staff staff;
    
    public OptionsMenu(SMPFXController controller, Staff staff) {
        this.controller = controller;
        this.staff = staff;
    }
    
    /** Opens up an options dialog. */
    public void options(Window owner) {
        final Stage dialog = new Stage();
        initializeDialog(dialog, owner);
        Label label = new Label(txt);
        defaultVolume = makeVolumeSlider();
        Button okButton = new Button("OK");

        okButton.setOnAction((ActionEvent event) -> dialog.close());

        FlowPane pane = new FlowPane(10, 10);
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(okButton);
        
        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setAlignment(Pos.CENTER);
        
        Label tempoAdjustHack = new Label("Multiply current tempo by:");
        tempoField = new TextField();
        
        Label timesigLabel = new Label("Enter new time signature:");
        timesigField = new TextField();
        timesigField.setPromptText("4/4, 3/4, 6/8, 6+3, ...");

        Label sfLabel = new Label("Current soundfont");
        soundfontsMenu = makeSoundfontsComboBox(dialog);
        bindBox = makeBindCheckBox();
        VBox soundfontsOptions = new VBox(1);
        soundfontsOptions.setAlignment(Pos.CENTER);
        
        barsVisibleBox = makeBarsVisibleCheckbox();
        numsVisibleBox = makeNumsVisibleCheckbox();

        // Hide some options while in arranger mode, due to them being impractical (Tempo multiplier, Soundfont binder) - seymour
        switch (StateMachine.getMode()) {
        case SONG:
            soundfontsOptions.getChildren().addAll(sfLabel, soundfontsMenu, bindBox);
            vBox.getChildren().addAll(label, defaultVolume, timesigLabel, timesigField,
                    tempoAdjustHack, tempoField, soundfontsOptions, barsVisibleBox, numsVisibleBox, pane);
            break;
            
        case ARRANGEMENT:
            soundfontsOptions.getChildren().addAll(sfLabel, soundfontsMenu);
            vBox.getChildren().addAll(label, defaultVolume, soundfontsOptions, barsVisibleBox, numsVisibleBox, pane);
            break;
        }
        
        defaultVolume.autosize();
        Scene scene1 = new Scene(vBox);
        dialog.setScene(scene1);
        dialog.showAndWait();
        updateValues();
        staff.redraw();
    }

    @SuppressWarnings("java:S4968")
    private void onCustomSoundfontOptionSelected(
    		ComboBox<String> availableSoundfonts, Window owner,
    		ObservableValue<? extends String> obsv, String oldVal, String newValue) {
    	
    	if (!newValue.equals("Add Soundfont..."))
    		return;
    	
        FileChooser f = new FileChooser();
        f.setInitialDirectory(new File(System.getProperty("user.dir")));
        f.setTitle("Add Soundfont...");
        f.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("soundfonts (*.sf2)", "*.sf2"));
        final File sf = f.showOpenDialog(null);
        
        // Any programmed selection triggers the ChangedListener again
        // Wrap in a runLater to avoid a huge stacktrace
        Platform.runLater(() -> {
            if (sf == null) {
                availableSoundfonts.getSelectionModel().selectPrevious();
                return;
            }
            
            if(!addSoundfont(sf, owner)) {
                availableSoundfonts.getSelectionModel().selectPrevious();
                return;
            }
            // Add soundfont name, or just pick it
            ObservableList<String> soundfonts = availableSoundfonts.getItems();
            int i;
            String sfName = sf.getName();
            for (i = 0; i < soundfonts.size() - 1; i++) {
                int compare = soundfonts.get(i).compareTo(sfName);
                if (compare <= 0)
                    break;
                
                availableSoundfonts.getItems().add(i, sfName);
            }
            availableSoundfonts.getSelectionModel().select(i);
        });
        
        bindBox.setSelected(newValue.equals(staff.getSequence().getSoundset()));
        /* we don't want to change the state of bindBox's userData so set it back to
           theStaff.getSequence().getSoundset() */
        bindBox.setUserData(staff.getSequence().getSoundset());
    }
    
    /**
     * @return A ComboBox listing all soundfonts in the AppData folder and an
     *         additional item to "Add Soundfont"
     */
    private ComboBox<String> makeSoundfontsComboBox(Window owner) {
        
        // Populate combobox list with soundfonts in AppData
        final ComboBox<String> availableSoundfonts = new ComboBox<>();
        String[] listOfFiles;
        try {
            listOfFiles = getSoundfontsList();
        } catch (IOException e) {
            log.error("Error while trying to get soundfonts list!", e);
            listOfFiles = new String[0];
        }
        for (String filename : listOfFiles) {
            availableSoundfonts.getItems().add(filename);

            if (filename.equals(StateMachine.getCurrentSoundset()))
                availableSoundfonts.getSelectionModel().selectLast();
        }

        availableSoundfonts.setPrefWidth(150);
        availableSoundfonts.getItems().add("Add Soundfont...");
        availableSoundfonts.valueProperty().addListener(
        		(obsv, oldVal, newValue) -> onCustomSoundfontOptionSelected(availableSoundfonts, owner, obsv, oldVal, newValue));
        
        return availableSoundfonts;
    }

    private CheckBox makeBindCheckBox() {
        final CheckBox checkBoxBind = new CheckBox();
        checkBoxBind.setText("Bind to song");
        checkBoxBind.setStyle("-fx-font-size: 9;");
        checkBoxBind.setUserData(staff.getSequence().getSoundset());
        if (soundfontsMenu.getSelectionModel().getSelectedItem().equals(staff.getSequence().getSoundset()))
            checkBoxBind.setSelected(true);
        checkBoxBind.selectedProperty().addListener((ov, oldVal, checked) -> {
            if(checked != null && checked.booleanValue()) {
                checkBoxBind.setUserData(soundfontsMenu.getSelectionModel().getSelectedItem());
            } else {
                checkBoxBind.setUserData("");
            }
        });
        
        return checkBoxBind;
    }
    
    /**
     * Sets the height, width, and other basic properties of this dialog box.
     *
     * @param dialog
     *            The dialog box that we are setting up.
     */
    private void initializeDialog(Stage dialog, Window owner) {
        dialog.setResizable(false);
        dialog.setTitle("Options");
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        // setting this style seems to be blocking everything
        // commenting out until someone finds why --rozlyn
//        dialog.initStyle(StageStyle.UTILITY);
    }

    /**
     * @return A slider that edits the default volume of the notes that one
     *         places.
     */
    private Slider makeVolumeSlider() {
        Slider dV = new Slider();
        dV.setMax((double) Values.MAX_VELOCITY + 1);
        dV.setMin(0);
        dV.setValue(Values.DEFAULT_VELOCITY);
        dV.setShowTickMarks(true);
        dV.setShowTickLabels(true);
        dV.setSnapToTicks(true);
        dV.setMajorTickUnit(16);
        return dV;
    }

    /** Updates the different values of this program. */
    private void updateValues() {
        changeDefaultVol();
        changeTimeSignature();
        multiplyTempo();
        changeSoundfont();
        updateBarVisibility();
        updateNumVisibility();
    }

    /** Updates the default volume of the program notes. */
    private void changeDefaultVol() {
        int vol = (int) defaultVolume.getValue();
        Values.DEFAULT_VELOCITY = vol >= 128 ? 127 : vol;
    }

    /** Updates the tempo from the options dialog. */
    private void multiplyTempo() {
        String gotTxt = tempoField.getText();
        if (gotTxt == null)
            return;
    
        int num = 1;
        try {
            num = Integer.parseInt(gotTxt);
        } catch (NumberFormatException e) {
            return;
        }
        if (num <= 1)
            return;
        
        double currTempo = StateMachine.getTempo();
        double newTempo = currTempo * num;
        
        TimeSignature currTimesig = StateMachine.getTimeSignature();
        TimeSignature newTimesig = TimeSignature.multiply(currTimesig, num);
        
        CommandInterface cmd = new MultiplyTempoCommand(staff, num, currTempo, newTempo, currTimesig, newTimesig);
        cmd.redo();
        
        controller.getModifySongManager().execute(cmd);
        controller.getModifySongManager().record();
    }
    
    /** Updates the program's soundfont, bind to song if checked. */
    private void changeSoundfont() {
        String selectedSoundfont = soundfontsMenu.getSelectionModel().getSelectedItem();
        try {
            staff.getSoundPlayer().loadFromAppData(selectedSoundfont);
        } catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
            log.error("Error while changing soundfont!", e);
        }
        
        String newSoundset = (String) bindBox.getUserData();
        if(!staff.getSequence().getSoundset().equals(newSoundset)) {
            staff.getSequence().setSoundset(newSoundset);
            StateMachine.setSongModified(true);
        }
    }
    
    private void changeTimeSignature() {
        String gotTxt = timesigField.getText();
        
        if (gotTxt == null)
            return;
        
        TimeSignature t;
        try {
            t = TimeSignature.valueOf(gotTxt);
        } catch (IllegalArgumentException e) {
            return;
        }
        
        staff.setTimeSignature(t);
    }
    
    /**
     * Creates the soundfont folder if it does not already exists.
     */
    private static File getSoundfontFolder() throws IOException {
        File dir = new File(Values.SOUNDFONTS_FOLDER);
        Files.createDirectories(dir.toPath());
        return dir;
    }
    
    /**
     * @return The list of filenames *.sf2 in the soundfonts folder.
     * @since v1.1.2
     */
    private static String[] getSoundfontsList() throws IOException {
        File soundfontsFolder = getSoundfontFolder();
        
        return soundfontsFolder.list(
                (File dir, String name) -> name.toLowerCase().endsWith(".sf2"));
    }
    
    /**
     * Copies the soundfont file to AppData.
     * 
     * @param sf
     *            The soundfont file.
     * @return if soundfont exists in AppData now
     * @since v1.1.2
     */
    private static boolean addSoundfont(File sf, Window owner) {
        String sfName = sf.getName();
        if(sfName.isEmpty())
            return false;
        File destSf = new File(Values.SOUNDFONTS_FOLDER + sfName);
        
        if (destSf.exists()) {
            String mssg = "A soundfont named '" + sfName + "' was already added.\nReplace it?";
            if (!Dialog.showYesNoDialog("Options", mssg, owner))
                return false;
        }
        
        try {
            Files.copy(sf.toPath(), destSf.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Error while trying to add soundfont!", e);
            return false;
        }
        return true;
    }

    private CheckBox makeBarsVisibleCheckbox() {
        CheckBox box = new CheckBox("Bars visible");
        box.setSelected(Settings.barsVisible);
        return box;
    }

    private void updateBarVisibility() {
        Settings.barsVisible = barsVisibleBox.isSelected();
    }

    private CheckBox makeNumsVisibleCheckbox() {
        CheckBox box = new CheckBox("Measure numbers visible");
        box.setSelected(Settings.numsVisible);
        return box;
    }

    private void updateNumVisibility() {
        Settings.numsVisible = numsVisibleBox.isSelected();
    }

}
