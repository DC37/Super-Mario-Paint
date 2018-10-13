package smp.presenters.buttons;

import javafx.beans.property.ObjectProperty;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.StateMachine;
import smp.presenters.api.button.ImageToggleButton;

/**
 * The button that changes the mode of Super Mario Paint between arranger and
 * song modes.
 *
 * @author RehdBlob
 * @since 2014.05.21
 */
public class ModeButtonPresenter extends ImageToggleButton {

	//TODO: auto-add these model comments
	//====Models====
    private ObjectProperty<ProgramState> programState;
	
    /**
     * This creates a new ModeButton object.
     *
     * @param modeButton
     *            This <code>ImageView</code> object that you are trying to link
     *            this button with.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public ModeButtonPresenter(ImageView modeButton) {
        super(modeButton);
        this.programState = StateMachine.getState();
    }

    @Override
    public void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState().get();
        if (curr != ProgramState.ARR_PLAYING && curr != ProgramState.SONG_PLAYING) {
            if (curr == ProgramState.EDITING) {
                this.programState.set(ProgramState.ARR_EDITING);
            } else if (curr == ProgramState.ARR_EDITING) {
                this.programState.set(ProgramState.EDITING);
            }
        }
    }

}
