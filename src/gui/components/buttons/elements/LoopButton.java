package gui.components.buttons.elements;

import gui.SMPFXController;
import gui.SMPMode;
import gui.StateMachine;
import gui.components.buttons.ImageToggleButton;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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
        if (StateMachine.getMode() == SMPMode.ARRANGEMENT)
            return;
        
        if (isPressed) {
            isPressed = false;
            releaseImage();
            StateMachine.setLoopPressed(false);
        } else {
            isPressed = true;
            pressImage();
            StateMachine.setLoopPressed(true);
        }
    }

}
