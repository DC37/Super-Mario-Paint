package gui.components.buttons.elements;

import gui.SMPFXController;
import gui.components.buttons.ImageRadioButton;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Wrapper class for an ImageView that holds the play button image. Pressing the
 * button changes the image and also changes the state of the program.
 *
 * @author RehdBlob
 * @since 2012.08.28
 */
public class PlayButton extends ImageRadioButton {

    /**
     * Instantiates the Play button on the staff
     *
     * @param i
     *            The ImageView object that will be manipulated by this class.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public PlayButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
        getImages(ImageIndex.PLAY_PRESSED, ImageIndex.PLAY_RELEASED);
        releaseImage();
        isPressed = false;
    }

    @Override
    public void reactPressed(MouseEvent e) {
        if (isPressed)
            return;
        super.reactPressed(e);
        theStaff.play();
    }

}
