package smp.presenters.buttons;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.fx.Dialog;
import smp.models.staff.StaffArrangement;
import smp.models.staff.StaffSequence;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.StateMachine;
import smp.models.stateMachine.Variables;
import smp.presenters.api.button.ImagePushButton;

/**
 * This is the button that creates a new song.
 *
 * @author RehdBlob
 * @since 2013.12.18
 *
 */
public class NewButtonPresenter extends ImagePushButton {

	//TODO: auto-add these model comments
	//====Models====
    private ObjectProperty<StaffSequence> theSequence;
	private DoubleProperty measureLineNum;
	private BooleanProperty songModified;
	private BooleanProperty arrModified;
	private ObjectProperty<StaffArrangement> theArrangement;

	/**
     * Default constructor.
     *
     * @param newButton
     *            This is the <code>ImageView</code> object that will house the
     *            Load button.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public NewButtonPresenter(ImageView newButton) {
        super(newButton);
        this.theSequence = Variables.theSequence;
        this.measureLineNum = StateMachine.getMeasureLineNum();
        this.songModified = StateMachine.getSongModified();
        this.arrModified = StateMachine.getArrModified();
        this.theArrangement = Variables.theArrangement;
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState().get();
        if (curr == ProgramState.EDITING)
            newSong();
        else if (curr == ProgramState.ARR_EDITING)
            newArrangement();
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        // do nothing.
    }

    /**
     * Creates a new song and clears the staff of all notes. Make sure you save
     * your song first! The action is ignored if the song is playing.
     */
    private void newSong() {
        boolean cont = true;
        if (this.songModified.get())
            cont = Dialog
                    .showYesNoDialog("The current song has been modified!\n"
                            + "Create a new song anyway?");

        if (cont) {
            this.theSequence.set(new StaffSequence());
//            theStaff.setSequenceFile(null);??
            measureLineNum.set(0);
            this.songModified.set(false);
        }
    }

    /**
     * Creates a new arrangement and clears the staff of all notes. Make sure
     * you save your arrangement first! The action is ignored if an arrangement
     * is playing.
     */
    private void newArrangement() {
        boolean cont = true;
        if (this.arrModified.get()) {
            cont = Dialog
                    .showYesNoDialog("The current arrangement has been\n"
                            + "modified! Create a new arrangement\nanyway?");
        }
        if (cont) {
        	this.theArrangement.set(new StaffArrangement());
//            theStaff.setArrangementFile(null);??
            this.arrModified.set(false);
        }
    }

}
