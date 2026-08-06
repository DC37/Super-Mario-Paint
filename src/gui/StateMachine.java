package gui;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    
    /** This tells us whether we have modified the song or not. */
    private static boolean modifiedSong = false;

    /** This tells us whether we have modified the arrangement or not. */
    private static boolean modifiedArr = false;

    /** The list of values denoting which notes are filtered. */
    private static IntegerProperty filteredNotes = new SimpleIntegerProperty(-1);
    
    /**
     * The file directory that we are currently located in. We'll start in the
     * user directory.
     */
    private static File currentDirectory = new File(System.getProperty("user.dir"));

    /** Set of currently-pressed buttons. */
    private static Set<KeyCode> buttonsPressed =
            Collections.synchronizedSet(new HashSet<KeyCode>());

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
     */
    private StateMachine() {}
    
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
    
    public static IntegerProperty filteredNotesProperty() {
        return filteredNotes;
    }
    
    /**
     * Set specific bit
     * @param idx index of the bit to modify, is in interval [0, 31]
     * @param b value to set
     */
    public static void setFilteredNote(int idx, boolean b) {
        int v = filteredNotes.get();
        int m = 1 << idx;
        v = b ? v | m : v & (~m);
        filteredNotes.set(v);
    }

    public static void setFilteredNotes(int v) {
        filteredNotes.set(v);
    }
    
    /**
     * Get specific bit
     * @param idx index of the bit to read, is in interval [0, 31]
     */
    public static boolean getFilteredNote(int idx) {
        int m = 1 << idx;
        int v = filteredNotes.get();
        return (v & m) != 0;
    }

    public static int getFilteredNotes() {
        return filteredNotes.get();
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
