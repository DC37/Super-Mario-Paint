package smp.stateMachine;

import java.io.Serializable;

/**
 * A loadable settings file that determines whatever is supposed
 * to display eventually on the screen.
 * @author RehdBlob
 * @since 2012.08.28
 */
@SuppressWarnings("unused")
public class Settings implements Serializable {

    /**
     * Generated serial ID.
     */
    private static final long serialVersionUID = -7243683035937693416L;


    /**
     * Classic debug on/off.
     */
    public static boolean debug = true;


    /**
     * If Advanced mode is unlocked, this is set to <b>true</b>
     */
    private boolean advModeUnlocked;



    /**
     * Initializes the settings that are currently stored in the program.
     * Unpacks the object from a serialized version.
     */
    public Settings() {

    }


    /**
     * 
     */
    private void setOptions() {

    }

    /**
     * Loads the settings from some file.
     */
    private void load() {

    }

    /**
     * Saves the settings into some file.
     */
    private void save() {

    }

    /**
     * Sets whether we want to see debug mode or not.
     * @param b True or false.
     */
    public static void setVerbose(boolean b) {
        debug = b;
    }
}