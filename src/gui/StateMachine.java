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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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

    /** This tells us whether we have modified the song or not. */
    private static boolean modifiedSong = false;

    /** This tells us whether we have modified the arrangement or not. */
    private static boolean modifiedArr = false;

    /** This keeps track of whether we have pressed the loop button or not. */
    private static boolean loopPressed = false;

	/** This keeps track of whether we have pressed the mute button or not. */
	private static boolean mutePressed = false;

    /**
     * This keeps track of whether we have pressed the low A mute button or not.
     */
    private static boolean muteAPressed = false;

	/**
	 * This keeps track of whether we have pressed the clipboard button or not.
	 */
	private static boolean clipboardPressed = false;
	
	private static BooleanProperty selectionModeOn = new SimpleBooleanProperty(false);

    /** The list of values denoting which notes should be extended. */
    private static boolean[] noteExtensions = new boolean[Values.NUMINSTRUMENTS];
    
    /**
     * The file directory that we are currently located in. We'll start in the
     * user directory.
     */
    private static File currentDirectory = new File(System.getProperty("user.dir"));

    /** Set of currently-pressed buttons. */
    private static Set<KeyCode> buttonsPressed =
            Collections.synchronizedSet(new HashSet<KeyCode>());

    /**
     * The default state that the program is in is the EDITING state, in which
     * notes are being placed on the staff.
     */
    private static ObjectProperty<ProgramState> currentState = new SimpleObjectProperty<>(ProgramState.EDITING);
    
    private static BooleanProperty playbackActive = new SimpleBooleanProperty(false);
    
    /**
     * Index of the current position on the screen during playback
     */
    private static IntegerProperty playbackPosition = new SimpleIntegerProperty(-1);

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
    
    public static ObjectProperty<ProgramState> getStateProperty() {
        return currentState;
    }

    /**
     * Get the current <code>State</code> of the <code>StateMachine</code>
     *
     * @return The current <code>State</code>.
     */
    public static ProgramState getState() {
        return currentState.get();
    }

    /**
     * @param s
     *            Set the <code>StateMachine</code> to a certain State.
     */
    public static void setState(ProgramState s) {
        currentState.set(s);
    }

    /**
     * Sets the state back to "Editing" by default.
     */
    public static void resetState() {
        currentState.set(ProgramState.EDITING);
    }
    
    public static BooleanProperty getPlaybackActiveProperty() {
        return playbackActive;
    }

    public static boolean isPlaybackActive() {
        return playbackActive.get();
    }
    
    public static IntegerProperty getPlaybackPositionProperty() {
        return playbackPosition;
    }
    
    public static int getPlaybackPosition() {
        return playbackPosition.get();
    }
    
    public static void setPlaybackPosition(int n) {
        playbackPosition.set(n);
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

    /** Sets that the song is now loop-enabled. */
    public static void setLoopPressed() {
        loopPressed = true;
    }

    /** Sets that the song is now *not* loop-enabled. */
    public static void resetLoopPressed() {
        loopPressed = false;
    }

    /** @return Whether the loop button is pressed or not. */
    public static boolean isLoopPressed() {
        return loopPressed;
    }

    /** Sets the fact that mute notes are now enabled. */
    public static void setMutePressed() {
        mutePressed = true;
    }

    /** Turns off the fact that we have pressed the mute button. */
    public static void resetMutePressed() {
        mutePressed = false;
    }

    /** @return Whether the mute button is pressed or not. */
    public static boolean isMutePressed() {
        return mutePressed;
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

    /**
     * @param b
     *            Whether we have pressed the low A mute button.
     */
    public static void setMuteAPressed(boolean b) {
        muteAPressed = b;
    }

    /**
     * @return Whether our mute-all button is pressed or not.
     */
    public static boolean isMuteAPressed() {
        return muteAPressed;
    }
    
    /**
     * @since 08.2017
     */
    public static void setClipboardPressed() {
    	clipboardPressed = true;
    }
    public static void resetClipboardPressed() {
    	clipboardPressed = false;
    }
    public static boolean isClipboardPressed() {
    	return clipboardPressed;
    }
    
    public static BooleanProperty getSelectionModeOnProperty() {
        return selectionModeOn;
    }
    
    public static boolean isSelectionModeOn() {
        return selectionModeOn.get();
    }
    
    public static void setSelectionModeOn(boolean b) {
        selectionModeOn.set(b);
    }

    /**
     * @param set The note extensions that we want to set.
     */
    public static void setNoteExtensions(boolean[] set) {
        noteExtensions = set;
    }

    /**
     * @return A list of notes that we want to act like the coin.
     */
    public static boolean[] getNoteExtensions() {
        return noteExtensions;
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

}
