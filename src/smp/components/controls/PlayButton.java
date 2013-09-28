package smp.components.controls;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.components.general.ImageRadioButton;
import smp.components.staff.Staff;
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

    /** Pointer to the staff object this button will affect. */
    private Staff theStaff;

    /**
     * Instantiates the Play button on the staff
     * @param i The ImageView object that will be manipulated by this class.
     */
    public PlayButton(ImageView i) {
        super(i);
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

    /**
     * @param s Pointer to the staff object that this button is to affect.
     */
    public void setStaff(Staff s) {
        theStaff = s;
    }

}
