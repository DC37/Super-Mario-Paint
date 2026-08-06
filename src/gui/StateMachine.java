package gui;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
    
    /** Set of currently-pressed buttons. */
    private static Set<KeyCode> buttonsPressed =
            Collections.synchronizedSet(new HashSet<>());

    /**
     * The current soundset name. This should change when a new soundfont is
     * loaded.
     */
    private static String currentSoundset = Values.DEFAULT_SOUNDFONT;
    
    /**
     * Do not make an instance of this class! The implementation is such that
     * several classes may check the overall state of the program, so there
     * should only ever be just the class and its static variables and methods
     * around.
     */
    private StateMachine() {}
    
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

}
