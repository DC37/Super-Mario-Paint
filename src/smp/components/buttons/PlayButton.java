package smp.components.buttons;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.components.general.ImageRadioButton;
import smp.components.staff.Staff;
import smp.fx.SMPFXController;
import smp.stateMachine.State;
import smp.stateMachine.StateMachine;

/**
 * Wrapper class for an ImageView that holds the play button
 * image. Pressing the button changes the image and also changes
 * the state of the program.
 * @author RehdBlob
 * @since 2012.08.28
 */
public class PlayButton extends ImageRadioButton {

    /**
     * Instantiates the Play button on the staff
     * @param i The ImageView object that will be manipulated by this class.
     * @param controller The FXML controller object.
     */
    public PlayButton(ImageView i, SMPFXController controller) {
        super(i, controller);
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
