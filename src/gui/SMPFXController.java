package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sound.midi.MidiChannel;

import backend.editing.ModifySongManager;
import backend.saving.Decoder;
import backend.songs.Accidental;
import backend.songs.MuteModifier;
import backend.songs.Arrangement;
import backend.songs.Note;
import backend.songs.Song;
import backend.songs.TimeSignature;
import backend.sound.SoundPlayer;
import gui.clipboard.StaffClipboard;
import gui.clipboard.StaffRubberBand;
import gui.components.FileChooserManager;
import gui.components.SongNameController;
import gui.components.buttons.SMPButton;
import gui.components.buttons.SMPHoldButton;
import gui.components.buttons.SMPInstrumentButton;
import gui.components.buttons.SMPRadioButton;
import gui.components.buttons.SMPToggleButton;
import gui.components.staff.StaffDisplayManager;
import gui.components.staff.StaffMouseEventHandler;
import gui.events.KeyboardHandlerMaker;
import gui.loaders.ImageIndex;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.util.StringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.NumberStringConverter;
import lombok.extern.slf4j.Slf4j;
import utilities.MathUtils;

/**
 * The Controller class for most of the program. This will handle the events
 * that happen on the screen.
 *
 * @author RehdBlob
 * @since 2012.08.16
 */
@Slf4j
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
     * The staff that notes, measure lines, and sprites will be placed on.
     */
    private Staff staff;

    @FXML
    private SMPToggleButton modeButton;

    @FXML
    private Text modeText;

    @FXML
    private SMPRadioButton playButton;

    @FXML
    private SMPRadioButton stopButton;

    @FXML
    private SMPToggleButton loopButton;

    @FXML
    private SMPToggleButton muteButton;

    @FXML
    private SMPToggleButton muteInstButton;
    
    @FXML
    private SMPToggleButton clipboardButton;
    
    @FXML
    private SMPRadioButton timesigButton_4_4;
    
    @FXML
    private SMPRadioButton timesigButton_3_4;
    
    @FXML
    private SMPRadioButton timesigButton_6_8;
    
    @FXML
    private SMPRadioButton timesigButtonCustom;
    
    @FXML
    private SMPButton saveButton;
    
    @FXML
    private SMPButton loadButton;
    
    @FXML
    private SMPButton newButton;
    
    @FXML
    private SMPButton optionsButton;
    
    @FXML
    private SMPHoldButton tempoPlusButton;
    
    @FXML
    private SMPHoldButton tempoMinusButton;
    
    @FXML
    private SMPButton addButton;
    
    @FXML
    private SMPButton deleteButton;
    
    @FXML
    private SMPButton upButton;
    
    @FXML
    private SMPButton downButton;

    @FXML
    private Parent arrangerView;
    
    @FXML
    private ListView<Song> arrangementList;

    /** This is the text that displays the current tempo of the song. */
    @FXML
    private Text tempoIndicator;

    /** This holds the tempo indicator. */
    @FXML
    private StackPane tempoBox;

    /** This is the text area that houses the song name. */
    @FXML
    private TextField songName;
    
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
    
    @FXML
    private AnchorPane basePane;
    
    private StaffMouseEventHandler staffMouseEventHandler;
    private StaffRubberBand rubberBand;
    
    private ModifySongManager commandManager;
    
    private Map<ImageIndex, Image> imagesHolder;
    private SoundPlayer soundPlayer;
    
    /** Handles the options menu */
    private OptionsMenu optionsMenu;
    
    /**
     * Zero-argument constructor (explicitly declared).
     */
    public SMPFXController() {
        // Intentionally left empty.
    }

    /**
     * Initializes the Controller class for Super Mario Paint
     */
    public void initialize() {
        // Set up command manager (undo and redo)
        commandManager = new ModifySongManager(() -> staff.redraw());
        
        basePane.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown())
                StateMachine.setCtrlPressed(true);
            if (event.isShiftDown())
                StateMachine.setShiftPressed(true);
            
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
        
        basePane.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (!event.isControlDown())
                StateMachine.setCtrlPressed(false);
            if (!event.isShiftDown())
                StateMachine.setShiftPressed(false);
        });
        
        // Set up staff.
        StaffDisplayManager displayManager = new StaffDisplayManager(staffFrame, imagesHolder, volumeBars, commandManager, Values.NOTELINES_IN_THE_WINDOW, Values.NOTES_IN_A_LINE, Values.MAX_STACKABLE_NOTES);
        staff = new Staff(displayManager, soundPlayer);
        displayManager.initialize();
        
        KeyboardHandlerMaker.of(this).initializeIn(basePane);
        
        // We leverage the StringProperty modeText to bind the properties of the button and the mode in both direction
        // Bidirectional bindings between different types can only be done if one type is String afaik
        Bindings.bindBidirectional(modeText.textProperty(), modeButton.selectedProperty(), new BooleanStringConverter () {
            @Override public String toString(Boolean b) { return b ? "Arr" : "Song"; }
            @Override public Boolean fromString(String string) { return string.equals("Arr"); }
        });
        
        Bindings.bindBidirectional(modeText.textProperty(), StateMachine.modeProperty(), new StringConverter<SMPMode>() {
            @Override public String toString(SMPMode mode) { return mode.equals(SMPMode.ARRANGEMENT) ? "Arr" : "Song"; }
            @Override public SMPMode fromString(String string) { return string.equals("Arr") ? SMPMode.ARRANGEMENT : SMPMode.SONG; }
        });
        
        loopButton.selectedProperty().bindBidirectional(StateMachine.loopPressedProperty());
        muteButton.selectedProperty().bindBidirectional(StateMachine.mutePressedProperty());
        muteInstButton.selectedProperty().bindBidirectional(StateMachine.muteAPressedProperty());
        clipboardButton.selectedProperty().bindBidirectional(StateMachine.clipboardPressedProperty());
        
        ToggleGroup mainRadioToggleGroup = new ToggleGroup();
        Utilities.groupToggleBtns(mainRadioToggleGroup,
        		stopButton, playButton);
        
        ToggleGroup muteToggleGroup = new ToggleGroup();
        Utilities.groupToggleBtns(muteToggleGroup,
        		muteButton, muteInstButton);
        
        ToggleGroup timesigToggleGroup = new ToggleGroup();
        Utilities.groupToggleBtns(timesigToggleGroup,
        		timesigButton_4_4, timesigButton_3_4, timesigButton_6_8, timesigButtonCustom);
        
        stopButton.setSelected(true);
        timesigButton_4_4.setSelected(true);
        
        String[] tooltipLines = {
            "Click (or Shift+R) to toggle region selection",
            "Hover over instrument & press F to filter instrument",
            "Ctrl+A to select all",
            "Ctrl+C to copy notes",
            "Ctrl+V to paste notes",
            "Ctrl+X to cut notes",
            "Delete to delete notes",
            "Alt+N to toggle notes selection",
            "Alt+V to toggle volumes selection"
        };
        
        Tooltip.install(clipboardButton, new Tooltip(String.join("\n", tooltipLines)));
        
        StateMachine.clipboardPressedProperty().addListener(obs -> {
            if (StateMachine.isClipboardPressed())
                displayManager.resetSilhouette();
        });

        // Set up arranger view
        arrangerView.visibleProperty().bind(Bindings.createBooleanBinding(() -> {
            switch (StateMachine.getMode()) {
            case SONG:
                return false;
            case ARRANGEMENT:
                return true;
            default:
                return false;
            }
        }, StateMachine.modeProperty()));
        
        arrangementList.getSelectionModel().selectedItemProperty().addListener(obs -> {
            if (StateMachine.isPlaybackActive())
                return;
            
            int songIndex = arrangementList.getSelectionModel().getSelectedIndex();
            if (songIndex == -1)
            	return;
            
            List<Song> seq = staff.getArrangement().getSequences();
            Window owner = arrangementList.getScene().getWindow();
            
            if (!confirmOperation(owner, "Load anyway?", true, false))
            	return;
            
            staff.populateStaff(seq.get(songIndex));
            seq.set(songIndex, staff.getSequence());
        });
        
        arrangementList.setCellFactory(list -> new ListCell<>() {
        	@Override
        	public void updateItem(Song song, boolean empty) {
        		super.updateItem(song, empty);
        		
        		setGraphic(null);
        		setText(empty || song == null ? null : song.getTitle());
        	}
        });

        // Set up options menu
        optionsMenu = new OptionsMenu(this, staff);
        
        // HACK
        staffMouseEventHandler = new StaffMouseEventHandler(staff, commandManager);
        
        // Set up top line.
        populateInstrumentButtons(instLine);
        
        selectedInst.imageProperty().bind(Bindings.createObjectBinding(() -> {
            InstrumentIndex i = StateMachine.getSelectedInstrument();
            return imagesHolder.get(ImageIndex.valueOf(i.toString()));
        }, StateMachine.selectedInstrumentProperty()));
        
        // Set up clipboard.
        rubberBand = new StaffRubberBand();
        new StaffClipboard(rubberBand, staff, this);
        
        volumeBars.mouseTransparentProperty().bind(StateMachine.clipboardPressedProperty());
        
        // Fix TextField focus problems.
        new SongNameController(songName, this);
        songName.promptTextProperty().bind(Bindings.createStringBinding(() -> {
            switch (StateMachine.getMode() ) {
            case SONG:
                return "Song Name:";
            case ARRANGEMENT:
                return "Arrangement Name:";
            default:
                return "";
            }
        }, StateMachine.modeProperty()));
        
        // Changing mode binds the bottom text to a different name property
        StateMachine.modeProperty().addListener(obs -> {
            switch (StateMachine.getMode()) {
            case SONG:
                songName.textProperty().unbindBidirectional(StateMachine.currentArrangementNameProperty());
                songName.textProperty().bindBidirectional(StateMachine.currentSongNameProperty());
                break;
            case ARRANGEMENT:
                songName.textProperty().unbindBidirectional(StateMachine.currentSongNameProperty());
                songName.textProperty().bindBidirectional(StateMachine.currentArrangementNameProperty());
                break;
            }
        });
        
        songName.textProperty().bindBidirectional(StateMachine.currentSongNameProperty());
        
        StateMachine.currentSongNameProperty().addListener(obs -> staff.getSequence().setTitle(StateMachine.getCurrentSongName()));
        StateMachine.currentArrangementNameProperty().addListener(obs -> staff.getArrangement().setTitle(StateMachine.getCurrentArrangementName()));
        
        // Set up tempo box
        tempoIndicator.textProperty().bindBidirectional(StateMachine.getTempoProperty(), new NumberStringConverter());

        tempoBox.setOnMousePressed(evt -> {
            try {
                if (!StateMachine.isPlaybackActive() && StateMachine.getMode() == SMPMode.SONG) {
                    Window owner = Utilities.getOwner(evt);
                    String tempo = Dialog.showTextDialog("Tempo", owner);
                    StateMachine.setTempo(Double.parseDouble(tempo));
                    tempo = tempo.trim();
                }
            } catch (NumberFormatException e) {
                // Do nothing.
            }
            evt.consume();
        });
        
        // Setup scrollbar
        scrollbar.maxProperty().bind(Bindings.createIntegerBinding(
                () -> Math.max(StateMachine.getMaxLine() - Values.NOTELINES_IN_THE_WINDOW, 0),
                StateMachine.getMaxLineProperty()));
        scrollbar.valueProperty().bindBidirectional(
                StateMachine.getCurrentLineProperty());
        
        scrollbar.disableProperty().bind(StateMachine.getPlaybackActiveProperty());
        
        // If the scrollbar is disabled, we give focus elsewhere
    	// to be able to handle key events (hitting space should stop playback)
        StateMachine.getPlaybackActiveProperty().addListener(obs -> Platform.runLater(
        		(StateMachine.isPlaybackActive() ? basePane : scrollbar)::requestFocus));
        
        // Trigger a redraw, editing mode only
        InvalidationListener doRedraw = obv -> staff.redraw();
        
        StateMachine.getCurrentLineProperty().addListener(doRedraw);
        StateMachine.getTimeSignatureProperty().addListener(doRedraw);
        
        StateMachine.setMeasureLineNum(0);
        
        // Setup arrangement listview
        StateMachine.getArrangementSongIndexProperty().addListener(obv -> {
            int idx = StateMachine.getArrangementSongIndex();
            arrangementList.getSelectionModel().select(idx);
            if (idx != -1)
                Platform.runLater(() -> arrangementList.scrollTo(idx));
            StateMachine.setCurrentSongName(arrangementList.getSelectionModel().getSelectedItem().getTitle());
        });
        
        // Cleanup after a song or arrangement had finished running
        StateMachine.getPlaybackActiveProperty().addListener(obv -> {
            if (!StateMachine.isPlaybackActive()) {
                StateMachine.setArrangementSongIndex(-1);
                stopButton.setSelected(true);
                displayManager.resetPlayBars();
            }
        });
        
        // Disable buttons while playback is active
        Node[] btns = {
        		timesigButton_4_4, timesigButton_3_4, timesigButton_6_8, timesigButtonCustom,
        		modeButton, saveButton, loadButton, newButton, optionsButton,
        		tempoPlusButton, tempoMinusButton, addButton, deleteButton, upButton, downButton
        };
        
        Arrays.asList(btns).forEach(btn -> btn.disableProperty().bind(StateMachine.getPlaybackActiveProperty()));
    }
    
    private void onInstrumentButtonAction(InstrumentIndex inst) {
    	if (StateMachine.isShiftPressed()) {
            boolean ex = StateMachine.getNoteExtension(inst.ordinal());
            StateMachine.setNoteExtension(inst.ordinal(), !ex);
            
            @SuppressWarnings("java:S3358")
            int i = (inst.ordinal() == 15) ? 16 : (inst.ordinal() == 16) ? 15 : inst.ordinal();
            
            staff.getSequence().getNoteExtensions()[i] = !ex;
            
        } else if (StateMachine.isCtrlPressed()) {
            int flt = StateMachine.getFilteredNotes();
            int newFlt;
            int mask = ~ ((-1) << InstrumentIndex.values().length);
            
            // we go through bitwise computations to only set the property once
            if ((flt & mask) == mask) {
                newFlt = 1 << inst.ordinal();
                
            } else {
                newFlt = flt ^ (1 << inst.ordinal());
                
                if ((newFlt & mask) == 0) {
                    newFlt = -1;
                }
            }
            
            StateMachine.setFilteredNotes(newFlt);
            
        } else {
            MidiChannel[] chan = soundPlayer.getChannels();
            if (chan[inst.getChannel() - 1] != null) {
                chan[inst.getChannel() - 1].noteOn(Values.DEFAULT_NOTE, Values.DEFAULT_VELOCITY);
            }
            
            StateMachine.setSelectedInstrument(inst);
        }
    }
    
    private void populateInstrumentButtons(Pane n) {
        SMPInstrumentButton[] vs = new SMPInstrumentButton[InstrumentIndex.values().length];
        n.getChildren().clear();
        
        for (InstrumentIndex inst : InstrumentIndex.values()) {
            SMPInstrumentButton b = new SMPInstrumentButton(inst.name(), imagesHolder.get(inst.smImageIndex()), imagesHolder.get(inst.smaImageIndex()));
            b.setImageFiltered(imagesHolder.get(ImageIndex.FILTER));
            b.setFitHeight(28);
            b.setFitWidth(26);
            b.setFocusTraversable(false);
            
            b.setOnAction(e -> onInstrumentButtonAction(inst));
            
            vs[inst.ordinal()] = b;
            n.getChildren().add(b);
        }
        
        StateMachine.noteExtensionsProperty().addListener(obs -> {
            for (InstrumentIndex inst : InstrumentIndex.values()) {
                vs[inst.ordinal()].setSustainOn(StateMachine.getNoteExtension(inst.ordinal()));
            }
        });
        
        StateMachine.filteredNotesProperty().addListener((obs, oldv, newv) -> {
            int diff = (int) oldv ^ (int) newv;
            for (InstrumentIndex inst : InstrumentIndex.values()) {
                if ((diff & 1) == 1) {
                    boolean ex = StateMachine.getFilteredNote(inst.ordinal());
                    vs[inst.ordinal()].setActive(ex);
                }
                diff >>= 1;
            }
        });
    }
    
    public boolean confirmOperation(Window owner, String q, boolean checkForSong, boolean checkForArr) {
    	boolean songModified = checkForSong && StateMachine.isSongModified();
    	boolean arrModified = checkForArr && StateMachine.isArrModified();
    	
    	String whatWasModified = "";
    	if (songModified && arrModified) {
    		whatWasModified = "The current song and arrangement\nhave both been modified!";
    	} else if (songModified) {
    		whatWasModified = "The current song has been modified!";
    	} else if (arrModified) {
    		whatWasModified = "The current arrangement has been modified!";
    	}
    	
    	boolean somethingWasModified = songModified || arrModified;
    	if (somethingWasModified) {
    		return Dialog.showYesNoDialog("HOLD IT!", String.format("%s%n%s", whatWasModified, q), owner);
    	}
    	
    	return true;
    }
    
    public void play(ActionEvent e) {
        staff.play();
    }
    
    public void stop(ActionEvent e) {
        staff.stop();
    }
    
    public void setTimesig_4_4(ActionEvent e) {
        staff.setTimeSignature(TimeSignature.FOUR_FOUR);
    }
    
    public void setTimesig_3_4(ActionEvent e) {
        staff.setTimeSignature(TimeSignature.THREE_FOUR);
    }
    
    public void setTimesig_6_8(ActionEvent e) {
        staff.setTimeSignature(TimeSignature.SIX_EIGHT);
    }
    
    public void setTimesig_custom(ActionEvent e) {
        Window owner = ((Node) e.getSource()).getScene().getWindow();
        String str = Dialog.showTextDialog(null, "Enter time signature:", "4/4, 3/4, 6/8, 6+3, ...", owner, true);
        if (str.isEmpty())
            return;
        
        try {
            TimeSignature t = TimeSignature.valueOf(str);
            staff.setTimeSignature(t);
            
            if (t.equals(TimeSignature.FOUR_FOUR)) {
                timesigButton_4_4.setSelected(true);
            } else if (t.equals(TimeSignature.THREE_FOUR)) {
                timesigButton_3_4.setSelected(true);
            } else if (t.equals(TimeSignature.SIX_EIGHT)) {
                timesigButton_6_8.setSelected(true);
            }
            
        } catch (IllegalArgumentException ee) {
            Dialog.showDialog(ee.getMessage());
        }
    }
    
    @FXML
    public void tempoUp(ActionEvent e) {
        StateMachine.setTempo(StateMachine.getTempo() + 1);
    }
    
    @FXML
    public void tempoDown(ActionEvent e) {
        StateMachine.setTempo(StateMachine.getTempo() - 1);
    }
    
    public void switchMode() {
        if (StateMachine.isPlaybackActive())
            return;

        switch (StateMachine.getMode()) {
        case SONG:
            StateMachine.setMode(SMPMode.ARRANGEMENT);
            break;

        case ARRANGEMENT:
            StateMachine.setMode(SMPMode.SONG);
            break;
        }
    }
    
    @FXML
    public void moveLeft(ActionEvent e) {
        staff.bumpStaff(-1);
    }
    
    @FXML
    public void moveLeftEdge(ActionEvent e) {
        staff.bumpStaff(Integer.MIN_VALUE);
    }
    
    @FXML
    public void moveRight(ActionEvent e) {
        staff.bumpStaff(1);
    }
    
    @FXML
    public void moveRightEdge(ActionEvent e) {
        staff.bumpStaff(Integer.MAX_VALUE);
    }
    
    @FXML
    public void addSongtoArrangement(ActionEvent e) {
        if (staff.addSongToArrangement()) {
            arrangementList.getItems().add(staff.getSequence());
            arrangementList.scrollTo(arrangementList.getItems().size() - 1);
        }
    }
    
    @FXML
    public void deleteSongFromArrangement(ActionEvent e) {
        int i = arrangementList.getSelectionModel().getSelectedIndex();
        
        if (staff.deleteSongFromArrangement(i)) {
        	arrangementList.getItems().remove(i);
        }
    }
    
    @FXML
    public void moveSongUpInArrangement(ActionEvent e) {
    	moveSongInArrangement(e, -1);
    }

    @FXML
    public void moveSongDownInArrangement(ActionEvent e) {
    	moveSongInArrangement(e, 1);
    }
    
    private void moveSongInArrangement(ActionEvent e, int diff) {
        ObservableList<Song> l = arrangementList.getItems();
        int i = arrangementList.getSelectionModel().getSelectedIndex();
        int moveTo = MathUtils.clamp(i + diff, 0, l.size());
        
        if (staff.moveSongInArrangement(i, moveTo)) {
            Song s = l.remove(i);
            l.add(moveTo, s);
            arrangementList.getSelectionModel().select(moveTo);
            arrangementList.scrollTo(moveTo);
        }
    }
    
    @FXML
    public void newSongOrArrangement(ActionEvent e) {
        newSongOrArrangement(Utilities.getOwner(e));
    }
    
    public void newSongOrArrangement(Window owner) {
        switch (StateMachine.getMode()) {
        case SONG:
            newSong(owner);
            break;
            
        case ARRANGEMENT:
            newArrangement(owner);
            break;
        }
    }
    
    public void newSong(Window owner) {
        if (confirmOperation(owner, "Create a new song anyway?", true, false)) {
            staff.setSequence(new Song());
            staff.setTimeSignature(Values.DEFAULT_TIME_SIGNATURE);
            staff.resetLocation();
            StateMachine.setMaxLine(Values.DEFAULT_LINES_PER_SONG);
            getNameTextField().clear();
            StateMachine.setSongModified(false);
        }
    }
    
    public void newArrangement(Window owner) {
        if (confirmOperation(owner, "Create a new arrangement anyway?", false, true)) {
            staff.setArrangement(new Arrangement());
            getNameTextField().clear();
            arrangementList.getItems().clear();
            StateMachine.setArrModified(false);
        }
    }
    
    @FXML
    public void save(ActionEvent e) {
        save(Utilities.getOwner(e));
    }
    
    public void save(Window owner) {
        switch (StateMachine.getMode()) {
        case SONG:
            Platform.runLater(() -> saveSong(owner));
            break;
            
        case ARRANGEMENT:
            Platform.runLater(() -> saveArrangement(owner));
            break;
        }
    }

    private void saveArrangement(Window owner) {
        String chosenSongName = getNameTextField().getText();
        if (!Utilities.legalFileName(chosenSongName)) {
            Dialog.showDialog(null, "Illegal file name!\nPlease avoid those characters:\n/, \\, <, >, :, |, *, \", ?, ^", owner);
            return;
        }
        
        try {
        	File outputFile = FileChooserManager.saveAs(owner, chosenSongName);
            if (outputFile == null)
                return;
            FileOutputStream fOut = new FileOutputStream(outputFile);
            Arrangement out = staff.getArrangement();
            
            for (int i = 0; i < out.getSequences().size(); i++) {
            	String name = arrangementList.getItems().get(i).getTitle();
            	out.getSequences().get(i).setTitle(name);
            }
            
            saveArrTxt(fOut, out);
            fOut.close();
            StateMachine.setCurrentDirectory(new File(outputFile.getParent()));
            StateMachine.setArrModified(false);
        } catch (IOException e) {
            log.error("Error in saveArrangement:", e);
        }
    }

    public void saveArrTxt(FileOutputStream fOut, Arrangement out) {
        PrintStream pr = new PrintStream(fOut);
        for (Song seq : out.getSequences()) {
            pr.println(seq.getTitle());
        }
        pr.close();
    }

    public void saveSong(Window owner) {
        String chosenSongName = getNameTextField().getText();
        if (!Utilities.legalFileName(chosenSongName)) {
            Dialog.showDialog(null, "Illegal file name!\nPlease avoid those characters:\n /, \\, <, >, :, |, *, \", ?, ^", owner);
            return;
        }
        
        try {
        	File outputFile = FileChooserManager.saveAs(owner, chosenSongName);
            if (outputFile == null)
                return;
            FileOutputStream fOut = new FileOutputStream(outputFile);
            Song out = staff.getSequence();
            out.setTempo(StateMachine.getTempo());
            saveSongTxt(fOut, out);
            fOut.close();
            StateMachine.setCurrentDirectory(new File(outputFile.getParent()));
            StateMachine.setSongModified(false);
        } catch (IOException e) {
            log.error("Error in saveSong:", e);
        }
    }

    public void saveSongTxt(FileOutputStream fOut, Song seq)
            throws IOException {
        PrintStream pr = new PrintStream(fOut);
        TimeSignature t = seq.getTimeSignature();
        if (t == null) {
            t = TimeSignature.FOUR_FOUR;
        }
        pr.printf("TEMPO: %f, EXT: %d, TIME: %s, SOUNDSET: %s\r\n", seq.getTempo(),
                Utilities.longFromBool(seq.getNoteExtensions()), t, seq.getSoundset());
        
        for (int i = 0; i < seq.getLength(); i++) {
            if (seq.getLine(i).getNotes().isEmpty()) {
                continue;
            }
            pr.print("" + (i / t.top() + 1) + ":" + (i % t.top()) + ",");
            List<Note> line = seq.getLine(i).getNotes();
            for (int j = 0; j < line.size(); j++) {
                pr.print(noteToString(line.get(j)) + ",");
            }
            pr.printf("VOL: %d\r\n", seq.getLine(i).getVolume());
        }
        pr.close();

        // when we change the soundfont for a song in the arr, we should store
        // the new soundfont in cache
        Task<Void> soundsetsTaskSave = new Task<Void>() {
            @Override
            public Void call() {
                List<Song> seqs = staff.getArrangement().getSequences();
                String currSeqName = getNameTextField().getText();
                for (Song seq : seqs) 
                    if (seq.getTitle().equals(currSeqName)) {
                        soundPlayer.storeInCache();
                        break;
                    }
                return null;
            }
        };
        
        new Thread(soundsetsTaskSave).start();
    }
    
    private static String noteToString(Note note) {
        String instName = note.getInstrument().toString();
        String noteName = Values.STAFF_NOTE_NAMES[note.getVerticalPosition()];
        String noteAcc = accidentalToString(note.getAccidental());
        String muteName = muteModifierToString(note.getMuteModifier());
        return instName + " " + noteName + noteAcc + muteName;
    }
    
    private static String accidentalToString(Accidental acc) {
        switch (acc) {
        case Accidental.DOUBLE_FLAT:
            return "B";
        case Accidental.FLAT:
            return "b";
        case Accidental.NATURAL:
            return "";
        case Accidental.SHARP:
            return "#";
        case Accidental.DOUBLE_SHARP:
            return "X";
        default:
            throw new IllegalArgumentException("Cannot convert " + acc + " to String");
        }
    }
    
    private static String muteModifierToString(MuteModifier mod) {
        switch (mod) {
        case MuteModifier.REGULAR:
            return "";
        case MuteModifier.MUTE_THIS_PITCH:
            return "m1";
        case MuteModifier.MUTE_THIS_INST:
            return "m2";
        default:
            throw new IllegalArgumentException("Cannot convert " + mod + " to String");
        }
    }
    
    @FXML
    public void load(ActionEvent e) {
        load(Utilities.getOwner(e));
    }

    public void load(Window owner) {
        switch (StateMachine.getMode()) {
        case SONG:
            Platform.runLater(() -> loadSong(owner));
            break;
            
        case ARRANGEMENT:
            Platform.runLater(() -> loadArrangement(owner));
            break;
        }
    }

    private void loadSong(Window owner) {
    	if (!confirmOperation(owner, "Load anyway?", true, false))
            return;
    	
        try {
        	File inputFile = FileChooserManager.open(null);
        	if (inputFile == null)
        		return;
        	StateMachine.setCurrentDirectory(new File(inputFile.getParent()));
        	loadSong(inputFile, owner);
        	
        } catch (Exception e) {
        	Dialog.showDialog(null, "Not a valid song file.", owner);
        }
    }

    private void loadSong(File inputFile, Window owner) {
        try {
            Song loaded = Decoder.SEQUENCE_DECODER.decode(inputFile).orElseThrow(IOException::new);
            staff.populateStaff(loaded);
            getModifySongManager().reset();
            getNameTextField().setText(loaded.getTitle());
            StateMachine.setNoteExtensions(loaded.getNoteExtensions());
            StateMachine.setSongModified(false);
            
        } catch (FileNotFoundException e) {
            Dialog.showDialog("Error!", "File " + inputFile + "not found!", owner);
            log.error("File not found error in loadSong:", e);
            
        } catch (IOException e) {
            Dialog.showDialog("Error!", "An IO exception occurred while reading file " + inputFile + "!", owner);
            log.error("IO error in loadSong:", e);
            
        } catch (Exception e) {
            Dialog.showDialog("Error!", "An error occurred while reading file " + inputFile + "!", owner);
            log.error("Error in loadSong:", e);
        }
    }

    private void loadArrangement(Window owner) {
    	if (!confirmOperation(owner, "Load anyway?", true, true))
    		return;
    	
        File inputFile = null;

        try {
        	inputFile = FileChooserManager.open(null);
        	if (inputFile == null)
        		return;
        	StateMachine.setCurrentDirectory(new File(inputFile.getParent()));
        	Arrangement loaded = Decoder.SMP_ARRANGEMENT_DECODER.decode(inputFile);
        	staff.populateStaffArrangement(loaded, owner);
            
            arrangementList.getItems().clear();
            for (Song seq : loaded.getSequences()) {
            	arrangementList.getItems().add(seq);
            }
            
        	StateMachine.setSongModified(false);
        	StateMachine.setArrModified(false);
        	
        } catch (ParseException | StreamCorruptedException
        		| NullPointerException e) {
        	try {
        		Arrangement loaded = Decoder.MPC_ARRANGEMENT_DECODER.decode(inputFile);
        		StateMachine.setCurrentDirectory(new File(inputFile.getParent()));
        		staff.populateStaffArrangement(loaded, owner);
        		StateMachine.setSongModified(false);
        		
        	} catch (Exception e1) {
        	    log.error("Error in loadArrangement:", e1);
        		Dialog.showDialog(null, "Not a valid arrangement file.", owner);
        	}
        	
        } catch (IOException e) {
            log.error("Error during loadArrangement:", e);
        }
    }
    
    @FXML
    public void options(ActionEvent e) {
        options(Utilities.getOwner(e));
    }
    
    public void options(Window owner) {
        optionsMenu.options(owner);
    }
    
    public Staff getStaff() {
    	return staff;
    }
    
    public SMPRadioButton getStopButton() {
    	return stopButton;
    }
    
    public SMPRadioButton getPlayButton() {
    	return playButton;
    }
    
    /**
     * @return The <code>Slider</code> that we will use to navigate the staff.
     */
    public Slider getScrollbar() {
        return scrollbar;
    }

    /** @return The text area that contains the song name. */
    public TextField getNameTextField() {
        return songName;
    }

    /** @return The HBox that holds the volume bars. */
    public HBox getVolumeBars() {
        return volumeBars;
    }

    public void setImagesHolder(Map<ImageIndex, Image> imagesHolder) {
        this.imagesHolder = imagesHolder;
    }

    /** @since v1.1.2 */
    public void setSoundPlayer(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }
    
    public AnchorPane getBasePane() {
        return basePane;
    }
    
    public StaffMouseEventHandler getStaffMouseEventHandler() {
        return staffMouseEventHandler;
    }
    
    public ModifySongManager getModifySongManager() {
        return commandManager;
    }
}
