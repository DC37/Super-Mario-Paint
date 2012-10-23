package smp.stateMachine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
     * Sets whether we want to see debug mode or not.
     * @param b True or false.
     */
    public static void setVerbose(boolean b) {
        debug = b;
    }
}