package smp.components.controls;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.components.general.ImageToggleButton;
import smp.stateMachine.StateMachine;

/**
 * This is the button that, when pressed, toggles whether you are
 * placing a mute note or not.
 * @author RehdBlob
 * @since 2013.11.10
 *
 */
public class MuteButton extends ImageToggleButton {

    /**
     * This creates a new MuteButton object.
     * @param i This <code>ImageView</code> object that you are
     * trying to link this button with.
     */
    public MuteButton(ImageView i) {
        super(i);
        getImages(ImageIndex.MUTE_PRESSED, ImageIndex.MUTE_RELEASED);
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
