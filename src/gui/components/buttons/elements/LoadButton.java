package gui.components.buttons.elements;

import gui.SMPFXController;
import gui.Utilities;
import gui.components.buttons.ImagePushButton;
import gui.loaders.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

/**
 * This is the button that loads a song.
 *
 * @author RehdBlob
 * @since 2013.09.28
 */
public class LoadButton extends ImagePushButton {
    
    /**
     * Default constructor.
     *
     * @param i
     *            This is the <code>ImageView</code> object that will house the
     *            Load button.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public LoadButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        Window owner = Utilities.getOwner(event);
        controller.load(owner);
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        // do nothing.
    }

}
