package smp.components.controls;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.components.general.ImageRadioButton;
import smp.stateMachine.StateMachine;


/**
 * This is a button that allows us to mute all of one type of instrument,
 * much like the low A glitch note in MPC.
 * @author RehdBlob
 * @since 2013.12.24
 */
public class MuteInstButton extends ImageRadioButton {

    /**
     * This creates a new MuteButton object.
     * @param i This <code>ImageView</code> object that you are
     * trying to link this button with.
     */
    public MuteInstButton(ImageView i) {
        super(i);
        getImages(ImageIndex.MUTE_A_PRESSED, ImageIndex.MUTE_A_RELEASED);
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
            StateMachine.setMuteAPressed(true);

        } else {
            isPressed = true;
            releaseImage();
            StateMachine.setMuteAPressed(false);
        }

    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }
}
