package smp.components.buttons;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.general.ImageRadioButton;
import smp.components.staff.Staff;
import smp.fx.SMPFXController;
import smp.stateMachine.State;
import smp.stateMachine.StateMachine;

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
        if (isPressed)
            return;
        super.reactPressed(e);
        StateMachine.setState(State.EDITING);
        theStaff.stopSong();
    }

}
