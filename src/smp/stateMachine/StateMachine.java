package smp.stateMachine;

import smp.components.staff.StaffInstrumentEventHandler;

/**
 * This is the state machine that keeps track of what state the
 * main window is in.
 * @author RehdBlob
 * @since 2012.08.07
 */
public class StateMachine {

    /** This keeps track of whether we have pressed the loop button or not. */
    private static boolean loopPressed = false;

    /** Flips different bits to see which keys are pressed. */
    private static int buttonPresses = 0b0;

    /**
     * The default state that the program is in is the
     * EDITING state, in which notes are being placed on the staff.
     */
    private static State currentState = State.EDITING;

    /**
     * The default time signature that we start out with is 4/4 time.
     */
    private static TimeSignature currentTimeSignature = TimeSignature.FOUR_FOUR;

    /**
     * The current focus that we have on the staff which we are going to update
     * flats and sharps for.
     */
    private static StaffInstrumentEventHandler focusPane;

    /**
     * The current measure line number that the program is on. Typically
     * a number between 0 and 383. This is zero by default.
     */
    private static int currentLine = 0;

    /**
     * This is the current tempo that the program is running at.
     */
    private static double tempo = 240;


    /**
     * Do not make an instance of this class! The implementation is such
     * that several classes may check the overall state of the program,
     * so there should only ever be just the class and its static variables
     * and methods around.
     * @deprecated
     */
    private StateMachine(){
    }

    /**
     * Get the current <code>State</code> of the <code>StateMachine</code>
     * @return The current <code>State</code>.
     */
    public static synchronized State getState() {
        return currentState;
    }

    /**
     * @param s Set the <code>StateMachine</code> to a certain State.
     */
    public static synchronized void setState(State s) {
        currentState = s;
    }

    /**
     * Sets the state back to "Editing" by default.
     */
    public static synchronized void resetState() {
        currentState = State.EDITING;
    }

    /**
     * @return The current time signature that we are running at.
     */
    public static synchronized TimeSignature getTimeSignature() {
        return currentTimeSignature;
    }

    /**
     * Sets the time signature to whatever that we give this method.
     * @param t The new time signature.
     */
    public static synchronized void setTimeSignature(TimeSignature t) {
        currentTimeSignature = t;
    }

    /** Sets the time signature back to "4/4" by default. */
    public static synchronized void resetTimeSignature() {
        currentTimeSignature = TimeSignature.FOUR_FOUR;
    }



    /**
     * @return The tempo that this program is running at.
     */
    public static synchronized double getTempo() {
        return tempo;
    }

    /**
     * Sets the tempo to what we give it here.
     * @param num The tempo we want to set the program to run at.
     * @return The current tempo.
     */
    public static synchronized void setTempo(double num) {
        tempo = num;
    }

    /**
     * Gets the current line number that we're on. Typically a value
     * between 0 and 383 for most files unless you've done fun stuff
     * and removed the 96-measure limit.
     * @return The current line number (left justify)
     */
    public static synchronized int getMeasureLineNum() {
        return currentLine;
    }

    /**
     * Sets the current line number to whatever is given to this method.
     * @param num The number that we're trying to set our current
     * line number to.
     */
    public static synchronized void setMeasureLineNum(int num) {
        currentLine = num;
    }

    /** @return Whatever key is pressed at the moment on the keyboard. */
    public static synchronized int getKeyPressed() {
        return 0;
    }

    /** Sets that we've pressed down the shift key. */
    public static void setShiftPressed() {
        buttonPresses = buttonPresses | 0b001;
    }

    /** @return Is shift pressed down? */
    public static boolean isShiftPressed() {
        return (buttonPresses & 0b001) == 0b001;
    }

    /** Sets that we've pressed down the alt key. */
    public static void setAltPressed() {
        buttonPresses = buttonPresses | 0b010;
    }

    /** @return Is alt pressed down? */
    public static boolean isAltPressed() {
        return (buttonPresses & 0b010) == 0b010;
    }

    /** Sets that we've pressed down the ctrl key. */
    public static void setCtrlPressed() {
        buttonPresses = buttonPresses | 0b100;
    }

    /** @return Is ctrl pressed down? */
    public static boolean isCtrlPressed() {
        return (buttonPresses & 0b100) == 0b100;
    }

    /** Sets that we've released the shift key. */
    public static void resetShiftPressed() {
        buttonPresses = buttonPresses ^ 0b001;
    }

    /** Sets that we've released the alt key. */
    public static void resetAltPressed() {
        buttonPresses = buttonPresses ^ 0b010;
    }

    /** Sets that we've released the ctrl key. */
    public static void resetCtrlPressed() {
        buttonPresses = buttonPresses ^ 0b100;
    }

    /**
     * @param stHandle This is the pane that we want to update the flats
     * or sharps.
     */
    public static void setFocusPane(StaffInstrumentEventHandler stHandle) {
        focusPane = stHandle;
    }

    /**
     * Forces an update of the current pane in focus.
     */
    public static void updateFocusPane() {
        if (focusPane != null)
            focusPane.updateAccidental();
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


}
