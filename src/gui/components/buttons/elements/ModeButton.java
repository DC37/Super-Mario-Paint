package gui.components.buttons.elements;

import gui.SMPFXController;
import gui.components.buttons.ImageToggleButton;
import gui.loaders.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * The button that changes the mode of Super Mario Paint between arranger and
 * song modes.
 *
 * @author RehdBlob
 * @since 2014.05.21
 */
public class ModeButton extends ImageToggleButton {

    /**
     * This creates a new ModeButton object.
     *
     * @param i
     *            This <code>ImageView</code> object that you are trying to link
     *            this button with.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public ModeButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
    }

    @Override
    public void reactPressed(MouseEvent event) {
        controller.switchMode();
    }

}
