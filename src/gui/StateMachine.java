package gui;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import backend.songs.TimeSignature;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;

/**
 * This is the state machine that keeps track of what state the main window is
 * in. This class keeps track of a bunch of variables that the program generally
 * uses.
 *
 * @author RehdBlob
 * @since 2012.08.07
 */
public class StateMachine {
	
	private static BooleanProperty ctrlPressed = new SimpleBooleanProperty();
	
	private static BooleanProperty shiftPressed = new SimpleBooleanProperty();
	
	private static ObjectProperty<InstrumentIndex> selectedInstrument = new SimpleObjectProperty<>(InstrumentIndex.MARIO);

    /** This tells us whether we have modified the song or not. */
    private static boolean modifiedSong = false;

    /** This tells us whether we have modified the arrangement or not. */
    private static boolean modifiedArr = false;

    /** This keeps track of whether we have pressed the loop button or not. */
    private static BooleanProperty loopPressed = new SimpleBooleanProperty(false);

	/** This keeps track of whether we have pressed the mute button or not. */
	private static BooleanProperty mutePressed = new SimpleBooleanProperty(false);

    /** This keeps track of whether we have pressed the low A mute button or not. */
    private static BooleanProperty muteAPressed = new SimpleBooleanProperty(false);

	/**
	 * This keeps track of whether we have pressed the clipboard button or not.
	 */
	private static BooleanProperty clipboardPressed = new SimpleBooleanProperty(false);

    /** The list of values denoting which notes should be extended. */
    private static IntegerProperty noteExtensions = new SimpleIntegerProperty(0);
    
    /**
     * The file directory that we are currently located in. We'll start in the
     * user directory.
     */
    private static File currentDirectory = new File(System.getProperty("user.dir"));

    /** Set of currently-pressed buttons. */
    private static Set<KeyCode> buttonsPressed =
            Collections.synchronizedSet(new HashSet<KeyCode>());

    private static ObjectProperty<SMPMode> mode = new SimpleObjectProperty<>(SMPMode.SONG);
    
    private static BooleanProperty playbackActive = new SimpleBooleanProperty(false);

    /**
     * The default time signature that we start out with is 4/4 time.
     */
    private static ObjectProperty<TimeSignature> currentTimeSignature = new SimpleObjectProperty<>(TimeSignature.FOUR_FOUR);

    /**
     * The current measure line number. Set to -1 as a special "uninitialized" value,
     * to force the initial redraw.
     */
    private static IntegerProperty currentLine = new SimpleIntegerProperty(-1);
    
    /**
     * The furthest you can reach by scrolling to the end of the sequence.
     * Technically this is the first line that cannot be displayed.
     */
    private static IntegerProperty maxLine = new SimpleIntegerProperty(Values.DEFAULT_LINES_PER_SONG);
    
    /**
     * Currently selected song in arranger mode. Set to -1 while in song mode.
     */
    private static IntegerProperty arrangementSongIndex = new SimpleIntegerProperty(-1);

    /**
     * This is the current tempo that the program is running at.
     */
    private static DoubleProperty tempo = new SimpleDoubleProperty(Values.DEFAULT_TEMPO);

	/**
	 * The current soundset name. This should change when a new soundfont is
	 * loaded.
	 */
	private static String currentSoundset = Values.DEFAULT_SOUNDFONT;
	
	private static BooleanProperty cursorOnStaff = new SimpleBooleanProperty();
	
	private static StringProperty currentSongName = new SimpleStringProperty("");
    private static StringProperty currentArrangementName = new SimpleStringProperty("");

    /**
     * Do not make an instance of this class! The implementation is such that
     * several classes may check the overall state of the program, so there
     * should only ever be just the class and its static variables and methods
     * around.
     *
     * @deprecated
     */
    private StateMachine() {
    }
    
    public static ReadOnlyBooleanProperty ctrlPressed() {
    	return ctrlPressed;
    }
    
    protected static void setCtrlPressed(boolean b) {
    	ctrlPressed.setValue(b);
    }
    
    public static boolean isCtrlPressed() {
    	return ctrlPressed.getValue();
    }
    
    public static ReadOnlyBooleanProperty shiftPressed() {
    	return shiftPressed;
    }
    
    protected static void setShiftPressed(boolean b) {
    	shiftPressed.setValue(b);
    }
    
    public static boolean isShiftPressed() {
    	return shiftPressed.getValue();
    }
    
    public static ObjectProperty<InstrumentIndex> selectedInstrumentProperty() {
    	return selectedInstrument;
    }
    
    public static InstrumentIndex getSelectedInstrument() {
    	return selectedInstrument.get();
    }
    
    public static void setSelectedInstrument(InstrumentIndex i) {
    	selectedInstrument.set(i);
    }
    
    public static ObjectProperty<SMPMode> modeProperty() {
        return mode;
    }

    public static SMPMode getMode() {
        return mode.get();
    }

    public static void setMode(SMPMode m) {
        mode.set(m);
    }
    
    public static BooleanProperty getPlaybackActiveProperty() {
        return playbackActive;
    }

    public static boolean isPlaybackActive() {
        return playbackActive.get();
    }
    
    public static void setPlaybackActive(boolean b) {
        playbackActive.set(b);
    }
    
    public static ObjectProperty<TimeSignature> getTimeSignatureProperty() {
        return currentTimeSignature;
    }

    public static TimeSignature getTimeSignature() {
        return currentTimeSignature.get();
    }

    public static void setTimeSignature(TimeSignature t) {
        currentTimeSignature.set(t);;
    }

    /**
     * @return The tempo that this program is running at.
     */
    public static double getTempo() {
        return tempo.get();
    }
    
    public static DoubleProperty getTempoProperty() {
        return tempo;
    }

    /**
     * Sets the tempo to what we give it here.
     *
     * @param num
     *            The tempo we want to set the program to run at.
     * @return The current tempo.
     */
    public static void setTempo(double num) {
        tempo.set(num);
    }
    
    public static IntegerProperty getCurrentLineProperty() {
        return currentLine;
    }

    /**
     * Gets the current line number that we're on. Typically a value between 0
     * and 383 for most files unless you've done fun stuff and removed the
     * 96-measure limit.
     *
     * @return The current line number (left justify)
     */
    public static int getMeasureLineNum() {
        return currentLine.get();
    }

    /**
     * Sets the current line number to whatever is given to this method.
     *
     * @param num
     *            The number that we're trying to set our current line number
     *            to.
     */
    public static void setMeasureLineNum(int num) {
        currentLine.set(num);
    }
    
    public static IntegerProperty getMaxLineProperty() {
        return maxLine;
    }
    
    public static int getMaxLine() {
        return maxLine.get();
    }
    
    public static void setMaxLine(int num) {
        maxLine.set(num);
    }
    
    public static IntegerProperty getArrangementSongIndexProperty() {
        return arrangementSongIndex;
    }
    
    public static int getArrangementSongIndex() {
        return arrangementSongIndex.get();
    }
    
    public static void setArrangementSongIndex(int i) {
        arrangementSongIndex.set(i);
    }
    
    public static BooleanProperty loopPressedProperty() {
        return loopPressed;
    }

    public static void setLoopPressed(boolean b) {
        loopPressed.set(b);
    }

    public static boolean isLoopPressed() {
        return loopPressed.get();
    }
    
    public static BooleanProperty mutePressedProperty() {
        return mutePressed;
    }

    public static void setMutePressed(boolean b) {
        mutePressed.set(b);
    }

    public static boolean isMutePressed() {
        return mutePressed.get();
    }

    public static BooleanProperty muteAPressedProperty() {
        return muteAPressed;
    }
    
    public static void setMuteAPressed(boolean b) {
        muteAPressed.set(b);
    }

    public static boolean isMuteAPressed() {
        return muteAPressed.get();
    }

    /**
     * Sets the modified flag to true or false.
     *
     * @param b
     *            Whether we have modified a song or not.
     */
    public static void setSongModified(boolean b) {
        modifiedSong = b;
    }

    /**
     * @return Whether we have modified the current song or not.
     */
    public static boolean isSongModified() {
        return modifiedSong;
    }

    /**
     * @param b
     *            Whether we have modified an arrangement or not.
     */
    public static void setArrModified(boolean b) {
        modifiedArr = b;
    }

    /**
     * @return Whether we have modified the current arrangement or not.
     */
    public static boolean isArrModified() {
        return modifiedArr;
    }
    
    public static BooleanProperty clipboardPressedProperty() {
        return clipboardPressed;
    }
    
    public static void setClipboardPressed(boolean b) {
    	clipboardPressed.set(b);
    }
    
    public static boolean isClipboardPressed() {
    	return clipboardPressed.get();
    }
    
    public static IntegerProperty noteExtensionsProperty() {
    	return noteExtensions;
    }
    
    /**
     * Set specific bit
     * @param idx index of the bit to modify, is in interval [0, 31]
     * @param b value to set
     */
    public static void setNoteExtension(int idx, boolean b) {
    	int v = noteExtensions.get();
    	int m = 1 << idx;
    	v = b ? v | m : v & (~m);
    	noteExtensions.set(v);
    }

    public static void setNoteExtensions(boolean[] set) {
    	for (int i = 0; i < set.length; i++) {
    		setNoteExtension(i, set[i]);
    	}
    }
    
    /**
     * Get specific bit
     * @param idx index of the bit to read, is in interval [0, 31]
     */
    public static boolean getNoteExtension(int idx) {
    	int m = 1 << idx;
    	int v = noteExtensions.get();
    	return (v & m) != 0;
    }

    public static boolean[] getNoteExtensions() {
    	boolean[] ret = new boolean[32];
    	for (int i = 0; i < 32; i++) {
    		ret[i] = getNoteExtension(i);
    	}
    	return ret;
    }

    /**
     * @return Set of currently-pressed buttons.
     */
    public static Set<KeyCode> getButtonsPressed() {
        return buttonsPressed;
    }

    /**
     * Clears the set of key presses in this program.
     */
    public static void clearKeyPresses() {
        buttonsPressed.clear();

    }

    /** @return Last directory we accessed. */
    public static File getCurrentDirectory() {
        return currentDirectory;
    }

    /** @param cDir Set current directory to this. */
    public static void setCurrentDirectory(File cDir) {
        StateMachine.currentDirectory = cDir;
    }

	/**
	 * @return The current soundset name.
	 * @since v1.1.2
	 */
	public static String getCurrentSoundset() {
		return currentSoundset;
	}

	/**
	 * @param soundset
	 *            Set current soundset to this.
	 * @since v1.1.2
	 */
	public static void setCurrentSoundset(String soundset) {
		StateMachine.currentSoundset = soundset;
    }
	
	public static BooleanProperty getCursorOnStaffProperty() {
	    return cursorOnStaff;
	}
	
	public static boolean isCursorOnStaff() {
	    return cursorOnStaff.get();
	}
	
	public static void setCursorOnStaff(boolean b) {
	    cursorOnStaff.set(b);
	}
	
	public static StringProperty currentSongNameProperty() {
	    return currentSongName;
	}
	
	public static void setCurrentSongName(String s) {
	    currentSongName.set(s);
	}
	
	public static String getCurrentSongName() {
	    return currentSongName.get();
	}
    
    public static StringProperty currentArrangementNameProperty() {
        return currentArrangementName;
    }
    
    public static void setCurrentArrangementName(String s) {
        currentArrangementName.set(s);
    }
    
    public static String getCurrentArrangementName() {
        return currentArrangementName.get();
    }

}
