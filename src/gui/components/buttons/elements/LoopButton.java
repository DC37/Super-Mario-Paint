package smp.components.buttons;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.general.ImageToggleButton;
import smp.fx.SMPFXController;
import smp.stateMachine.ProgramState;
import smp.stateMachine.StateMachine;

/**
 * This is the button, that when clicked, toggles the loop function of this
 * program.
 *
 * @author RehdBlob
 * @since 2013.08.29
 */
public class LoopButton extends ImageToggleButton {

    /**
     * This creates a new LoopButton object.
     *
     * @param i
     *            This <code>ImageView</code> object that you are trying to link
     *            this button with.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public LoopButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
        getImages(ImageIndex.LOOP_PRESSED, ImageIndex.LOOP_RELEASED);
        releaseImage();
        isPressed = false;
    }

    /** Releases the loop button. */
    public void release() {
        if (isPressed)
            reactPressed(null);
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState();
        if (curr != ProgramState.ARR_EDITING && curr != ProgramState.ARR_PLAYING) {
            if (isPressed) {
                isPressed = false;
                releaseImage();
                StateMachine.resetLoopPressed();
            } else {
                isPressed = true;
                pressImage();
                StateMachine.setLoopPressed();
            }
        }
    }

}
