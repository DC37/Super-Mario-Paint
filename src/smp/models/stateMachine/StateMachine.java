package smp.models.stateMachine;

import java.io.File;
import java.util.HashSet;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.input.KeyCode;
import smp.components.Values;
import smp.models.staff.StaffSequence;

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

    /**
     * The file directory that we are currently located in. We'll start in the
     * user directory.
     */
    private static ObjectProperty<File> currentDirectory = new SimpleObjectProperty<>(new File(System.getProperty("user.dir")));

	/** Set of currently-pressed buttons. */
	private static ObservableSet<KeyCode> buttonsPressed = FXCollections
			.synchronizedObservableSet(FXCollections.observableSet(new HashSet<KeyCode>()));

	/**
	 * The default state that the program is in is the EDITING state, in which
	 * notes are being placed on the staff.
	 */
    private static ObjectProperty<ProgramState> currentState = new SimpleObjectProperty<>(ProgramState.EDITING);

    /**
     * The default time signature that we start out with is 4/4 time.
     */
    private static ObjectProperty<TimeSignature> currentTimeSignature = new SimpleObjectProperty<>(TimeSignature.FOUR_FOUR);
    
    public static class CurrentLineProperty extends SimpleDoubleProperty {
    	//TODO: auto-add these model comments
    	//====Models====
    	private ObjectProperty<StaffSequence> theSequence;
		public CurrentLineProperty(double initialValue) {
    		super(initialValue);
    		this.theSequence = Variables.theSequence;
		}
		@Override
    	public void set(double value) {
			value = value > 0 ? value : 0;
			int max = this.theSequence.get().getTheLinesSize().get() - Values.NOTELINES_IN_THE_WINDOW;
			value = value < max ? value : max;
            super.set(value);
    	}
    }
    /**
     * The current measure line number that the program is on. Typically a
     * number between 0 and 383. This is zero by default.
     */
    private static DoubleProperty currentLine = new CurrentLineProperty(0);

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
     * Gets the current line number that we're on. Typically a value between 0
     * and 383 for most files unless you've done fun stuff and removed the
     * 96-measure limit.
     *
     * @return The current line number (left justify)
     */
    public static DoubleProperty getMeasureLineNum() {
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
     * @return Set of currently-pressed buttons.
     */
    public static ObservableSet<KeyCode> getButtonsPressed() {
        return buttonsPressed;
    }

    /**
     * Clears the set of key presses in this program.
     */
    public static void clearKeyPresses() {
        buttonsPressed.clear();
    }

    /** @return Last directory we accessed. */
    public static ObjectProperty<File> getCurrentDirectory() {
        return currentDirectory;
    }

    /** @param cDir Set current directory to this. */
    public static void setCurrentDirectory(File cDir) {
        StateMachine.currentDirectory.set(cDir);
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
