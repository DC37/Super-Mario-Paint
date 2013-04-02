package smp.stateMachine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Scanner;

/**
 * A loadable settings file that determines whatever is supposed
 * to display eventually on the screen.
 * @author RehdBlob
 * @since 2012.08.28
 */
public class Settings implements Serializable {

    /** The name of the file that we write to. */
    private final static String settingsFile = "settings.data";

    /**
     * Classic debug on/off.
     */
    public static boolean debug;


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
     * @param b True or false.
     */
    public static void setVerbose(boolean b) {
        debug = b;
    }

    /**
     * Saves the settings of the program.
     * @throws IOException If we can't write the object.
     */
    public static void save() throws IOException {
        Settings dummySettings = new Settings();
        FileOutputStream f_out = new
                FileOutputStream(settingsFile);
        PrintStream p = new PrintStream(f_out);
        p.println(advModeUnlocked);
        p.println(LIM_NOTESPERLINE);
        p.println(LIM_96_MEASURES);
        p.println(LIM_VOLUME_LINE);
        p.println(LIM_LOWA);
        p.println(LIM_HIGHD);
        p.println(LOW_A_ON);
        p.println(NEG_TEMPO_FUN);
        p.println(LIM_TEMPO_GAPS);
        p.println(RESIZE_WIN);
        p.println(ADV_MODE);
        p.close();
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
        Scanner file = new Scanner(f_in);
        advModeUnlocked = file.nextBoolean();
        file.nextLine();
        LIM_NOTESPERLINE = file.nextBoolean();
        file.nextLine();
        LIM_96_MEASURES = file.nextBoolean();
        file.nextLine();
        LIM_VOLUME_LINE = file.nextBoolean();
        file.nextLine();
        LIM_LOWA = file.nextBoolean();
        file.nextLine();
        LIM_HIGHD = file.nextBoolean();
        file.nextLine();
        LOW_A_ON = file.nextBoolean();
        file.nextLine();
        NEG_TEMPO_FUN = file.nextBoolean();
        file.nextLine();
        LIM_TEMPO_GAPS = file.nextBoolean();
        file.nextLine();
        RESIZE_WIN = file.nextBoolean();
        file.nextLine();
        ADV_MODE = file.nextBoolean();
        file.close();
        f_in.close();
    }

    /**
     * Sets the debug switch on or off.
     * @param b Whether we want debug mode to be on or not.
     */
    public static void setDebug(boolean b) {
        debug = b;
    }

    /** Turns debug mode on. */
    public static void setDebug() {
        debug = true;
    }

    /** Turns debug mode off. */
    public static void resetDebug() {
        debug = false;
    }
}

