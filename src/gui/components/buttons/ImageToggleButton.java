package gui.components.buttons;

import gui.SMPFXController;
import gui.loaders.ImageLoader;
import javafx.scene.image.ImageView;

/**
 * Image toggle button that is an on/off switch. By clicking on it, the button
 * is either locked down or up.
 * 
 * @author RehdBlob
 * @since 2012.08.30
 */
public abstract class ImageToggleButton extends AbstractImageButton {

    /**
     * @param i
     *            The ImageView passed to the Button wrapper.
     * @param controller
     *            The FXML controller class.
     */
    public ImageToggleButton(ImageView i, SMPFXController controller,
            ImageLoader im) {
        super(i, controller, im);
    }

}
