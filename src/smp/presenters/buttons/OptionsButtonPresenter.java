package smp.presenters.buttons;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import smp.models.staff.StaffNoteLine;
import smp.models.staff.StaffSequence;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.StateMachine;
import smp.models.stateMachine.Variables;
import smp.presenters.api.button.ImagePushButton;
import smp.views.OptionsWindowController;

/**
 * This is the options button. It currently doesn't do anything.
 *
 * @author RehdBlob
 * @since 2013.12.25
 */
public class OptionsButtonPresenter extends ImagePushButton {

	//TODO: auto-add these model comments
	//====Models====
	private ObjectProperty<StaffSequence> theSequence;
	private IntegerProperty defaultVelocity;
	private StringProperty currentSoundset;
	private BooleanProperty songModified;

    /**
     * Default constructor.
     *
     * @param optionsButton
     *            The image that we want to link this to.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public OptionsButtonPresenter(ImageView optionsButton) {
        super(optionsButton);
        this.defaultVelocity = Variables.defaultVelocity;
        this.theSequence = Variables.theSequence;
        this.currentSoundset = StateMachine.getCurrentSoundset();
        this.songModified = StateMachine.getSongModified();
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState().get();
        if (curr == ProgramState.EDITING)
			try {
				options();
			} catch (IOException e) {
				e.printStackTrace();
			}
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        // do nothing.
    }

    /** Opens up an options dialog. 
     * @throws IOException */
    private void options() throws IOException {
        final Stage dialog = new Stage();
        initializeDialog(dialog);
        
        FXMLLoader loader = new FXMLLoader();
        OptionsWindowController owc = new OptionsWindowController();
        loader.setController(owc);
        loader.setLocation(new File(owc.getFXML()).toURI().toURL());
        
        Scene scene1 = new Scene((Parent) loader.load());
        dialog.setScene(scene1);
        dialog.showAndWait();
        while (dialog.isShowing()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // Do nothing
            }
        }
        updateValues();
    }

	/**
     * Sets the height, width, and other basic properties of this dialog box.
     *
     * @param dialog
     *            The dialog box that we are setting up.
     */
    private void initializeDialog(Stage dialog) {
        dialog.setHeight(280);
        dialog.setWidth(250);
        dialog.setResizable(false);
        dialog.setTitle("Options");
        dialog.initStyle(StageStyle.UTILITY);
    }

    /** Updates the different values of this program. */
    private void updateValues() {
        changeDefaultVol();
        multiplyTempo();
        changeSoundfont();
    }

    /** Updates the default volume of the program notes. */
    private void changeDefaultVol() {
        int vol = Variables.optionsDefaultVolume.get();
        this.defaultVelocity.set(vol >= 128 ? 127 : vol);
    }

    /** Updates the tempo from the options dialog. */
    private void multiplyTempo() {
        String txt = Variables.optionsTempoMultiplier.get();
        if (txt != null) {
            int num = 1;
            try {
                num = Integer.parseInt(txt);
            } catch (NumberFormatException e) {
                return;
            }
            if (num <= 1)
                return;
            ObservableList<StaffNoteLine> s = this.theSequence.get().getTheLines();
            ArrayList<StaffNoteLine> n = new ArrayList<StaffNoteLine>();
            for (int i = 0; i < s.size(); i++) {
                n.add(s.get(i));
                for (int j = 0; j < num - 1; j++)
                    n.add(new StaffNoteLine());
            }
            s.clear();
            s.addAll(n);
            DoubleProperty tempo = this.theSequence.get().getTempo();
            tempo.set(tempo.get() * num);
            
            //TODO:
//            controller.getModifySongManager().execute(new MultiplyTempoCommand(theStaff, num));
//            controller.getModifySongManager().record();
        }
    }
    
    /** Updates the program's soundfont, bind to song if checked. */
    private void changeSoundfont() {
    	//TODO:
//		SoundfontLoader sfLoader = controller.getSoundfontLoader();
//		String selectedSoundfont = soundfontsMenu.getSelectionModel().getSelectedItem();
//		try {
//			sfLoader.loadFromAppData(selectedSoundfont);
//		} catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
//			e.printStackTrace();
//		}
		
		String newSoundset = Variables.optionsBindedSoundfont.get();
		if(!theSequence.get().getSoundset().get().equals(newSoundset)) {
			theSequence.get().setSoundset(newSoundset);
			songModified.set(true);
		}
    }
}
