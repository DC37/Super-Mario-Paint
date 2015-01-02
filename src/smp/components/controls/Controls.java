package smp.components.controls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.Values;
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
import smp.components.general.Utilities;
import smp.components.staff.Staff;
import smp.components.staff.sequences.StaffSequence;
import smp.fx.Dialog;
import smp.fx.SMPFXController;
import smp.stateMachine.ProgramState;
import smp.stateMachine.StateMachine;

/**
 * These are the control buttons for the program.
 *
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

    /** The controller object. */
    private SMPFXController controller;

    /** The image loader object. */
    private ImageLoader il;

    /**
     * Initializes the set of controls that will be used in Super Mario Paint.
     */
    public Controls(Staff st, SMPFXController ct, ImageLoader im,
            ListView<String> l) {
        theList = l;
        il = im;
        theStaff = st;
        setController(ct);
        setScrollbar(controller.getScrollbar());
        theList = controller.getArrangementList();
        initializeArrows();
        initializeScrollbar();
        initializeControlButtons();
        theStaff.setCurrVal(scrollbar.valueProperty());
        initializeTempoButtons();
        currTempo.setValue(String.valueOf(StateMachine.getTempo()));
        initializeLoadSaveButtons();
        initializeNewButton();
        initializeArrangementList();

    }

    /**
     * Adds in the listener behaviour for the arrangement list.
     */
    private void initializeArrangementList() {
        theList.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends String> observable,
                            String oldValue, String newValue) {
                        int x = theList.getSelectionModel().getSelectedIndex();
                        if (x != -1
                                && StateMachine.getState() != ProgramState.ARR_PLAYING) {
                            ArrayList<StaffSequence> s = theStaff
                                    .getArrangement().getTheSequences();
                            ArrayList<File> f = theStaff.getArrangement()
                                    .getTheSequenceFiles();
                            loadArrangementFile(f.get(x));
                            s.set(x, theStaff.getSequence());
                        }
                    }
                });
    }

    /**
     * Initializes the new song button.
     */
    private void initializeNewButton() {
        NewButton n = new NewButton(controller.getNewButton(), controller, il);
        n.setStaff(theStaff);
    }

    /** Initializes the load and save buttons which allow one to keep songs. */
    private void initializeLoadSaveButtons() {
        SaveButton s = new SaveButton(controller.getSaveButton(), controller,
                il);
        s.setStaff(theStaff);
        LoadButton l = new LoadButton(controller.getLoadButton(), controller,
                il);
        l.setStaff(theStaff);
    }

    /** Initializes the plus and minus buttons that can change the tempo. */
    private void initializeTempoButtons() {
        TempoAdjustButton plus = new TempoAdjustButton(
                controller.getTempoPlus(), controller, il);
        TempoAdjustButton minus = new TempoAdjustButton(
                controller.getTempoMinus(), controller, il);
        plus.setPositive(true);
        minus.setPositive(false);
        StringProperty tempoDisplay = controller.getTempoIndicator()
                .textProperty();
        currTempo.bindBidirectional(tempoDisplay);
        plus.setStringProperty(currTempo);
        minus.setStringProperty(currTempo);
        StackPane tBox = controller.getTempoBox();

        tBox.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    ProgramState curr = StateMachine.getState();
                    if (curr == ProgramState.EDITING) {
                        String tempo = Dialog.showTextDialog("Tempo");
                        StateMachine.setTempo(Double.parseDouble(tempo));
                        tempo = tempo.trim();
                        currTempo.setValue(tempo);
                    }
                } catch (NumberFormatException e) {
                    // Do nothing.
                }
                event.consume();
            }
        });
    }

    /** Initializes the play button and the stop button. */
    private void initializeControlButtons() {
        play = new PlayButton(controller.getPlayButton(), controller, il);
        stop = new StopButton(controller.getStopButton(), controller, il);
        loop = new LoopButton(controller.getLoopButton(), controller, il);
        mute = new MuteButton(controller.getMuteButton(), controller, il);
        muteA = new MuteInstButton(controller.getMuteAButton(), controller, il);
        options = new OptionsButton(controller.getOptionsButton(), controller,
                il);
        add = new AddButton(controller.getAddButton(), controller, il);
        delete = new DeleteButton(controller.getDeleteButton(), controller, il);
        moveUp = new MoveButton(controller.getUpButton(), 1, controller, il);
        moveDown = new MoveButton(controller.getDownButton(), -1, controller,
                il);

        mute.setMuteButton(muteA);
        muteA.setMuteButton(mute);
        mute.setStaff(theStaff);
        muteA.setStaff(theStaff);

        play.link(stop);
        stop.link(play);

        play.setStaff(theStaff);
        stop.setStaff(theStaff);
        options.setStaff(theStaff);

        add.setStaff(theStaff);
        delete.setStaff(theStaff);
        moveUp.setStaff(theStaff);
        moveDown.setStaff(theStaff);

    }

    /**
     * Sets up the slider and arrows that the controls will have.
     */
    private void initializeArrows() {
        leftArrow = new ArrowButton(controller.getLeftArrow(), scrollbar,
                ImageIndex.SCROLLBAR_LEFT1_PRESSED, ImageIndex.SCROLLBAR_LEFT1,
                controller, il);
        rightArrow = new ArrowButton(controller.getRightArrow(), scrollbar,
                ImageIndex.SCROLLBAR_RIGHT1_PRESSED,
                ImageIndex.SCROLLBAR_RIGHT1, controller, il);
        leftFastArrow = new ArrowButton(controller.getLeftFastArrow(),
                scrollbar, ImageIndex.SCROLLBAR_LEFT2_PRESSED,
                ImageIndex.SCROLLBAR_LEFT2, controller, il);
        rightFastArrow = new ArrowButton(controller.getRightFastArrow(),
                scrollbar, ImageIndex.SCROLLBAR_RIGHT2_PRESSED,
                ImageIndex.SCROLLBAR_RIGHT2, controller, il);

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

    /**
     * @param b
     *            Whether we want to turn on arranger mode or not.
     */
    public void setArrangerMode(boolean b) {
        if (b)
            setArrangerMode();
        else
            setEditingMode();
    }

    /** Changes the current interface to the arranger mode. */
    private void setArrangerMode() {
        changeCenterList();
        loop.release();
        StateMachine.setState(ProgramState.ARR_EDITING);

    }

    /**
     * Adds the list of songs characteristic of the arranger mode.
     */
    private void changeCenterList() {
        theStaff.setSequenceName(controller.getNameTextField().getText());
        controller.getNameTextField().setPromptText("Arrangement Name");
        controller.getNameTextField().setText(theStaff.getArrangementName());
        controller.getArrangementList().setVisible(true);
        controller.getDeleteButton().setVisible(true);
        controller.getAddButton().setVisible(true);
        controller.getUpButton().setVisible(true);
        controller.getDownButton().setVisible(true);
    }

    /** Changes the current interface to the normal song editing mode. */
    private void setEditingMode() {
        revertCenterList();
        StateMachine.setState(ProgramState.EDITING);
    }

    /**
     * Reverts the center list into just the middle panel.
     */
    private void revertCenterList() {
        theStaff.setArrangementName(controller.getNameTextField().getText());
        controller.getNameTextField().setPromptText("Song Name");
        controller.getNameTextField().setText(theStaff.getSequenceName());
        controller.getArrangementList().setVisible(false);
        controller.getDeleteButton().setVisible(false);
        controller.getAddButton().setVisible(false);
        controller.getUpButton().setVisible(false);
        controller.getDownButton().setVisible(false);
    }

    /**
     * Loads an arrangement sequence from an input file.
     *
     * @param inputFile
     *            This is the input filename.
     */
    private void loadArrangementFile(File inputFile) {
        try {
            StaffSequence loaded = Utilities.loadSong(inputFile);
            Utilities.normalize(loaded);
            theStaff.setSequence(loaded);
            theStaff.setSequenceFile(inputFile);
            StateMachine.setTempo(loaded.getTempo());
            theStaff.getControlPanel().updateCurrTempo();
            theStaff.getControlPanel()
                    .getScrollbar()
                    .setMax(loaded.getTheLines().size()
                            - Values.NOTELINES_IN_THE_WINDOW);
            theStaff.setLocation(0);
            theStaff.getNoteMatrix().redraw();
            String fname = inputFile.getName();
            try {
                fname = fname.substring(0, fname.indexOf("."));
            } catch (IndexOutOfBoundsException e) {
                // Do nothing
            }
            theStaff.setSequenceName(fname);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
     *
     * @param scroll
     *            The slider that we'll be using.
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                currTempo.setValue(String.valueOf(StateMachine.getTempo()));
            }
        });
    }

    /**
     * Sets the controller class.
     *
     * @param ct
     *            The FXML controller class.
     */
    public void setController(SMPFXController ct) {
        controller = ct;
    }

}
