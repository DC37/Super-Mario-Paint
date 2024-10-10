package smp.components.buttons;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import smp.ImageLoader;
import smp.SoundfontLoader;
import smp.commandmanager.commands.MultiplyTempoCommand;
import smp.components.Values;
import smp.components.general.ImagePushButton;
import smp.components.staff.sequences.StaffSequence;
import smp.fx.SMPFXController;
import smp.stateMachine.ProgramState;
import smp.stateMachine.StateMachine;

/**
 * This is the options button. It currently doesn't do anything.
 *
 * @author RehdBlob
 * @author seymour
 * @since 2013.12.25
 */
public class OptionsButton extends ImagePushButton {

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

    /**
     * Default constructor.
     *
     * @param i
     *            The image that we want to link this to.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public OptionsButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
        
        ct.getBasePane().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (controller.getNameTextField().focusedProperty().get()) 
                    return; // Disable while textfield is focused
                
                if(event.isControlDown() && event.getCode() == KeyCode.COMMA)
                    reactPressed(null);
            }
        });
        
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState();
        if (curr == ProgramState.EDITING || curr == ProgramState.ARR_EDITING)
        	options(((Node) event.getSource()).getScene().getWindow());
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        // do nothing.
    }

    /** Opens up an options dialog. */
    private void options(Window owner) {
        final Stage dialog = new Stage();
        initializeDialog(dialog, owner);
        Label label = new Label(txt);
        defaultVolume = makeVolumeSlider();
        Button okButton = new Button("OK");

        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }

        });

        FlowPane pane = new FlowPane(10, 10);
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(okButton);
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        Label tempoAdjustHack = new Label("Increase tempo by how many times?");
        tempoField = new TextField();

        Label sfLabel = new Label("Current soundfont");
		soundfontsMenu = makeSoundfontsComboBox(dialog);
		bindBox = makeBindCheckBox();
        VBox soundfontsOptions = new VBox(1);
        soundfontsOptions.setAlignment(Pos.CENTER);

        // Hide some options while in arranger mode, due to them being impractical (Tempo multiplier, Soundfont binder) - seymour
		if (StateMachine.getState() == ProgramState.ARR_EDITING) {
			soundfontsOptions.getChildren().addAll(sfLabel, soundfontsMenu);
	        vBox.getChildren().addAll(label, defaultVolume, soundfontsOptions, pane);
		} else {
			soundfontsOptions.getChildren().addAll(sfLabel, soundfontsMenu, bindBox);
	        vBox.getChildren().addAll(label, defaultVolume, tempoAdjustHack,
	                tempoField, soundfontsOptions, pane);
		}
		
        defaultVolume.autosize();
        Scene scene1 = new Scene(vBox);
        dialog.setScene(scene1);
        dialog.showAndWait();
        updateValues();
        theStaff.redraw();
    }

	/**
	 * @return A ComboBox listing all soundfonts in the AppData folder and an
	 *         additional item to "Add Soundfont"
	 */
	private ComboBox<String> makeSoundfontsComboBox(Window owner) {

		controller.getSoundfontLoader().ensureSoundfontsFolderExists();
		
		// Populate combobox list with soundfonts in AppData
		final ComboBox<String> soundfontsMenu = new ComboBox<>();
		String[] listOfFiles = controller.getSoundfontLoader().getSoundfontsList();
		for (String filename : listOfFiles) {
			soundfontsMenu.getItems().add(filename);

			if (filename.equals(StateMachine.getCurrentSoundset()))
				soundfontsMenu.getSelectionModel().selectLast();
		}
		soundfontsMenu.setPrefWidth(150);
		soundfontsMenu.getItems().add("Add Soundfont...");
		soundfontsMenu.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obsv, String oldVal, String newValue) {
				if(newValue.equals("Add Soundfont...")) {
					FileChooser f = new FileChooser();
					f.setInitialDirectory(new File(System.getProperty("user.dir")));
					f.setTitle("Add Soundfont...");
					f.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("soundfonts (*.sf2)", "*.sf2"));
					final File sf = f.showOpenDialog(null);
					
					// Any programmed selection triggers the ChangedListener again 
					// Wrap in a runLater to avoid a huge stacktrace
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (sf == null) {
								soundfontsMenu.getSelectionModel().selectPrevious();
								return;
							}
							
							if(!controller.getSoundfontLoader().addSoundfont(sf, owner)) {
								soundfontsMenu.getSelectionModel().selectPrevious();
								return;
							}
							// Add soundfont name, or just pick it
							ObservableList<String> soundfonts = soundfontsMenu.getItems();
							int i;
							String sfName = sf.getName();
							for (i = 0; i < soundfonts.size() - 1; i++) {
								int compare = soundfonts.get(i).compareTo(sfName);
								if (compare == 0)
									break;
								else if (compare > 0) {
									soundfontsMenu.getItems().add(i, sfName);
									break;
								}
							}
							soundfontsMenu.getSelectionModel().select(i);
						}
					});
				} 
				
				bindBox.setSelected(newValue.equals(theStaff.getSequence().getSoundset()));
                /* we don't want to change the state of bindBox's userData so set it back to
                   theStaff.getSequence().getSoundset() */
				bindBox.setUserData(theStaff.getSequence().getSoundset());
			}
		});
		
		return soundfontsMenu;
	}


	private CheckBox makeBindCheckBox() {
		final CheckBox bindBox = new CheckBox();
		bindBox.setText("Bind to song");
		bindBox.setStyle("-fx-font-size: 9;");
		bindBox.setUserData(theStaff.getSequence().getSoundset());
		if (soundfontsMenu.getSelectionModel().getSelectedItem().equals(theStaff.getSequence().getSoundset()))
			bindBox.setSelected(true);
		bindBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean checked) {
				if(checked) {
					bindBox.setUserData(soundfontsMenu.getSelectionModel().getSelectedItem());
				} else {
					bindBox.setUserData("");
				}
			}
		});
		
		return bindBox;
	}
	
	/**
     * Sets the height, width, and other basic properties of this dialog box.
     *
     * @param dialog
     *            The dialog box that we are setting up.
     */
    private void initializeDialog(Stage dialog, Window owner) {
        dialog.setHeight(StateMachine.getState() == ProgramState.ARR_EDITING ? 220 : 300);
        dialog.setWidth(260);
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
        dV.setMax(Values.MAX_VELOCITY + 1);
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
        multiplyTempo();
        changeSoundfont();
    }

    /** Updates the default volume of the program notes. */
    private void changeDefaultVol() {
        int vol = (int) defaultVolume.getValue();
        Values.DEFAULT_VELOCITY = vol >= 128 ? 127 : vol;
    }

    /** Updates the tempo from the options dialog. */
    private void multiplyTempo() {
        String txt = tempoField.getText();
        if (txt != null) {
            int num = 1;
            try {
                num = Integer.parseInt(txt);
            } catch (NumberFormatException e) {
                return;
            }
            if (num <= 1)
                return;
            
            StaffSequence seq = theStaff.getSequence();
            double currTempo = StateMachine.getTempo();
            double newTempo = currTempo * num;
            
            seq.expand(num);
            seq.setTempo(newTempo);
            StateMachine.setTempo(newTempo);
            theStaff.getControlPanel().getScrollbar()
                    .setMax(seq.getLength() - Values.NOTELINES_IN_THE_WINDOW);
            
            controller.getModifySongManager().execute(new MultiplyTempoCommand(theStaff, num, currTempo, newTempo));
            controller.getModifySongManager().record();
        }
    }
    
    /** Updates the program's soundfont, bind to song if checked. */
    private void changeSoundfont() {
		SoundfontLoader sfLoader = controller.getSoundfontLoader();
		String selectedSoundfont = soundfontsMenu.getSelectionModel().getSelectedItem();
		try {
			sfLoader.loadFromAppData(selectedSoundfont);
		} catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
			e.printStackTrace();
		}
		
		String newSoundset = (String) bindBox.getUserData();
		if(!theStaff.getSequence().getSoundset().equals(newSoundset)) {
			theStaff.getSequence().setSoundset(newSoundset);
			StateMachine.setSongModified(true);
		}
    }
}
