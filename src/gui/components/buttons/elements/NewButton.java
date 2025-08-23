package gui.components.buttons.elements;

import backend.songs.StaffArrangement;
import backend.songs.StaffSequence;
import gui.Dialog;
import gui.ProgramState;
import gui.SMPFXController;
import gui.StateMachine;
import gui.Values;
import gui.components.buttons.ImagePushButton;
import gui.loaders.ImageLoader;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

/**
 * This is the button that creates a new song.
 *
 * @author RehdBlob
 * @since 2013.12.18
 *
 */
public class NewButton extends ImagePushButton {

    /**
     * Default constructor.
     *
     * @param i
     *            This is the <code>ImageView</code> object that will house the
     *            Load button.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public NewButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
        
        // @since v1.4 to accomodate for those with a smaller screen that may not be able to access it.
  		ct.getBasePane().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

  			@Override
  			public void handle(KeyEvent event) {
				if (controller.getNameTextField().focusedProperty().get()) return; // Disable while textfield is focused
  				if(event.isControlDown() && event.getCode() == KeyCode.N)
  					reactPressed(null);
  			}
  		});
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState();
    	Window owner = (event == null) ? null : ((Node) event.getSource()).getScene().getWindow();
        if (curr == ProgramState.EDITING)
            newSong(owner);
        else if (curr == ProgramState.ARR_EDITING)
            newArrangement(owner);
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        // do nothing.
    }

    /**
     * Creates a new song and clears the staff of all notes. Make sure you save
     * your song first! The action is ignored if the song is playing.
     */
    private void newSong(Window owner) {
        boolean cont = true;
        if (StateMachine.isSongModified())
            cont = Dialog
                    .showYesNoDialog("The current song has been modified!\n"
                            + "Create a new song anyway?", owner);

        if (cont) {
            theStaff.setSequence(new StaffSequence());
            theStaff.setSequenceFile(null);
            theStaff.setTimeSignature(Values.DEFAULT_TIME_SIGNATURE);
            theStaff.resetLocation();
            StateMachine.setMaxLine(Values.DEFAULT_LINES_PER_SONG);
            controller.getNameTextField().clear();
            StateMachine.setSongModified(false);
        }
    }

    /**
     * Creates a new arrangement and clears the staff of all notes. Make sure
     * you save your arrangement first! The action is ignored if an arrangement
     * is playing.
     */
    private void newArrangement(Window owner) {
        boolean cont = true;
        if (StateMachine.isArrModified()) {
            cont = Dialog
                    .showYesNoDialog("The current arrangement has been\n"
                            + "modified! Create a new arrangement\nanyway?", owner);
        }
        if (cont) {
            theStaff.setArrangement(new StaffArrangement());
            theStaff.setArrangementFile(null);
            controller.getNameTextField().clear();
            theStaff.getArrangementList().getItems().clear();
            StateMachine.setArrModified(false);
        }
    }

}
