package smp.components.controls;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import smp.ImageIndex;
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
    public Controls(Staff st) {
        theStaff = st;
        setScrollbar(SMPFXController.getScrollbar());
        initializeArrows();
        initializeScrollbar();
        initializeControlButtons();
        initializeLoopButton();
    }

    /** Initializes the loop button. */
    private void initializeLoopButton() {
        play = new PlayButton(new ImageView());
        stop = new StopButton (new ImageView());
        play.link(stop);
        stop.link(play);
        SMPFXController.getControlPanel();
    }

    /** Initializes the play button and the stop button. */
    private void initializeControlButtons() {

    }

    /**
     * Sets up the slider and arrows that the controls will have.
     */
    private void initializeArrows() {
        leftArrow = new ArrowButton(SMPFXController.getLeftArrow(),
                scrollbar, ImageIndex.SCROLLBAR_LEFT1_PRESSED,
                ImageIndex.SCROLLBAR_LEFT1);
        rightArrow = new ArrowButton(SMPFXController.getRightArrow(),
                scrollbar, ImageIndex.SCROLLBAR_RIGHT1_PRESSED,
                ImageIndex.SCROLLBAR_RIGHT1);
        leftFastArrow = new ArrowButton(SMPFXController.getLeftFastArrow(),
                scrollbar, ImageIndex.SCROLLBAR_LEFT2_PRESSED,
                ImageIndex.SCROLLBAR_LEFT2);
        rightFastArrow = new ArrowButton(SMPFXController.getRightFastArrow(),
                scrollbar, ImageIndex.SCROLLBAR_RIGHT2_PRESSED,
                ImageIndex.SCROLLBAR_RIGHT2);

        leftArrow.setSkipAmount(-1);
        rightArrow.setSkipAmount(1);
        rightFastArrow.setSkipAmount(Double.MAX_VALUE);
        leftFastArrow.setSkipAmount(-Double.MAX_VALUE);
    }


    /** Sets up the scollbar to affect the staff in some way. */
    private void initializeScrollbar() {
        scrollbar.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0,
                    Number oldVal, Number newVal) {
                StateMachine.setMeasureLineNum(newVal.intValue());
                theStaff.setLocation(newVal.intValue());
            }

        });
    }

    /** Starts the song. */
    public void play() {
        if (StateMachine.getState() == State.EDITING);

    }

    /** Stops the song. */
    public void stop() {

    }

    /**
     * Sets the scollbar that we will be using.
     * @param scroll The slider that we'll be using.
     */
    public void setScrollbar(Slider scroll) {
        scrollbar = scroll;
    }

}
