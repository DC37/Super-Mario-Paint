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
    protected void reactPressed(MouseEvent e) {
        if (isPressed)
            return;
        super.reactPressed(e);
        StateMachine.setState(State.SONG_PLAYING);
        theStaff.startSong();
    }

}
