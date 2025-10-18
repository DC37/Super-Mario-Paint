package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.util.ArrayList;

import backend.editing.ModifySongManager;
import backend.saving.mpc.MPCDecoder;
import backend.songs.Accidental;
import backend.songs.StaffArrangement;
import backend.songs.StaffNote;
import backend.songs.StaffSequence;
import backend.songs.TimeSignature;
import gui.clipboard.StaffClipboard;
import gui.clipboard.StaffRubberBand;
import gui.components.Controls;
import gui.components.SongNameController;
import gui.components.buttons.ImageRadioButton;
import gui.components.buttons.SMPToggleButton;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
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

    @FXML
    private SMPToggleButton loopButton;

    @FXML
    private SMPToggleButton muteButton;

    @FXML
    private SMPToggleButton muteInstButton;
    
    @FXML
    private SMPToggleButton clipboardButton;
    
    @FXML
    private ImageView timesig_4_4;
    
    @FXML
    private ImageView timesig_3_4;
    
    @FXML
    private ImageView timesig_6_8;
    
    @FXML
    private ImageView timesig_custom;

    @FXML
    private Parent arrangerView;
    
    @FXML
    private ListView<String> arrangementList;

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
    
    /** Handles the options menu */
    private OptionsMenu optionsMenu;
    
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
        controlPanel = new Controls(staff, this, il);
        staff.setControlPanel(controlPanel);
        makeKeyboardHandlers(basePane);
        
        loopButton.selectedProperty().bindBidirectional(StateMachine.loopPressedProperty());
        muteButton.selectedProperty().bindBidirectional(StateMachine.mutePressedProperty());
        muteInstButton.selectedProperty().bindBidirectional(StateMachine.muteAPressedProperty());
        clipboardButton.selectedProperty().bindBidirectional(StateMachine.clipboardPressedProperty());
        
        ToggleGroup muteToggleGroup = new ToggleGroup();
        muteButton.setToggleGroup(muteToggleGroup);
        muteInstButton.setToggleGroup(muteToggleGroup);
        
        Tooltip.install(clipboardButton, new Tooltip("Click (or Shift+R) to toggle region selection\n"
                + "Hover over instrument & press F to filter instrument\n"
                + "Ctrl+A to select all\n"
                + "Ctrl+C to copy notes\n"
                + "Ctrl+V to paste notes\n"
                + "Ctrl+X to cut notes\n"
                + "Delete to delete notes\n"
                + "Alt+N to toggle notes selection\n"
                + "Alt+V to toggle volumes selection"));

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
            if (songIndex != -1) {
                ArrayList<StaffSequence> seq = staff.getArrangement().getTheSequences();
                ArrayList<File> files = staff.getArrangement().getTheSequenceFiles();
                Window owner = arrangementList.getScene().getWindow();
                Utilities.loadSequenceFromArrangement(files.get(songIndex), staff, SMPFXController.this, owner);
                seq.set(songIndex, staff.getSequence());
            }
        });

        // Set up options menu
        optionsMenu = new OptionsMenu(this, staff);
        
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
        
        // Set up clipboard.
        rubberBand = new StaffRubberBand();
        new StaffClipboard(rubberBand, staff, this, il);
        
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
        
        modeText.textProperty().bind(Bindings.createStringBinding(() -> {
            switch (StateMachine.getMode() ) {
            case SONG:
                return "Song";
            case ARRANGEMENT:
                return "Arr";
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
            StateMachine.setCurrentSongName(arrangementList.getSelectionModel().getSelectedItem());
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
    public void addSongtoArrangement(ActionEvent e) {
        staff.addSongToArrangement();
    }
    
    @FXML
    public void deleteSongFromArrangement(ActionEvent e) {
        staff.deleteSongFromArrangement();
    }
    
    @FXML
    public void moveSongUpInArrangement(ActionEvent e) {
        staff.moveSongInArrangement(1);
    }

    @FXML
    public void moveSongDownInArrangement(ActionEvent e) {
        staff.moveSongInArrangement(-1);
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
            outputFile = f.showSaveDialog(owner);
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
            outputFile = f.showSaveDialog(owner);
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
        boolean cont = true;
        if (StateMachine.isSongModified())
            cont = Dialog
                    .showYesNoDialog("The current song has been modified!\n"
                            + "Load anyway?", owner);
        File inputFile = null;
        if (cont) {
            try {
                FileChooser f = new FileChooser();
                FileChooser.ExtensionFilter filterTxt = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                FileChooser.ExtensionFilter filterAny = new FileChooser.ExtensionFilter("Any files (*.*)", "*.*");
                f.getExtensionFilters().add(filterTxt);
                f.getExtensionFilters().add(filterAny);
                f.setInitialDirectory(StateMachine.getCurrentDirectory());
                inputFile = f.showOpenDialog(null);
                if (inputFile == null)
                    return;
                StateMachine.setCurrentDirectory(new File(inputFile.getParent()));
                loadSong(inputFile, owner);
            } catch (Exception e) {
                Dialog.showDialog("Not a valid song file.", owner);
            }
        }
    }

    private void loadSong(File inputFile, Window owner) {
        try {
            StaffSequence loaded = null;
            try {
                loaded = MPCDecoder.decode(inputFile);
            } catch (ParseException e1) {
                loaded = Utilities.loadSong(inputFile);
            }
            if (loaded == null) {
                throw new IOException();
            }
            String fname = Utilities.populateStaff(loaded, inputFile, false, staff, this);
            getNameTextField().setText(fname);
            StateMachine.setNoteExtensions(loaded.getNoteExtensions());
            getInstBLine().updateNoteExtensions();
            StateMachine.setSongModified(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Dialog.showDialog("Problem loading file!", owner);
            e.printStackTrace();
        } catch (Exception e) {
            Dialog.showDialog("Not a valid song file.", owner);
        }
    }

    private void loadArrangement(Window owner) {
        boolean cont = true;
        if (StateMachine.isSongModified() || StateMachine.isArrModified()) {
            if (StateMachine.isSongModified() && StateMachine.isArrModified()) {
                cont = Dialog
                        .showYesNoDialog("The current song and arrangement\n"
                                + "have both been modified!\nLoad anyway?", owner);
            } else if (StateMachine.isSongModified()) {
                cont = Dialog
                        .showYesNoDialog("The current song has been modified!\n"
                                + "Load anyway?", owner);
            } else if (StateMachine.isArrModified()) {
                cont = Dialog
                        .showYesNoDialog("The current arrangement has been\n"
                                + "modified! Load anyway?", owner);
            }
        }
        File inputFile = null;
        if (cont) {
            try {
                FileChooser f = new FileChooser();
                FileChooser.ExtensionFilter filterTxt = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                FileChooser.ExtensionFilter filterAny = new FileChooser.ExtensionFilter("Any files (*.*)", "*.*");
                f.getExtensionFilters().add(filterTxt);
                f.getExtensionFilters().add(filterAny);
                f.setInitialDirectory(StateMachine.getCurrentDirectory());
                inputFile = f.showOpenDialog(null);
                if (inputFile == null)
                    return;
                StateMachine.setCurrentDirectory(new File(inputFile.getParent()));
                StaffArrangement loaded = Utilities.loadArrangement(inputFile);
                Utilities.normalizeArrangement(loaded, inputFile);
                Utilities.populateStaffArrangement(loaded, inputFile, false, staff, this, owner);
                StateMachine.setSongModified(false);
                StateMachine.setArrModified(false);
            } catch (ClassNotFoundException | StreamCorruptedException
                    | NullPointerException e) {
                try {
                    StaffArrangement loaded = MPCDecoder
                            .decodeArrangement(inputFile);
                    StateMachine.setCurrentDirectory(new File(inputFile.getParent()));
                    Utilities.normalizeArrangement(loaded, inputFile);
                    Utilities.populateStaffArrangement(loaded, inputFile, true, staff, this, owner);
                    StateMachine.setSongModified(false);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Dialog.showDialog("Not a valid arrangement file.", owner);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    public void options(ActionEvent e) {
        options(Utilities.getOwner(e));
    }
    
    public void options(Window owner) {
        optionsMenu.options(owner);
    }
    
    private void makeKeyboardHandlers(Node n) {
        n.addEventHandler(KeyEvent.KEY_PRESSED, ke -> {
            switch(ke.getCode()) {
            case PAGE_UP:
                if (StateMachine.isPlaybackActive())
                    break;
                
                staff.shift(-Values.NOTELINES_IN_THE_WINDOW);
                break;

            case PAGE_DOWN:
                if (StateMachine.isPlaybackActive())
                    break;
                
                staff.shift(Values.NOTELINES_IN_THE_WINDOW);
                break;

            case HOME:
                if (StateMachine.isPlaybackActive())
                    break;
                
                if (ke.isControlDown())
                    staff.setLocation(0);
                break;

            case END:
                if (StateMachine.isPlaybackActive())
                    break;
                
                if (ke.isControlDown())
                    staff.setLocation((int) scrollbar.getMax());
                break;

            case A:
                if (StateMachine.isPlaybackActive())
                    break;
                
                if (!ke.isControlDown() && !ke.isShiftDown())
                    staff.moveLeft();
                
                if (ke.isShiftDown())
                    staff.jumpToNext();
                break;

            case LEFT:
                if (StateMachine.isPlaybackActive())
                    break;
                
                if (songName.focusedProperty().get()) // Don't trigger while typing name
                    break;
                
                if (ke.isControlDown() || ke.isShiftDown())
                    staff.jumpToPrevious();
                break;

            case D:
                if (StateMachine.isPlaybackActive())
                    break;
                
                if (!ke.isControlDown() && !ke.isShiftDown())
                    staff.moveRight();
                
                if (ke.isControlDown() || ke.isShiftDown())
                    staff.jumpToNext();
                break;

            case RIGHT:
                if (StateMachine.isPlaybackActive())
                    break;
                
                if (songName.focusedProperty().get()) // Don't trigger while typing name
                    break;
                
                if (ke.isControlDown() || ke.isShiftDown())
                    staff.jumpToNext();
                break;

            case SPACE:
                if (songName.focusedProperty().get()) // Don't trigger while typing name
                    break;

                if (ke.isControlDown() || ke.isShiftDown())
                    staff.setLocation(0);
                
                if (StateMachine.isPlaybackActive()) {
                    staff.stop();
                    
                } else {
                    staff.play();
                }

                break;
                
            case R:
                if (songName.focusedProperty().get())
                    break;
                
                if (ke.isShiftDown())
                    StateMachine.setClipboardPressed(!StateMachine.isClipboardPressed());
                break;
                
            case S:
                if (songName.focusedProperty().get())
                    break;
                
                if (ke.isControlDown())
                    save(Utilities.getOwner(ke));
                break;
                
            case M:
                if (songName.focusedProperty().get())
                    break;
                
                StateMachine.setMuteAPressed(!StateMachine.isMuteAPressed());
                break;
                
            case N:
                if (songName.focusedProperty().get())
                    break;
                
                if (!ke.isControlDown() && !ke.isAltDown())
                    StateMachine.setMutePressed(!StateMachine.isMutePressed());
                
                else if (ke.isControlDown())
                    newSongOrArrangement(Utilities.getOwner(ke));
                break;
                
            case O:
                if (songName.focusedProperty().get())
                    break;
                
                if (ke.isControlDown())
                    load(Utilities.getOwner(ke));
                break;
                
            case COMMA:
                if (songName.focusedProperty().get())
                    break;
                
                if (ke.isControlDown())
                    options(Utilities.getOwner(ke));
                break;
                
            default:
            }

            StateMachine.getButtonsPressed().add(ke.getCode());

            if (StateMachine.isCursorOnStaff()) {
                Accidental acc = StaffMouseEventHandler.computeAccidental();
                staff.getDisplayManager().refreshSilhouette(acc);
            }

            ke.consume();
        }
                );

        n.addEventHandler(KeyEvent.KEY_RELEASED, ke -> {
            StateMachine.getButtonsPressed().remove(ke.getCode());

            if (StateMachine.isCursorOnStaff()) {
                Accidental acc = StaffMouseEventHandler.computeAccidental();
                staff.getDisplayManager().refreshSilhouette(acc);
            }

            ke.consume();
        });

        n.addEventHandler(ScrollEvent.ANY, se -> {
            if (StateMachine.isPlaybackActive())
                return;

            if (se.getDeltaY() < 0) {
                if (se.isControlDown())
                    staff.shift(4);
                
                else
                    staff.moveRight();
                
            } else if (se.getDeltaY() > 0) {
                if (se.isControlDown())
                    staff.shift(-4);
                
                else
                    staff.moveLeft();
            }

            se.consume();
        });
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
    
    public Parent getArrangerView() {
        return arrangerView;
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

    /** @return The mode button image. */
    public ImageView getModeButton() {
        return modeButton;
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
