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
public class Settings implements Serializable {

    /**
     * Generated serial ID.
     */
    private static final long serialVersionUID = -7243683035937693416L;


    /**
     * Classic debug on/off.
     */
    public boolean debug = true;


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
     * Limit the number of notes per line to 5.
     */
    public boolean LIM_NOTESPERLINE = true;

    /**
     * Limit the number of measures to 96.
     */
    public boolean LIM_96_MEASURES = true;

    /**
     * Limit the number of volume control lines to
     * 1 per line.
     */
    public boolean LIM_VOLUME_LINE = true;

    /**
     * Enable or disable the Low A note as a usable
     * note. Default is disabled.
     */
    public boolean LIM_LOWA = true;

    /**
     * Enable or disable the High D note as a usable
     * note. Default is disabled.
     */
    public boolean LIM_HIGHD = true;

    /**
     * Turn on the Low A Glitch or not.
     */
    public boolean LOW_A_ON = true;

    /**
     * Cause fun things to happen when negative tempo is
     * entered. Otherwise, the tempo will be set to ludicrous
     * speeds if this is not enabled.
     */
    public boolean NEG_TEMPO_FUN = false;

    /**
     * Turn tempo gaps on or off. Most likely people won't want this
     * and maybe it'll be removed.
     */
    public boolean LIM_TEMPO_GAPS = false;

    /**
     * Be able to resize the window to get to the Secret Button.
     */
    public boolean RESIZE_WIN = false;

    /**
     * Advanced mode. All of these options don't matter when this is pressed
     * because a completely different interface is loaded when this is set to
     * true...
     */
    public boolean ADV_MODE = false;




    /**
     * Sets whether we want to see debug mode or not.
     * @param b True or false.
     */
    public void setVerbose(boolean b) {
        debug = b;
    }
}