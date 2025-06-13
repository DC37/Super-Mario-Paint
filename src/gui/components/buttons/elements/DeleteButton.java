package gui.components.buttons.elements;

import gui.SMPFXController;
import gui.components.buttons.ImagePushButton;
import gui.loaders.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * This is a button that deletes a song from an arrangement.
 *
 * @author RehdBlob
 * @since 2014.07.27
 */
public class DeleteButton extends ImagePushButton {

    /**
     * Default constructor.
     *
     * @param i
     *            The <code>ImageView</code> object that we are going to make
     *            into a button.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public DeleteButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
    }


    @Override
    protected void reactPressed(MouseEvent event) {
        theStaff.deleteSongFromArrangement();
    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

}
