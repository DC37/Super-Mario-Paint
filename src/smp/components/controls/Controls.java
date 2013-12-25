package smp.components.controls;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import smp.ImageIndex;
import smp.components.staff.Staff;
import smp.fx.SMPFXController;
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

    /** The pointer to the mute button on the staff. */
    private MuteButton mute;

    /** The pointer to the mute-all button ont he staff. */
    private MuteInstButton muteA;

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

    /** This is the current tempo. */
    private StringProperty currTempo = new SimpleStringProperty();

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
        initializeTempoButtons();
        currTempo.setValue(String.valueOf(StateMachine.getTempo()));
        initializeLoadSaveButtons();
        initializeNewButton();
    }

    /**
     * Initializes the new song button.
     */
    private void initializeNewButton() {
        NewButton n = new NewButton(SMPFXController.getNewButton());
        n.setStaff(theStaff);
    }

    /** Initializes the load and save buttons which allow one to keep songs. */
    private void initializeLoadSaveButtons() {
        SaveButton s = new SaveButton(SMPFXController.getSaveButton());
        s.setStaff(theStaff);
        LoadButton l = new LoadButton(SMPFXController.getLoadButton());
        l.setStaff(theStaff);
    }

    /** Initializes the plus and minus buttons that can change the tempo. */
    private void initializeTempoButtons() {
        TempoAdjustButton plus = new TempoAdjustButton(
                SMPFXController.getTempoPlus());
        TempoAdjustButton minus = new TempoAdjustButton(
                SMPFXController.getTempoMinus());
        plus.setPositive(true);
        minus.setPositive(false);
        StringProperty tempoDisplay =
                SMPFXController.getTempoIndicator().textProperty();
        currTempo.bindBidirectional(tempoDisplay);
        plus.setStringProperty(currTempo);
        minus.setStringProperty(currTempo);
    }

    /** Initializes the play button and the stop button. */
    private void initializeControlButtons() {
        play = new PlayButton(SMPFXController.getPlayButton());
        stop = new StopButton(SMPFXController.getStopButton());
        loop = new LoopButton(SMPFXController.getLoopButton());
        mute = new MuteButton(SMPFXController.getMuteButton());
        muteA = new MuteInstButton(SMPFXController.getMuteAButton());
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

    /** @return The play button of the controls set. */
    public PlayButton getPlayButton() {
        return play;
    }

    /** @return The stop button of the controls set. */
    public StopButton getStopButton() {
        return stop;
    }

    /** @return The loop button of the controls set. */
    public LoopButton getLoopButton() {
        return loop;
    }

    /** @return The mute button of the controls set. */
    public MuteButton getMuteButton() {
        return mute;
    }

    /** @return The 'mute-all' button of the controls set. */
    public MuteInstButton getMuteAButton() {
        return muteA;
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

    /** Updates the current tempo. */
    public void updateCurrTempo() {
        currTempo.setValue(String.valueOf(StateMachine.getTempo()));
    }

}
