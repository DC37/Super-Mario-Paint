package smp.components.controls;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import smp.ImageIndex;
import smp.components.buttons.AddButton;
import smp.components.buttons.ArrowButton;
import smp.components.buttons.DeleteButton;
import smp.components.buttons.LoadButton;
import smp.components.buttons.LoopButton;
import smp.components.buttons.MoveButton;
import smp.components.buttons.MuteButton;
import smp.components.buttons.MuteInstButton;
import smp.components.buttons.NewButton;
import smp.components.buttons.OptionsButton;
import smp.components.buttons.PlayButton;
import smp.components.buttons.SaveButton;
import smp.components.buttons.StopButton;
import smp.components.buttons.TempoAdjustButton;
import smp.components.staff.Staff;
import smp.fx.Dialog;
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

    /** The pointer to the button that adds a new song on the arranger. */
    private AddButton add;

    /** The pointer to the button that deletes a song from a list. */
    private DeleteButton delete;

    /** The pointer to the button that moves a song up. */
    private MoveButton moveUp;

    /** The pointer to the button that moves a song down. */
    private MoveButton moveDown;

    /** The pointer to the options button on the staff. */
    private OptionsButton options;

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

    /** The ListView of songs for the arranger. */
    private ListView<String> theList;

    /**
     * Initializes the set of controls that will be used in Super Mario Paint.
     */
    public Controls(Staff st) {
        theStaff = st;
        setScrollbar(SMPFXController.getScrollbar());
        theList = SMPFXController.getArrangementList();
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
        StackPane tBox = SMPFXController.getTempoBox();

        tBox.setOnMousePressed(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            String tempo = Dialog.showTextDialog("Tempo");
                            StateMachine.setTempo(Double.parseDouble(tempo));
                            tempo = tempo.trim();
                            currTempo.setValue(tempo);
                        } catch (NumberFormatException e) {
                            // Do nothing.
                        }
                        event.consume();
                    }
                });
    }

    /** Initializes the play button and the stop button. */
    private void initializeControlButtons() {
        play = new PlayButton(SMPFXController.getPlayButton());
        stop = new StopButton(SMPFXController.getStopButton());
        loop = new LoopButton(SMPFXController.getLoopButton());
        mute = new MuteButton(SMPFXController.getMuteButton());
        muteA = new MuteInstButton(SMPFXController.getMuteAButton());
        options = new OptionsButton(SMPFXController.getOptionsButton());
        add = new AddButton(SMPFXController.getAddButton());
        delete = new DeleteButton(SMPFXController.getDeleteButton());
        moveUp = new MoveButton(SMPFXController.getUpButton(), 1);
        moveDown = new MoveButton(SMPFXController.getDownButton(), -1);

        mute.setMuteButton(muteA);
        muteA.setMuteButton(mute);
        mute.setStaff(theStaff);
        muteA.setStaff(theStaff);

        play.link(stop);
        stop.link(play);

        play.setStaff(theStaff);
        stop.setStaff(theStaff);
        options.setStaff(theStaff);
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

        leftArrow.setStaff(theStaff);
        rightArrow.setStaff(theStaff);
        rightFastArrow.setStaff(theStaff);
        leftFastArrow.setStaff(theStaff);
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

    /** @param b Whether we want to turn on arranger mode or not. */
    public void setArrangerMode(boolean b) {
        if (b)
            setArrangerMode();
        else
            setEditingMode();
    }

    /** Changes the current interface to the arranger mode. */
    private void setArrangerMode() {
        changeCenterList();
        changeTempoButtons();
        StateMachine.setState(State.ARR_EDITING);

    }

    /**
     * Adds the list of songs characteristic of the arranger mode.
     */
    private void changeCenterList() {
        SMPFXController.getSongName().setPromptText("Arrangement Name");
        SMPFXController.getArrangementList().setVisible(true);
        SMPFXController.getDeleteButton().setVisible(true);
        SMPFXController.getAddButton().setVisible(true);
        SMPFXController.getUpButton().setVisible(true);
        SMPFXController.getDownButton().setVisible(true);
    }

    /**
     * Removes the tempo buttons and adds the add song / delete song /
     * insert song buttons.
     */
    private void changeTempoButtons() {

    }

    /** Changes the current interface to the normal song editing mode. */
    private void setEditingMode() {
        revertCenterList();
        revertTempoButtons();
        StateMachine.setState(State.EDITING);
    }

    /**
     * Reverts the center list into just the middle panel.
     */
    private void revertCenterList() {
        SMPFXController.getSongName().setPromptText("Song Name");
        SMPFXController.getArrangementList().setVisible(false);
        SMPFXController.getDeleteButton().setVisible(false);
        SMPFXController.getAddButton().setVisible(false);
        SMPFXController.getUpButton().setVisible(false);
        SMPFXController.getDownButton().setVisible(false);
    }

    /**
     * Reverts the tempo buttons back to their original state.
     */
    private void revertTempoButtons() {

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

    /** @return The options button. */
    public OptionsButton getOptionsButton() {
        return options;
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
