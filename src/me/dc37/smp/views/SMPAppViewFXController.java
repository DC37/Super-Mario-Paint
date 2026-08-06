package me.dc37.smp.views;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import javax.sound.midi.MidiChannel;

import backend.BackendUtils;
import backend.editing.ModifySongManager;
import backend.saving.ArrangementDecoders;
import backend.saving.SequenceDecoders;
import backend.songs.Arrangement;
import backend.songs.Note;
import backend.songs.Song;
import backend.songs.TimeSignature;
import gui.Dialog;
import gui.OptionsMenu;
import gui.SMPInstrument;
import gui.SMPMode;
import gui.Staff;
import gui.StateMachine;
import gui.Utilities;
import gui.Values;
import gui.clipboard.StaffClipboard;
import gui.clipboard.StaffRubberBand;
import gui.components.FileChooserManager;
import gui.components.ModeTypeStringConverter;
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
import javafx.beans.Observable;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;
import lombok.extern.slf4j.Slf4j;
import me.dc37.smp.controllers.SMPAppController;
import me.dc37.smp.models.ResourceModel;
import me.dc37.smp.models.SMPAppModel;
import utilities.MathUtils;

/**
 * The Controller class for most of the program. This will handle the events
 * that happen on the screen.
 *
 * @author RehdBlob
 * @since 2012.08.16
 */
@Slf4j
public class SMPAppViewFXController {
    
	private static final String PROMPT_LOAD_CONFIRM = "Load anyway?";
	private static final String PROMPT_ERROR = "Error!";
	
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
    private SMPRadioButton timeSigBtnFourFour;
    
    @FXML
    private SMPRadioButton timeSigBtnThreeFour;
    
    @FXML
    private SMPRadioButton timeSigBtnSixEight;
    
    @FXML
    private SMPRadioButton timeSigBtnCustom;
    
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
    
    /** Handles the options menu */
    private OptionsMenu optionsMenu;
    
    private ResourceModel resModel;
    private SMPAppModel model;
    
    /**
     * Constructs a FXML Controller for the Main Window.
     * 
     * @param controller A {@link SMPAppController} that
     *                   represents the operations available
     *                   to the main window (MVCI).
     */
    public SMPAppViewFXController(ResourceModel resModel, SMPAppModel model) {
        this.resModel = resModel;
        this.model = model;
    }

    /**
     * Initializes the Controller class for Super Mario Paint
     */
    public void initialize() {
        // Set up command manager (undo and redo)
        commandManager = new ModifySongManager(() -> staff.redraw());
        
        basePane.addEventHandler(KeyEvent.KEY_PRESSED, this::manageShiftCtrlPresses);
        basePane.addEventHandler(KeyEvent.KEY_RELEASED, this::manageShiftCtrlPresses);
        
        // Set up staff.
        StaffDisplayManager displayManager = new StaffDisplayManager(
        		staffFrame, resModel.getIcons(), volumeBars, commandManager,
        		Values.NOTELINES_IN_THE_WINDOW, Values.NOTES_IN_A_LINE, Values.MAX_STACKABLE_NOTES);
        
        staff = new Staff(displayManager, resModel.getSoundPlayer(), model);
        displayManager.initialize();
        
        KeyboardHandlerMaker.of(this, model).initializeIn(basePane);
        
        // We leverage the StringProperty modeText to bind the properties of the button and the mode in both direction
        // Bidirectional bindings between different types can only be done if one type is String afaik
        Bindings.bindBidirectional(modeText.textProperty(), modeButton.selectedProperty(),
        		new ModeTypeStringConverter<>(b -> b != null && b.booleanValue(), isArr -> isArr));
        
        Bindings.bindBidirectional(modeText.textProperty(), model.getModeProperty(),
        		new ModeTypeStringConverter<>(
        				mode -> mode.equals(SMPMode.ARRANGEMENT),
        				isArr -> (isArr != null && isArr.booleanValue()) ? SMPMode.ARRANGEMENT : SMPMode.SONG));
        
        loopButton.selectedProperty().bindBidirectional(model.getLoopPressedProperty());
        muteButton.selectedProperty().bindBidirectional(model.getMutePressedProperty());
        muteInstButton.selectedProperty().bindBidirectional(model.getMuteAPressedProperty());
        clipboardButton.selectedProperty().bindBidirectional(model.getClipboardPressedProperty());
        
        ToggleGroup mainRadioToggleGroup = new ToggleGroup();
        Utilities.groupToggleBtns(mainRadioToggleGroup,
        		stopButton, playButton);
        
        ToggleGroup muteToggleGroup = new ToggleGroup();
        Utilities.groupToggleBtns(muteToggleGroup,
        		muteButton, muteInstButton);
        
        ToggleGroup timesigToggleGroup = new ToggleGroup();
        Utilities.groupToggleBtns(timesigToggleGroup,
        		timeSigBtnFourFour, timeSigBtnThreeFour, timeSigBtnSixEight, timeSigBtnCustom);
        
        stopButton.setSelected(true);
        timeSigBtnFourFour.setSelected(true);
        
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
        
        model.getClipboardPressedProperty().addListener(obs -> {
            if (model.isClipboardPressed())
                displayManager.resetSilhouette();
        });

        // Set up arranger view
        arrangerView.visibleProperty().bind(Bindings.createBooleanBinding(
        		() -> model.getMode() == SMPMode.ARRANGEMENT,
        		model.getModeProperty()));
        
        arrangementList.getSelectionModel().selectedItemProperty().addListener(this::onArrangementListSelectionChanged);
        
        arrangementList.setCellFactory(this::createArrangementSongListCell);

        // Set up options menu
        optionsMenu = new OptionsMenu(this, staff, model);
        
        // HACK
        staffMouseEventHandler = new StaffMouseEventHandler(staff, commandManager, model);
        
        // Set up top line.
        populateInstrumentButtons(instLine);
        
        selectedInst.imageProperty().bind(Bindings.createObjectBinding(() -> {
        	SMPInstrument i = model.getSelectedInstrument();
        	return resModel.getIcon(i.getImageIndex()).orElseThrow();
        }, model.getSelectedInstrumentProperty()));
        
        // Set up clipboard.
        rubberBand = new StaffRubberBand(model);
        new StaffClipboard(rubberBand, staff, this, model);
        
        volumeBars.mouseTransparentProperty().bind(model.getClipboardPressedProperty());
        
        // Fix TextField focus problems.
        new SongNameController(songName, this);
        songName.promptTextProperty().bind(Bindings.createStringBinding(
        		this::getItemNamePromptText, model.getModeProperty()));
        
        // Changing mode binds the bottom text to a different name property
        model.getModeProperty().addListener(this::onModeTypeChanged);
        
        songName.textProperty().bindBidirectional(model.getCurrentSongNameProperty());
        
        model.getCurrentSongNameProperty().addListener(obs -> staff.getSequence().setTitle(model.getCurrentSongName()));
        model.getCurrentArrangementNameProperty().addListener(obs -> staff.getArrangement().setTitle(model.getCurrentArrangementName()));
        
        // Set up tempo box
        tempoIndicator.textProperty().bindBidirectional(model.getTempoProperty(), new NumberStringConverter());
        tempoBox.setOnMousePressed(this::onTempoBoxMousePressed);
        
        // Setup scrollbar
        scrollbar.maxProperty().bind(Bindings.createIntegerBinding(
                () -> Math.max(model.getMaxLine() - Values.NOTELINES_IN_THE_WINDOW, 0),
                model.getMaxLineProperty()));
        scrollbar.valueProperty().bindBidirectional(
                model.getCurrentLineProperty());
        
        scrollbar.disableProperty().bind(model.getPlaybackActiveProperty());
        
        // If the scrollbar is disabled, we give focus elsewhere
    	// to be able to handle key events (hitting space should stop playback)
        model.getPlaybackActiveProperty().addListener(obs -> Platform.runLater(
        		(model.isPlaybackActive() ? basePane : scrollbar)::requestFocus));
        
        // Trigger a redraw, editing mode only
        InvalidationListener doRedraw = obv -> staff.redraw();
        
        model.getCurrentLineProperty().addListener(doRedraw);
        model.getTimeSignatureProperty().addListener(doRedraw);
        
        model.setCurrentLine(0);
        
        // Setup arrangement listview
        model.getArrangementSongIndexProperty().addListener(this::onArrangementSongIndexChanged);
        
        // Cleanup after a song or arrangement had finished running
        model.getPlaybackActiveProperty().addListener(obv -> {
            if (!model.isPlaybackActive()) {
                model.setArrangementSongIndex(-1);
                stopButton.setSelected(true);
                displayManager.resetPlayBars();
            }
        });
        
        // Disable buttons while playback is active
        Node[] btns = {
        		timeSigBtnFourFour, timeSigBtnThreeFour, timeSigBtnSixEight, timeSigBtnCustom,
        		modeButton, saveButton, loadButton, newButton, optionsButton,
        		tempoPlusButton, tempoMinusButton, addButton, deleteButton, upButton, downButton
        };
        
        Arrays.asList(btns).forEach(btn -> btn.disableProperty().bind(model.getPlaybackActiveProperty()));
    }
    
    private void manageShiftCtrlPresses(KeyEvent event) {
    	boolean ctrlPressed = event.isControlDown();
    	
    	model.setCtrlPressed(ctrlPressed);
    	model.setShiftPressed(event.isShiftDown());
    	
    	if (ctrlPressed) {
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
        }
    }
    
    private void onArrangementListSelectionChanged(Observable obs) {
    	if (model.isPlaybackActive())
            return;
        
        int songIndex = arrangementList.getSelectionModel().getSelectedIndex();
        if (songIndex == -1)
        	return;
        
        List<Song> seq = staff.getArrangement().getSequences();
        Window owner = arrangementList.getScene().getWindow();
        
        if (!confirmOperation(owner, PROMPT_LOAD_CONFIRM, true, false))
        	return;
        
        staff.populateStaff(seq.get(songIndex));
        seq.set(songIndex, staff.getSequence());
    }
    
    private ListCell<Song> createArrangementSongListCell(ListView<Song> list) {
    	return (new ListCell<>() {
        	@Override
        	public void updateItem(Song song, boolean empty) {
        		super.updateItem(song, empty);
        		
        		setGraphic(null);
        		setText(empty || song == null ? null : song.getTitle());
        	}
        });
    }
    
    private String getItemNamePromptText() {
        switch (model.getMode()) {
        case SONG:
            return "Song Name:";
        case ARRANGEMENT:
            return "Arrangement Name:";
        default:
            return "";
        }
    }
    
    private void onModeTypeChanged(Observable obs) {
    	switch (model.getMode()) {
        case SONG:
            songName.textProperty().unbindBidirectional(model.getCurrentArrangementNameProperty());
            songName.textProperty().bindBidirectional(model.getCurrentSongNameProperty());
            break;
        case ARRANGEMENT:
            songName.textProperty().unbindBidirectional(model.getCurrentSongNameProperty());
            songName.textProperty().bindBidirectional(model.getCurrentArrangementNameProperty());
            break;
        }
    }
    
    private void onTempoBoxMousePressed(MouseEvent evt) {
    	try {
            if (!model.isPlaybackActive() && model.getMode() == SMPMode.SONG) {
                Window owner = Utilities.getOwner(evt);
                String tempo = Dialog.showTextDialog("Tempo", owner);
                model.setTempo(Double.parseDouble(tempo.trim()));
            }
        } catch (NumberFormatException e) {
            // Do nothing.
        }
        evt.consume();
    }
    
    private void onArrangementSongIndexChanged(Observable obv) {
    	int idx = model.getArrangementSongIndex();
        arrangementList.getSelectionModel().select(idx);
        if (idx != -1)
            Platform.runLater(() -> arrangementList.scrollTo(idx));
        model.setCurrentSongName(arrangementList.getSelectionModel().getSelectedItem().getTitle());
    }
    
    private void onInstrumentButtonAction(SMPInstrument inst) {
    	if (model.isShiftPressed()) {
            boolean ex = model.getNoteExtension(inst.ordinal());
            model.setNoteExtension(inst.ordinal(), !ex);
            
            int i = BackendUtils.swapCoinPiranhaInstrumentIdxs(inst.ordinal());
            
            staff.getSequence().getNoteExtensions()[i] = !ex;
            
        } else if (model.isCtrlPressed()) {
            int flt = model.getFilteredNotes();
            int newFlt;
            int mask = ~ ((-1) << SMPInstrument.values().length);
            
            // we go through bitwise computations to only set the property once
            if ((flt & mask) == mask) {
                newFlt = 1 << inst.ordinal();
                
            } else {
                newFlt = flt ^ (1 << inst.ordinal());
                
                if ((newFlt & mask) == 0) {
                    newFlt = -1;
                }
            }
            
            model.setFilteredNotes(newFlt);
            
        } else {
            MidiChannel[] chan = resModel.getSoundPlayer().getChannels();
            if (chan[inst.getChannel() - 1] != null) {
                chan[inst.getChannel() - 1].noteOn(Values.DEFAULT_NOTE, Values.getDefaultVolume());
            }
            
            model.setSelectedInstrument(inst);
        }
    }
    
    private void populateInstrumentButtons(Pane n) {
        SMPInstrumentButton[] vs = new SMPInstrumentButton[SMPInstrument.values().length];
        n.getChildren().clear();
        
        for (SMPInstrument inst : SMPInstrument.values()) {
            SMPInstrumentButton b = new SMPInstrumentButton(inst.name(),
                    resModel.getIcon(inst.getImgIdxSustainOff()).orElseThrow(),
                    resModel.getIcon(inst.getImgIdxSustainOn()).orElseThrow());
            
            b.setImageFiltered(resModel.getIcon(ImageIndex.FILTER).orElseThrow());
            b.setFitHeight(28);
            b.setFitWidth(26);
            b.setFocusTraversable(false);
            
            b.setOnAction(e -> onInstrumentButtonAction(inst));
            
            vs[inst.ordinal()] = b;
            n.getChildren().add(b);
        }
        
        model.getNoteExtensionsProperty().addListener(obs -> {
            for (SMPInstrument inst : SMPInstrument.values()) {
                vs[inst.ordinal()].setSustainOn(model.getNoteExtension(inst.ordinal()));
            }
        });
        
        model.getFilteredNotesProperty().addListener((obs, oldv, newv) -> {
            int diff = (int) oldv ^ (int) newv;
            for (SMPInstrument inst : SMPInstrument.values()) {
                if ((diff & 1) == 1) {
                    boolean ex = model.getFilteredNote(inst.ordinal());
                    vs[inst.ordinal()].setActive(ex);
                }
                diff >>= 1;
            }
        });
    }
    
    public boolean confirmOperation(Window owner, String q, boolean checkForSong, boolean checkForArr) {
    	boolean songModified = checkForSong && model.isSongModified();
    	boolean arrModified = checkForArr && model.isArrangementModified();
    	
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
    
    public void setTimeSigFourFour(ActionEvent e) {
        staff.setTimeSignature(TimeSignature.FOUR_FOUR);
    }
    
    public void setTimeSigThreeFour(ActionEvent e) {
        staff.setTimeSignature(TimeSignature.THREE_FOUR);
    }
    
    public void setTimeSigSixEight(ActionEvent e) {
        staff.setTimeSignature(TimeSignature.SIX_EIGHT);
    }
    
    public void setTimeSigCustom(ActionEvent e) {
        Window owner = ((Node) e.getSource()).getScene().getWindow();
        String str = Dialog.showTextDialog(null, "Enter time signature:", "4/4, 3/4, 6/8, 6+3, ...", owner, true);
        if (str.isEmpty())
            return;
        
        try {
            TimeSignature t = TimeSignature.valueOf(str);
            staff.setTimeSignature(t);
            
            if (t.equals(TimeSignature.FOUR_FOUR)) {
                timeSigBtnFourFour.setSelected(true);
            } else if (t.equals(TimeSignature.THREE_FOUR)) {
                timeSigBtnThreeFour.setSelected(true);
            } else if (t.equals(TimeSignature.SIX_EIGHT)) {
                timeSigBtnSixEight.setSelected(true);
            }
            
        } catch (IllegalArgumentException ee) {
            Dialog.showDialog(ee.getMessage());
        }
    }
    
    @FXML
    public void tempoUp(ActionEvent e) {
        model.setTempo(model.getTempo() + 1);
    }
    
    @FXML
    public void tempoDown(ActionEvent e) {
        model.setTempo(model.getTempo() - 1);
    }
    
    public void switchMode() {
        if (model.isPlaybackActive())
            return;

        switch (model.getMode()) {
        case SONG:
            model.setMode(SMPMode.ARRANGEMENT);
            break;

        case ARRANGEMENT:
            model.setMode(SMPMode.SONG);
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
    	moveSongInArrangement(-1);
    }

    @FXML
    public void moveSongDownInArrangement(ActionEvent e) {
    	moveSongInArrangement(1);
    }
    
    private void moveSongInArrangement(int diff) {
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
        switch (model.getMode()) {
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
            model.setMaxLine(Values.DEFAULT_LINES_PER_SONG);
            getNameTextField().clear();
            model.setSongModified(false);
        }
    }
    
    public void newArrangement(Window owner) {
        if (confirmOperation(owner, "Create a new arrangement anyway?", false, true)) {
            staff.setArrangement(new Arrangement());
            getNameTextField().clear();
            arrangementList.getItems().clear();
            model.setArrangementModified(false);
        }
    }
    
    @FXML
    public void save(ActionEvent e) {
        save(Utilities.getOwner(e));
    }
    
    public void save(Window owner) {
        switch (model.getMode()) {
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
        	Dialog.showDialog(null, Utilities.getIllegalCharsDialogText("Illegal file name!\nPlease avoid those characters:"), owner);
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
            model.setArrangementModified(false);
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
        	Dialog.showDialog(null, Utilities.getIllegalCharsDialogText("Illegal file name!\nPlease avoid those characters:"), owner);
            return;
        }
        
        try {
        	File outputFile = FileChooserManager.saveAs(owner, chosenSongName);
            if (outputFile == null)
                return;
            FileOutputStream fOut = new FileOutputStream(outputFile);
            Song out = staff.getSequence();
            out.setTempo(model.getTempo());
            saveSongTxt(fOut, out);
            fOut.close();
            StateMachine.setCurrentDirectory(new File(outputFile.getParent()));
            model.setSongModified(false);
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
                        resModel.getSoundPlayer().storeInCache();
                        break;
                    }
                return null;
            }
        };
        
        new Thread(soundsetsTaskSave).start();
    }
    
    private static String noteToString(Note note) {
        String instName = note.getInstrument().toString();
        String noteName = Values.getNoteName(note.getVerticalPosition());
        String noteAcc = note.getAccidental().getToken();
        String muteName = note.getMuteModifier().getToken();
        return instName + " " + noteName + noteAcc + muteName;
    }
    
    @FXML
    public void load(ActionEvent e) {
        load(Utilities.getOwner(e));
    }

    public void load(Window owner) {
        switch (model.getMode()) {
        case SONG:
            Platform.runLater(() -> loadSong(owner));
            break;
            
        case ARRANGEMENT:
            Platform.runLater(() -> loadArrangement(owner));
            break;
        }
    }

    private void loadSong(Window owner) {
    	if (!confirmOperation(owner, PROMPT_LOAD_CONFIRM, true, false))
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
            Song loaded = SequenceDecoders.getAllTryable().decode(inputFile).orElseThrow(IOException::new);
            staff.populateStaff(loaded);
            getModifySongManager().reset();
            getNameTextField().setText(loaded.getTitle());
            model.setNoteExtensions(loaded.getNoteExtensions());
            model.setSongModified(false);
            
        } catch (FileNotFoundException e) {
            Dialog.showDialog(PROMPT_ERROR, "File " + inputFile + "not found!", owner);
            log.error("File not found error in loadSong:", e);
            
        } catch (IOException e) {
            Dialog.showDialog(PROMPT_ERROR, "An IO exception occurred while reading file " + inputFile + "!", owner);
            log.error("IO error in loadSong:", e);
            
        } catch (Exception e) {
            Dialog.showDialog(PROMPT_ERROR, "An error occurred while reading file " + inputFile + "!", owner);
            log.error("Error in loadSong:", e);
        }
    }

    private void loadArrangement(Window owner) {
    	if (!confirmOperation(owner, PROMPT_LOAD_CONFIRM, true, true))
    		return;
    	
        File inputFile = null;

        try {
        	inputFile = FileChooserManager.open(null);
        	if (inputFile == null)
        		return;
        	StateMachine.setCurrentDirectory(new File(inputFile.getParent()));
        	Arrangement loaded = ArrangementDecoders.SMP.getDecoder().decode(inputFile);
        	staff.populateStaffArrangement(loaded);
            
            arrangementList.getItems().clear();
            for (Song seq : loaded.getSequences()) {
            	arrangementList.getItems().add(seq);
            }
            
        	model.setSongModified(false);
        	model.setArrangementModified(false);
        	
        } catch (ParseException | StreamCorruptedException
        		| NullPointerException e) {
        	try {
        		Arrangement loaded = ArrangementDecoders.MPC.getDecoder().decode(inputFile);
        		StateMachine.setCurrentDirectory(new File(inputFile.getParent()));
        		staff.populateStaffArrangement(loaded);
        		model.setSongModified(false);
        		
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
