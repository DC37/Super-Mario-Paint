package smp.components.controls;

import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.staff.Staff;
import smp.fx.SMPFXController;
import smp.stateMachine.State;
import smp.stateMachine.StateMachine;

public class Controls {

    /** The pointer to the play button on the staff. */
    private PlayButton play;

    /** The pointer to the stop button on the staff. */
    private StopButton stop;

    /** This is the slider at the bottom of the screen. */
    private Slider scrollbar;

    /** The arrow that you click to go left. */
    private ArrowButton leftArrow;

    /** The arrow that you click to go all the way left. */
    private ArrowButton leftFastArrow;

    /** The arrow that you click to go right. */
    private ArrowButton rightArrow;

    /** The arrow that you click to go all the way right. */
    private ArrowButton rightFastArrow;

    /** This is the staff that the controls are linked to. */
    private Staff theStaff;

    /**
     * Initializes the set of controls that will be used in Super Mario Paint.
     */
    public Controls() {
        setScrollbar(SMPFXController.getScrollbar());
        leftArrow = new ArrowButton(SMPFXController.getLeftArrow());
        rightArrow = new ArrowButton(SMPFXController.getRightArrow());
        leftFastArrow = new ArrowButton(SMPFXController.getLeftFastArrow());
        rightFastArrow = new ArrowButton(SMPFXController.getRightFastArrow());

        leftArrow.getImages(ImageIndex.SCROLLBAR_LEFT1_PRESSED,
                ImageIndex.SCROLLBAR_LEFT1);
        rightArrow.getImages(ImageIndex.SCROLLBAR_RIGHT1_PRESSED,
                ImageIndex.SCROLLBAR_RIGHT1);
        leftFastArrow.getImages(ImageIndex.SCROLLBAR_LEFT2_PRESSED,
                ImageIndex.SCROLLBAR_LEFT2);
        rightFastArrow.getImages(ImageIndex.SCROLLBAR_RIGHT2_PRESSED,
                ImageIndex.SCROLLBAR_RIGHT2);
    }

    /** Starts the song. */
    public void play() {
        if (StateMachine.getState() == State.EDITING);

    }

    /** Stops the song. */
    public void stop() {
        State s = StateMachine.getState();
    }

    /**
     * Sets the scollbar that we will be using.
     * @param scroll The slider that we'll be using.
     */
    public void setScrollbar(Slider scroll) {
        scrollbar = scroll;
    }

}
