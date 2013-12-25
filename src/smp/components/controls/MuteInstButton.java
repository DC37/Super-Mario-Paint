package smp.components.controls;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.components.general.ImageRadioButton;
import smp.components.general.ImageToggleButton;
import smp.stateMachine.StateMachine;


/**
 * This is a button that allows us to mute all of one type of instrument,
 * much like the low A glitch note in MPC.
 * @author RehdBlob
 * @since 2013.12.24
 */
public class MuteInstButton extends ImageToggleButton {

    /** The mute button that is linked to this button. */
    private MuteButton mt;

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
    public void reactPressed(MouseEvent event) {
        if (isPressed) {
            isPressed = false;
            releaseImage();
            StateMachine.setMuteAPressed(false);
        } else {
            if (mt.isPressed())
                mt.reactPressed(null);
            isPressed = true;
            pressImage();
            StateMachine.setMuteAPressed(true);
        }

    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

    /** @param im The mute button that we want to set. */
    public void setMuteButton(MuteButton im) {
        mt = im;
    }

    /** @return The mute button that this muteA button is linked to. */
    public ImageToggleButton getMuteButton() {
        return mt;
    }

}
