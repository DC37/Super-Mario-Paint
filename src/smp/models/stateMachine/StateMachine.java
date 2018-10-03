package smp.models.stateMachine;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import smp.components.Values;

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
    private static BooleanProperty modifiedSong = new SimpleBooleanProperty(false);

    /** This tells us whether we have modified the arrangement or not. */
    private static BooleanProperty modifiedArr = new SimpleBooleanProperty(false);

    /** This keeps track of whether we have pressed the loop button or not. */
    private static BooleanProperty loopPressed = new SimpleBooleanProperty(false);

	/** This keeps track of whether we have pressed the mute button or not. */
	private static BooleanProperty mutePressed = new SimpleBooleanProperty(false);

    /**
     * This keeps track of whether we have pressed the low A mute button or not.
     */
    private static BooleanProperty muteAPressed = new SimpleBooleanProperty(false);

	/**
	 * This keeps track of whether we have pressed the clipboard button or not.
	 */
	private static BooleanProperty clipboardPressed = new SimpleBooleanProperty(false);

    /** The list of values denoting which notes should be extended. */
    private static BooleanProperty[] noteExtensions = new BooleanProperty[Values.NUMINSTRUMENTS];
    
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

    /**
     * The default time signature that we start out with is 4/4 time.
     */
    private static ObjectProperty<TimeSignature> currentTimeSignature = new SimpleObjectProperty<>(TimeSignature.FOUR_FOUR);

    /**
     * The current measure line number that the program is on. Typically a
     * number between 0 and 383. This is zero by default.
     */
    private static IntegerProperty currentLine = new SimpleIntegerProperty(0);

    /**
     * This is the current tempo that the program is running at.
     */
    private static DoubleProperty tempo = new SimpleDoubleProperty(Values.DEFAULT_TEMPO);

	/**
	 * The current soundset name. This should change when a new soundfont is
	 * loaded.
	 */
	private static StringProperty currentSoundset = new SimpleStringProperty(Values.DEFAULT_SOUNDFONT);

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

    /**
     * Get the current <code>State</code> of the <code>StateMachine</code>
     *
     * @return The current <code>State</code>.
     */
    public static ObjectProperty<ProgramState> getState() {
        return currentState;
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

    /**
     * @return The current time signature that we are running at.
     */
    public static ObjectProperty<TimeSignature> getTimeSignature() {
        return currentTimeSignature;
    }

    /**
     * Sets the time signature to whatever that we give this method.
     *
     * @param t
     *            The new time signature.
     */
    public static void setTimeSignature(TimeSignature t) {
        currentTimeSignature.set(t);
    }

    /** Sets the time signature back to "4/4" by default. */
    public static void resetTimeSignature() {
        currentTimeSignature.set(TimeSignature.FOUR_FOUR);
    }

    /**
     * @return The tempo that this program is running at.
     */
    public static DoubleProperty getTempo() {
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

    /**
     * Gets the current line number that we're on. Typically a value between 0
     * and 383 for most files unless you've done fun stuff and removed the
     * 96-measure limit.
     *
     * @return The current line number (left justify)
     */
    public static IntegerProperty getMeasureLineNum() {
        return currentLine;
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

    /** Sets that the song is now loop-enabled. */
    public static void setLoopPressed(boolean b) {
        loopPressed.set(b);
    }

    /** @return Whether the loop button is pressed or not. */
    public static BooleanProperty getLoopPressed() {
        return loopPressed;
    }

    /** Sets the fact that mute notes are now enabled. */
	public static void setMutePressed(boolean b) {
		mutePressed.set(b);
	}

    /** @return Whether the mute button is pressed or not. */
    public static BooleanProperty getMutePressed() {
        return mutePressed;
    }

    /**
     * Sets the modified flag to true or false.
     *
     * @param b
     *            Whether we have modified a song or not.
     */
    public static void setSongModified(boolean b) {
        modifiedSong.set(b);
    }

    /**
     * @return Whether we have modified the current song or not.
     */
    public static BooleanProperty getSongModified() {
        return modifiedSong;
    }

    /**
     * @param b
     *            Whether we have modified an arrangement or not.
     */
    public static void setArrModified(boolean b) {
        modifiedArr.set(b);
    }

    /**
     * @return Whether we have modified the current arrangement or not.
     */
    public static BooleanProperty getArrModified() {
        return modifiedArr;
    }

    /**
     * @param b
     *            Whether we have pressed the low A mute button.
     */
    public static void setMuteAPressed(boolean b) {
        muteAPressed.set(b);
    }

    /**
     * @return Whether our mute-all button is pressed or not.
     */
    public static BooleanProperty getMuteAPressed() {
        return muteAPressed;
    }
    
    /**
     * @since 08.2017
     */
    public static void setClipboardPressed(boolean b) {
    	clipboardPressed.set(b);
    }
    public static BooleanProperty getClipboardPressed() {
    	return clipboardPressed;
    }
    

    /**
     * @param set The note extensions that we want to set.
     */
    public static void setNoteExtensions(BooleanProperty[] set) {
        noteExtensions = set;
    }

    /**
     * @return A list of notes that we want to act like the coin.
     */
    public static BooleanProperty[] getNoteExtensions() {
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
	public static StringProperty getCurrentSoundset() {
		return currentSoundset;
	}

	/**
	 * @param soundset
	 *            Set current soundset to this.
	 * @since v1.1.2
	 */
	public static void setCurrentSoundset(String soundset) {
		StateMachine.currentSoundset.set(soundset);
	}
}
