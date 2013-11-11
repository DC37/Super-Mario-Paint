package smp.components.controls;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.components.general.ImageToggleButton;
import smp.stateMachine.StateMachine;

/**
 * This is the button, that when clicked, toggles the loop function
 * of this program.
 * @author RehdBlob
 * @since 2013.08.29
 */
public class LoopButton extends ImageToggleButton {

    /**
     * This creates a new LoopButton object.
     * @param i This <code>ImageView</code> object that you are
     * trying to link this button with.
     */
    public LoopButton(ImageView i) {
        super(i);
        getImages(ImageIndex.LOOP_PRESSED, ImageIndex.LOOP_RELEASED);
        releaseImage();
        isPressed = false;
    }

    @Override
    protected void reactClicked(MouseEvent event) {
        // Do nothing

    }

    @Override
    protected void reactPressed(MouseEvent event) {
        if (isPressed) {
            isPressed = false;
            pressImage();
            StateMachine.setLoopPressed();

        } else {
            isPressed = true;
            releaseImage();
            StateMachine.resetLoopPressed();
        }

    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

}
