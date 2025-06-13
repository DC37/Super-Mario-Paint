package gui.components.buttons.elements;

import gui.SMPFXController;
import gui.components.buttons.ImageRadioButton;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Wrapper class for an ImageView that holds the stop button image. Pressing the
 * button changes the image and also changes the state of the program.
 *
 * @author RehdBlob
 * @since 2012.09.14
 */
public class StopButton extends ImageRadioButton {

    /**
     * Instantiates the stop button.
     *
     * @param i
     *            The ImageView that will be manipulated by this class.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public StopButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
        getImages(ImageIndex.STOP_PRESSED, ImageIndex.STOP_RELEASED);
        pressImage();
        isPressed = true;
    }
    

    @Override
    public void reactPressed(MouseEvent e) {
        if (e != null) {
        	theStaff.stopSounds();
        }
        if (isPressed)
            return;
        super.reactPressed(e);
        theStaff.stop();
    }


}
