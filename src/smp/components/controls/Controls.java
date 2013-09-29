package smp.components.controls;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import smp.ImageIndex;
import smp.components.staff.Staff;
import smp.fx.SMPFXController;
import smp.stateMachine.State;
import smp.stateMachine.StateMachine;

/**
 * These are the control buttons for the program.
 * @author RehdBlob
 * @since 2012.09.04
 */
public class Controls {

    /** The pointer to the play button on the staff. */
    private PlayButton play;

    /** The pointer to the stop button on the staff. */
    private StopButton stop;

    /** The pointer to the loop button on the staff. */
    private LoopButton loop;

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
        theStaff.setCurrVal(scrollbar.valueProperty());
    }

    /** Initializes the play button and the stop button. */
    private void initializeControlButtons() {
        play = new PlayButton(SMPFXController.getPlayButton());
        stop = new StopButton (SMPFXController.getStopButton());
        loop = new LoopButton(SMPFXController.getLoopButton());
        play.link(stop);
        stop.link(play);
        play.setStaff(theStaff);
        stop.setStaff(theStaff);
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
                theStaff.getStaffImages().updateStaffMeasureLines(
                        newVal.intValue());
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

    /**
     * @return The scrollbar of the program.
     */
    public Slider getScrollbar() {
        return scrollbar;
    }

}
