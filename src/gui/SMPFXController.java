package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import backend.editing.ModifySongManager;
import backend.songs.StaffArrangement;
import backend.songs.StaffNote;
import backend.songs.StaffSequence;
import backend.songs.TimeSignature;
import gui.clipboard.StaffClipboard;
import gui.clipboard.StaffRubberBand;
import gui.components.Controls;
import gui.components.SongNameController;
import gui.components.buttons.ImageRadioButton;
import gui.components.staff.StaffDisplayManager;
import gui.components.staff.StaffMouseEventHandler;
import gui.components.toppanel.ButtonLine;
import gui.components.toppanel.PanelButtons;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;
import gui.loaders.SoundfontLoader;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.converter.NumberStringConverter;

/**
 * The Controller class for most of the program. This will handle the events
 * that happen on the screen.
 *
 * @author RehdBlob
 * @since 2012.08.16
 */
public class SMPFXController {

    /**
     * The image that shows the selected instrument.
     */
    @FXML
    private ImageView selectedInst;

    /**
     * Instrument line.
     */
    @FXML
    private HBox instLine;

    /**
     * The line of buttons that corresponds with the line of images at the top
     * of the frame.
     */
    private ButtonLine instBLine;

    /**
     * The staff that notes, measure lines, and sprites will be placed on.
     */
    private Staff staff;

    /**
     * The button that changes the mode of the staff between song and arranger
     * mode.
     */
    @FXML
    private ImageView modeButton;

    /**
     * The text that displays the mode of the staff.
     */
    @FXML
    private Text modeText;

    /**
     * The controls line that includes the play button, stop button, loop
     * button, etc.
     */
    @FXML
    private HBox controls;

    /** The play button. */
    @FXML
    private ImageView play;

    /** The stop button. */
    @FXML
    private ImageView stop;

    /** The loop button. */
    @FXML
    private ImageView loop;

    /** The mute button. */
    @FXML
    private ImageView mute;

    /** The 'mute-all' button. */
    @FXML
    private ImageView muteA;
    
    /** The clipboard selection button. */
    @FXML
    private ImageView clipboardButton;
    
    @FXML
    private ImageView timesig_4_4;
    
    @FXML
    private ImageView timesig_3_4;
    
    @FXML
    private ImageView timesig_6_8;
    
    @FXML
    private ImageView timesig_custom;

    /** The arranger view piece that holds the list of songs. */
    @FXML
    private ListView<String> arrangementList;

    /** The button that adds a song to the arranger list. */
    @FXML
    private ImageView addButton;

    /** The button that deletes a song from the arranger list. */
    @FXML
    private ImageView deleteButton;

    /** The button that moves a song up on the arranger list. */
    @FXML
    private ImageView upButton;

    /** The button that moves a song down on the arranger list. */
    @FXML
    private ImageView downButton;

    /** This is the plus sign that increases the tempo of the song. */
    @FXML
    private ImageView tempoPlus;

    /** This is the minus sign that decreases the tempo of the song. */
    @FXML
    private ImageView tempoMinus;

    /** This is the text that displays the current tempo of the song. */
    @FXML
    private Text tempoIndicator;

    /** This holds the tempo indicator. */
    @FXML
    private StackPane tempoBox;

    /** The button that loads the song. */
    @FXML
    private ImageView loadButton;

    /** The button that saves the song. */
    @FXML
    private ImageView saveButton;

    /** The button that makes a new song. */
    @FXML
    private ImageView newButton;

    /** The button that opens the options dialog. */
    @FXML
    private ImageView optionsButton;

    /** This is the text area that houses the song name. */
    @FXML
    private TextField songName;

    /**
     * The controls line object that holds the FXML controls object.
     */
    private Controls controlPanel;
    
    @FXML
    private Pane staffFrame;

    /**
     * This holds the volume bars in the program.
     */
    @FXML
    private HBox volumeBars;

    /** The scrollbar that moves the staff. */
    @FXML
    private Slider scrollbar;

    /**
     * The left arrow that you can click to make the staff go to the left.
     */
    @FXML
    private ImageView leftArrow;

    /**
     * The right arrow that you can click to make the staff go to the right.
     */
    @FXML
    private ImageView rightArrow;

    /**
     * The left arrow that you can click to make the staff go to the left
     * quickly.
     */
    @FXML
    private ImageView leftFastArrow;

    /**
     * The left arrow that you can click to make the staff go to the right
     * quickly.
     */
    @FXML
    private ImageView rightFastArrow;
    
    @FXML
    private AnchorPane basePane;
    
    private StaffMouseEventHandler staffMouseEventHandler;
    private StaffRubberBand rubberBand;
    
    private ModifySongManager commandManager;
    
    /** This is the image loader. */
    private ImageLoader il;
    
    /** This is the soundfont loader. */
    private SoundfontLoader sf;

    /** Prevent multiple save windows from opening. */
    private boolean saveInProgress = false;
    
    /**
     * Zero-argument constructor (explicitly declared).
     */
    public SMPFXController() {

    }

    /**
     * Initializes the Controller class for Super Mario Paint
     */
    public void initialize() {
        while (il == null)
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                continue;
            }

        // Set up command manager (undo and redo)
        commandManager = new ModifySongManager(() -> staff.redraw());
        
        basePane.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
            if (event.isControlDown())
                switch (event.getCode()) {
                case Y:
                    commandManager.redo();
                    break;
                case Z:
                    commandManager.undo();
                    break;
                default:
                    break;
                }
        });
        
        // Set up staff.
        StaffDisplayManager displayManager = new StaffDisplayManager(staffFrame, il, volumeBars, commandManager, Values.NOTELINES_IN_THE_WINDOW, Values.NOTES_IN_A_LINE, Values.MAX_STACKABLE_NOTES);
        staff = new Staff(this, displayManager, arrangementList);
        displayManager.initialize();
        controlPanel = new Controls(staff, this, il, arrangementList);
        staff.setControlPanel(controlPanel);
        
        // Set up time signature buttons
        ImageRadioButton timesig_4_4_button = new ImageRadioButton(timesig_4_4, this, il) {
        	{
        		getImages(ImageIndex.TIMESIG_4_4_PRESSED, ImageIndex.TIMESIG_4_4_RELEASED);
        		pressImage();
        	}
        	
        	@Override
        	public void reactPressed(MouseEvent e) {
        		if (isPressed) {
        			return;
        		}
        		
        		super.reactPressed(e);
        		staff.setTimeSignature(TimeSignature.FOUR_FOUR);
        	}
        };
        
        ImageRadioButton timesig_3_4_button = new ImageRadioButton(timesig_3_4, this, il) {
        	{
        		getImages(ImageIndex.TIMESIG_3_4_PRESSED, ImageIndex.TIMESIG_3_4_RELEASED);
        	}
        	
        	@Override
        	public void reactPressed(MouseEvent e) {
        		if (isPressed) {
        			return;
        		}
        		
        		super.reactPressed(e);
        		staff.setTimeSignature(TimeSignature.THREE_FOUR);
        	}
        };
        
        ImageRadioButton timesig_6_8_button = new ImageRadioButton(timesig_6_8, this, il) {
        	{
        		getImages(ImageIndex.TIMESIG_6_8_PRESSED, ImageIndex.TIMESIG_6_8_RELEASED);
        	}
        	
        	@Override
        	public void reactPressed(MouseEvent e) {
        		if (isPressed) {
        			return;
        		}
        		
        		super.reactPressed(e);
        		staff.setTimeSignature(TimeSignature.SIX_EIGHT);
        	}
        };
        
        ImageRadioButton timesig_custom_button = new ImageRadioButton(timesig_custom, this, il) {
        	{
        		getImages(ImageIndex.TIMESIG_CUSTOM_PRESSED, ImageIndex.TIMESIG_CUSTOM_RELEASED);
        	}
        	
        	@Override
        	public void reactPressed(MouseEvent e) {
        		Window owner = ((Node) e.getSource()).getScene().getWindow();
        		String str = Dialog.showTextDialog("Enter time signature:", "4/4, 3/4, 6/8, 6+3, ...", owner);
        		if (str.isEmpty())
        			return;
        		
        		try {
        			staff.setTimeSignature(TimeSignature.valueOf(str));
        			
        		} catch (IllegalArgumentException ee) {
        			Dialog.showDialog(ee.getMessage());
        			return;
        		}
        		
        		super.reactPressed(e); // only if the input timesig is correct
        	}
        };
        
        timesig_4_4_button.link(timesig_3_4_button);
        timesig_4_4_button.link(timesig_6_8_button);
        timesig_4_4_button.link(timesig_custom_button);
        timesig_3_4_button.link(timesig_4_4_button);
        timesig_3_4_button.link(timesig_6_8_button);
        timesig_3_4_button.link(timesig_custom_button);
        timesig_6_8_button.link(timesig_4_4_button);
        timesig_6_8_button.link(timesig_3_4_button);
        timesig_6_8_button.link(timesig_custom_button);
        timesig_custom_button.link(timesig_4_4_button);
        timesig_custom_button.link(timesig_3_4_button);
        timesig_custom_button.link(timesig_6_8_button);
        
        // HACK
        staffMouseEventHandler = new StaffMouseEventHandler(staff, commandManager);
        
        // Set up top line.
        instBLine = new ButtonLine(instLine, selectedInst, il, staff);
        selectedInst.setImage(il.getSpriteFX(ImageIndex.MARIO));

        new PanelButtons(staff, this, il, instBLine);

        arrangementList.setEditable(true);
        arrangementList.setStyle("-fx-font: 8pt \"Arial\";");
        // Hide all arranger features for now.
        arrangementList.setVisible(false);
        addButton.setVisible(false);
        deleteButton.setVisible(false);
        upButton.setVisible(false);
        downButton.setVisible(false);
        
        // Set up clipboard.
        rubberBand = new StaffRubberBand();
        new StaffClipboard(rubberBand, staff, this, il);
        
        StateMachine.clipboardPressedProperty().addListener(obs -> {
            boolean v = StateMachine.isClipboardPressed();
            volumeBars.setMouseTransparent(v);
        });
        
        // Fix TextField focus problems.
        new SongNameController(songName, this);
        
        // Bind playback active property
        StateMachine.getPlaybackActiveProperty().bind(Bindings.createBooleanBinding(
                () -> {
                    switch (StateMachine.getState()) {
                    case SONG_PLAYING:
                    case ARR_PLAYING:
                        return true;
                    default:
                        return false;
                    }
                }, StateMachine.getStateProperty()));
        
        // Bind displayed tempo to internal value in state machine
        tempoIndicator.textProperty().bindBidirectional(StateMachine.getTempoProperty(), new NumberStringConverter());
        
        // Setup scrollbar
        scrollbar.maxProperty().bind(Bindings.createIntegerBinding(
                () -> StateMachine.getMaxLine() - Values.NOTELINES_IN_THE_WINDOW,
                StateMachine.getMaxLineProperty()));
        scrollbar.valueProperty().bindBidirectional(
                StateMachine.getCurrentLineProperty());
        
        scrollbar.disableProperty().bind(StateMachine.getPlaybackActiveProperty());
        
        // Trigger a redraw, editing mode only
        InvalidationListener doRedraw = (obv) -> staff.redraw();
        
        StateMachine.getCurrentLineProperty().addListener(doRedraw);
        StateMachine.getTimeSignatureProperty().addListener(doRedraw);
        
        StateMachine.setMeasureLineNum(0);
        
        // Setup arrangement listview
        StateMachine.getArrangementSongIndexProperty().addListener(obv -> {
            int idx = StateMachine.getArrangementSongIndex();
            arrangementList.getSelectionModel().select(idx);
            if (idx != -1)
                Platform.runLater(() -> arrangementList.scrollTo(idx));
            staff.setSequenceName(arrangementList.getSelectionModel().getSelectedItem());
        });
        
        // Cleanup after a song or arrangement had finished running
        StateMachine.getPlaybackActiveProperty().addListener(obv -> {
            if (!StateMachine.isPlaybackActive()) {
                StateMachine.setArrangementSongIndex(-1);
                displayManager.resetPlayBars();
            }
        });
        
    }
    
    public void switchMode() {
        ProgramState curr = StateMachine.getState();
        if (curr == ProgramState.ARR_PLAYING || curr == ProgramState.SONG_PLAYING)
            return;

        if (curr == ProgramState.EDITING) {
            modeText.setText("Arr");
            staff.setArrangerMode(true);

        } else if (curr == ProgramState.ARR_EDITING) {
            modeText.setText("Song");
            staff.setArrangerMode(false);
        }
    }
    
    public void newSongOrArrangement(Window owner) {
        ProgramState curr = StateMachine.getState();
        if (curr == ProgramState.EDITING)
            newSong(owner);
        else if (curr == ProgramState.ARR_EDITING)
            newArrangement(owner);
    }
    
    public void newSong(Window owner) {
        boolean cont = true;
        if (StateMachine.isSongModified())
            cont = Dialog
                    .showYesNoDialog("The current song has been modified!\n"
                            + "Create a new song anyway?", owner);

        if (cont) {
            staff.setSequence(new StaffSequence());
            staff.setSequenceFile(null);
            staff.setTimeSignature(Values.DEFAULT_TIME_SIGNATURE);
            staff.resetLocation();
            StateMachine.setMaxLine(Values.DEFAULT_LINES_PER_SONG);
            getNameTextField().clear();
            StateMachine.setSongModified(false);
        }
    }
    
    public void newArrangement(Window owner) {
        boolean cont = true;
        if (StateMachine.isArrModified()) {
            cont = Dialog
                    .showYesNoDialog("The current arrangement has been\n"
                            + "modified! Create a new arrangement\nanyway?", owner);
        }
        if (cont) {
            staff.setArrangement(new StaffArrangement());
            staff.setArrangementFile(null);
            getNameTextField().clear();
            staff.getArrangementList().getItems().clear();
            StateMachine.setArrModified(false);
        }
    }
    
    public void save(Window owner) {
        ProgramState curr = StateMachine.getState();
        if (curr == ProgramState.EDITING || curr == ProgramState.SONG_PLAYING) {
            final ProgramState saveState = curr;
            StateMachine.setState(ProgramState.MENU_OPEN);
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    saveSong(owner);
                    StateMachine.setState(saveState);
                }
                
            });
        } else if (!saveInProgress && (curr == ProgramState.ARR_EDITING
                || curr == ProgramState.ARR_PLAYING)) {
            final ProgramState saveState = curr;
            StateMachine.setState(ProgramState.MENU_OPEN);
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    saveArrangement(owner);
                    StateMachine.setState(saveState);
                }
                
            });
        }
    }

    private void saveArrangement(Window owner) {
        String songName = getNameTextField().getText();
        if (!Utilities.legalFileName(songName)) {
            Dialog.showDialog("Illegal file name!\nPlease avoid those characters:\n/, \\, <, >, :, |, *, \", ?, ^", owner);
            return;
        }
        
        try {
            FileChooser f = new FileChooser();
            f.setInitialDirectory(StateMachine.getCurrentDirectory());
            f.setInitialFileName(songName + ".txt");
            f.getExtensionFilters().addAll(
                    new ExtensionFilter("Text file", "*.txt"),
                    new ExtensionFilter("All files", "*"));
            File outputFile = null;
            saveInProgress = true;
            outputFile = f.showSaveDialog(null);
            saveInProgress = false;
            if (outputFile == null)
                return;
            FileOutputStream f_out = new FileOutputStream(outputFile);
            StaffArrangement out = staff.getArrangement();
            out.getTheSequenceNames().clear();
            out.setTheSequenceNames(staff.getArrangementList().getItems());
            if (Settings.SAVE_OBJECTS) {
                saveArrObject(f_out, out);
            } else {
                saveArrTxt(f_out, out);
            }
            f_out.close();
            StateMachine.setCurrentDirectory(new File(outputFile.getParent()));
            staff.setArrangementFile(outputFile);
            StateMachine.setArrModified(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveArrTxt(FileOutputStream f_out, StaffArrangement out) {
        PrintStream pr = new PrintStream(f_out);
        for (String s : out.getTheSequenceNames()) {
            pr.println(s);
        }
        pr.close();
    }

    /**
     * Saves arrangement in object format.
     *
     * @param f_out
     *            FileOutputStream to write in.
     * @param out
     *            The output arrangement file
     * @throws IOException
     * @deprecated
     */
    public void saveArrObject(FileOutputStream f_out, StaffArrangement out)
            throws IOException {
        ObjectOutputStream o_out = new ObjectOutputStream(f_out);
        o_out.writeObject(out);
        o_out.close();
    }

    public void saveSong(Window owner) {
        String songName = getNameTextField().getText();
        if (!Utilities.legalFileName(songName)) {
            Dialog.showDialog("Illegal file name!\nPlease avoid those characters:\n /, \\, <, >, :, |, *, \", ?, ^", owner);
            return;
        }
        
        try {
            FileChooser f = new FileChooser();
            f.setInitialDirectory(StateMachine.getCurrentDirectory());
            f.setInitialFileName(songName + ".txt");
            f.getExtensionFilters().addAll(
                    new ExtensionFilter("Text file", "*.txt"),
                    new ExtensionFilter("All files", "*"));
            File outputFile = null;
            saveInProgress = true;
            outputFile = f.showSaveDialog(null);
            saveInProgress = false;
            if (outputFile == null)
                return;
            FileOutputStream f_out = new FileOutputStream(outputFile);
            StaffSequence out = staff.getSequence();
            out.setTempo(StateMachine.getTempo());
            if (Settings.SAVE_OBJECTS) {
                saveSongObject(f_out, out);
            } else {
                saveSongTxt(f_out, out);
            }
            f_out.close();
            StateMachine.setCurrentDirectory(new File(outputFile.getParent()));
            staff.setSequenceFile(outputFile);
            StateMachine.setSongModified(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves in object file format. Makes decent use of serialization. There are
     * some issues with this because if one changes the staff sequence class,
     * there are going to be issues loading the file.
     *
     * @param f_out
     *            FileOutputStream for saving into a file.
     * @param out
     *            The StaffSequence to write.
     * @throws IOException
     * @deprecated
     */
    public void saveSongObject(FileOutputStream f_out, StaffSequence out)
            throws IOException {
        ObjectOutputStream o_out = new ObjectOutputStream(f_out);
        o_out.writeObject(out);
        o_out.close();
    }

    public void saveSongTxt(FileOutputStream f_out, StaffSequence seq)
            throws IOException {
        PrintStream pr = new PrintStream(f_out);
        TimeSignature t = seq.getTimeSignature();
        if (t == null) {
            t = TimeSignature.FOUR_FOUR;
        }
        pr.printf("TEMPO: %f, EXT: %d, TIME: %s, SOUNDSET: %s\r\n", seq.getTempo(),
                Utilities.longFromBool(seq.getNoteExtensions()), t, seq.getSoundset());
        
        for (int i = 0; i < seq.getLength(); i++) {
            if (seq.getLine(i).isEmpty()) {
                continue;
            }
            pr.print("" + (i / t.top() + 1) + ":" + (i % t.top()) + ",");
            ArrayList<StaffNote> line = seq.getLine(i).getNotes();
            for (int j = 0; j < line.size(); j++) {
                pr.print(line.get(j) + ",");
            }
            pr.printf("VOL: %d\r\n", seq.getLine(i).getVolume());
        }
        pr.close();

        // when we change the soundfont for a song in the arr, we should store
        // the new soundfont in cache
        Task<Void> soundsetsTaskSave = new Task<Void>() {
            @Override
            public Void call() {
                ArrayList<String> seqNames = staff.getArrangement().getTheSequenceNames();
                String currSeqName = getNameTextField().getText();
                for (String seqName : seqNames) 
                    if (seqName.equals(currSeqName)) {
                        getSoundfontLoader().storeInCache();
                        break;
                    }
                return null;
            }
        };
        
        new Thread(soundsetsTaskSave).start();
    }

    public void switchClipMode(boolean on) {
        if (on) {
            StateMachine.setClipboardPressed(false);
            StateMachine.setSelectionModeOn(false);
            volumeBars.setMouseTransparent(false);
            
        } else {
            StateMachine.setClipboardPressed(true);
            StateMachine.setSelectionModeOn(true);
            volumeBars.setMouseTransparent(true);
        }
    }

    /**
     * @return The <code>Slider</code> that we will use to navigate the staff.
     */
    public Slider getScrollbar() {
        return scrollbar;
    }

    /**
     * @return The <code>ImageView</code> that holds the fast left navigation
     *         arrow of the staff.
     */
    public ImageView getLeftFastArrow() {
        return leftFastArrow;
    }

    /**
     * @return The <code>ImageView</code> that holds the right navigation arrow
     *         of the staff.
     */
    public ImageView getRightArrow() {
        return rightArrow;
    }

    /**
     * @return The <code>ImageView</code> that holds the left navigation arrow
     *         of the staff.
     */
    public ImageView getRightFastArrow() {
        return rightFastArrow;
    }

    /**
     * @return The <code>ImageView</code> that holds the fast right navigation
     *         arrow of the staff.
     */
    public ImageView getLeftArrow() {
        return leftArrow;
    }

    /**
     * @return The <code>ImageView</code> object that contains the play button.
     */
    public ImageView getPlayButton() {
        return play;
    }

    /**
     * @return The <code>ImageView</code> object that contains the stop button.
     */
    public ImageView getStopButton() {
        return stop;
    }

    /**
     * @return The <code>ImageView</code> object that contains the loop button.
     */
    public ImageView getLoopButton() {
        return loop;
    }

    /**
     * @return The <code>ImageView</code> object that contains the mute button.
     */
    public ImageView getMuteButton() {
        return mute;
    }

    /**
     * @return The <code>ImageView</code> object that contains the 'mute-all'
     *         button.
     */
    public ImageView getMuteAButton() {
        return muteA;
    }

	/**
	 * @return The <code>ImageView</code> object that contains the clipboard
	 *         selection button.
	 */
    public ImageView getClipboardButton() {
    	return clipboardButton;
    }

    /** @return The control panel of the program. */
    public Controls getControls() {
        return controlPanel;
    }

    /** @return The tempo plus button. */
    public ImageView getTempoPlus() {
        return tempoPlus;
    }

    /** @return The tempo minus button. */
    public ImageView getTempoMinus() {
        return tempoMinus;
    }

    /** @return The tempo indicator text. */
    public Text getTempoIndicator() {
        return tempoIndicator;
    }

    /** @return The tempo indicator box. */
    public StackPane getTempoBox() {
        return tempoBox;
    }

    /** @return The load button. */
    public ImageView getLoadButton() {
        return loadButton;
    }

    /** @return The save button. */
    public ImageView getSaveButton() {
        return saveButton;
    }

    /** @return The new song button. */
    public ImageView getNewButton() {
        return newButton;
    }

    /** @return The options button. */
    public ImageView getOptionsButton() {
        return optionsButton;
    }

    /** @return The text area that contains the song name. */
    public TextField getNameTextField() {
        return songName;
    }

    /** @return The HBox that holds the volume bars. */
    public HBox getVolumeBars() {
        return volumeBars;
    }

    /** @return The instrument button line. */
    public ButtonLine getInstBLine() {
        return instBLine;
    }

    /** @return The mode text. */
    public Text getModeText() {
        return modeText;
    }

    /** @return The mode button image. */
    public ImageView getModeButton() {
        return modeButton;
    }

    /** @return The list of songs in the arrangement. */
    public ListView<String> getArrangementList() {
        return arrangementList;
    }

    /** @return The delete button image. */
    public ImageView getDeleteButton() {
        return deleteButton;
    }

    /** @return The add button image. */
    public ImageView getAddButton() {
        return addButton;
    }

    /** @return The move up button image. */
    public ImageView getUpButton() {
        return upButton;
    }

    /** @return The move down button image. */
    public ImageView getDownButton() {
        return downButton;
    }

    public void setImageLoader(ImageLoader imgLoader) {
        il = imgLoader;
    }

    /** @since v1.1.2 */
	public void setSoundfontLoader(SoundfontLoader sfLoader) {
		sf = sfLoader;
	}
    
    public Staff getStaff(){
    	return staff;
    }
    
    public AnchorPane getBasePane() {
    	return basePane;
    }
    
    public HBox getInstLine() {
    	return instLine;
    }
    
    public StaffMouseEventHandler getStaffMouseEventHandler() {
        return staffMouseEventHandler;
    }
    
    public ModifySongManager getModifySongManager() {
    	return commandManager;
    }
    
    /**
     * @return the program's Soundfont Loader
     * @since v1.1.2
     */
    public SoundfontLoader getSoundfontLoader() {
    	return sf;
    }
}
