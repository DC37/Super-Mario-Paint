package smp.components.buttons;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.general.ImageRadioButton;
import smp.fx.SMPFXController;
import smp.stateMachine.ProgramState;
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
        if (e != null) {
        	theStaff.stopSounds();
        }
        if (isPressed)
            return;
        super.reactPressed(e);
        if (StateMachine.getState() == ProgramState.SONG_PLAYING) {
            StateMachine.setState(ProgramState.EDITING);
            theStaff.stopSong();
        } else if (StateMachine.getState() == ProgramState.ARR_PLAYING) {
            StateMachine.setState(ProgramState.ARR_EDITING);
            theStaff.stopSong();
        }
    }


}
