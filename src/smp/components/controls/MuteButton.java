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

    /** The muteA button that this mute button is linked to. */
    private MuteInstButton mt;

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
    public void reactPressed(MouseEvent event) {
        if (isPressed) {
            isPressed = false;
            releaseImage();
            StateMachine.resetMutePressed();
        } else {
            if (mt.isPressed())
                mt.reactPressed(null);
            isPressed = true;
            pressImage();
            StateMachine.setMutePressed();

        }

    }

    /** @param im The mute button that we want to set. */
    public void setMuteButton(MuteInstButton im) {
        mt = im;
    }

    /** @return The mute button that this muteA button is linked to. */
    public ImageToggleButton getMuteButton() {
        return mt;
    }

}
