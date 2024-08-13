package smp.stateMachine;

import java.io.FileInputStream;
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
public class Settings {

    /** The current version number of this program. */
    public final static String version = "v1.4.4";

    /** The name of the file that we write to. */
    private final static String settingsFile = "settings.data";

    /** Change this to true for old functionality of serializing objects. */
    public final static boolean SAVE_OBJECTS = false;

    /**
     * Classic debug on/off. 32-bit field
     * 2^05 - Print arranger actions (add, delete, move) 
     * 2^04 - Print staff value every time it changes.
     * 2^03 -
     * 2^02 -
     * 2^01 - Print sound output of instrument line
     * 2^00 - Print all output of loaders
     * 0    - No further verbose debug output
     * Set to -1 (or anything < 0) for max debug.
     */
    public static int debug = 0;

    /**
     * If Advanced mode is unlocked, this is set to <b>true</b>
     */
    private static boolean advModeUnlocked = false;


    /**
     * Limit the number of notes per line to 5.
     */
    public static boolean LIM_NOTESPERLINE = true;

    /**
     * Limit the number of measures to 96.
     */
    public static boolean LIM_96_MEASURES = true;

    /**
     * Limit the number of volume control lines to
     * 1 per line.
     */
    public static boolean LIM_VOLUME_LINE = true;

    /**
     * Enable or disable the Low A note as a usable
     * note. Default is disabled.
     */
    public static boolean LIM_LOWA = true;

    /**
     * Enable or disable the High D note as a usable
     * note. Default is disabled.
     */
    public static boolean LIM_HIGHD = true;

    /**
     * Turn on the Low A Glitch or not.
     */
    public static boolean LOW_A_ON = true;

    /**
     * Cause fun things to happen when negative tempo is
     * entered. Otherwise, the tempo will be set to ludicrous
     * speeds if this is not enabled.
     */
    public static boolean NEG_TEMPO_FUN = false;

    /**
     * Turn tempo gaps on or off. Most likely people won't want this
     * and maybe it'll be removed.
     */
    public static boolean LIM_TEMPO_GAPS = false;

    /**
     * Be able to resize the window to get to the Secret Button.
     */
    public static boolean RESIZE_WIN = false;

    /**
     * Advanced mode. All of these options don't matter when this is pressed
     * because a completely different interface is loaded when this is set to
     * true...
     */
    public static boolean ADV_MODE = false;

    /**
     * Sets whether we want to see debug mode or not.
     * @param b Debug level.
     */
    public static void setVerbose(int b) {
        debug = b;
    }

    /** Used to save settings via object serialization. */
    private static class SettingsSaver implements Serializable {

        /**
         * Generated Serial ID.
         */
        private static final long serialVersionUID = -3844082395768911493L;

        /** The number of setting types that we have. */
        private transient final int NUM_SETTINGS = 11;

        /** The array of settings that we are going to initialize. */
        public boolean[] set = new boolean[NUM_SETTINGS];

        /** Initializes all of the fields in here. */
        public SettingsSaver() {
            set[0] = advModeUnlocked;
            set[1] = LIM_NOTESPERLINE;
            set[2] = LIM_96_MEASURES;
            set[3] = LIM_VOLUME_LINE;
            set[4] = LIM_LOWA;
            set[5] = LIM_HIGHD;
            set[6] = LOW_A_ON;
            set[7] = NEG_TEMPO_FUN;
            set[8] = LIM_TEMPO_GAPS;
            set[9] = RESIZE_WIN;
            set[10] = ADV_MODE;
        }

    }

    /**
     * Saves the settings of the program.
     * @throws IOException If we can't write the object.
     */
    public static void save() throws IOException {
        FileOutputStream f_out = new
                FileOutputStream(settingsFile);
        ObjectOutputStream o_out = new
                ObjectOutputStream(f_out);
        SettingsSaver s = new SettingsSaver();
        o_out.writeObject(s);
        o_out.close();
        f_out.close();
    }

    /**
     * Unserializes this Settings file.
     * @throws IOException If for some reason we can't load
     * the settings.
     * @throws ClassNotFoundException If for some reason we can't find
     * the settings file file.
     */
    public static void load()
            throws IOException, ClassNotFoundException {
        FileInputStream f_in = new
                FileInputStream (settingsFile);
        ObjectInputStream o_in = new
                ObjectInputStream(f_in);
        SettingsSaver loaded = (SettingsSaver) o_in.readObject();
        advModeUnlocked  = loaded.set[0];
        LIM_NOTESPERLINE = loaded.set[1];
        LIM_96_MEASURES  = loaded.set[2];
        LIM_VOLUME_LINE  = loaded.set[3];
        LIM_LOWA         = loaded.set[4];
        LIM_HIGHD        = loaded.set[5];
        LOW_A_ON         = loaded.set[6];
        NEG_TEMPO_FUN    = loaded.set[7];
        LIM_TEMPO_GAPS   = loaded.set[8];
        RESIZE_WIN       = loaded.set[9];
        ADV_MODE         = loaded.set[10];
        o_in.close();
        f_in.close();
    }

    /**
     * Sets the debug switch on or off.
     * @param b Debug level.
     */
    public static void setDebug(int b) {
        debug = b;
    }


}

