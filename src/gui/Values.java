package gui;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import backend.songs.NoteInfo;
import backend.songs.Pitch;
import backend.songs.TimeSignature;
import gui.components.buttons.old.SMPInstrumentButtonGroup;
import gui.resources.SMPResourceType;
import gui.resources.SMPResourceUtil;

/**
 * A "constants" file for holding things like default window size, default
 * modes, etc. These values may change over the operation of the program.
 *
 * @author RehdBlob
 * @since 2012.08.14
 */
public class Values {
    
    private Values() {}

    /** The program name. */
    public static final String PROGRAM_NAME = SMPResourceUtil.getProperty("gradle.bridge.properties", SMPResourceType.UNCATEGORIZED, "name");
    
    /** The current version number of the program. */
    public static final String VERSION = SMPResourceUtil.getProperty("gradle.bridge.properties", SMPResourceType.UNCATEGORIZED, "version");

    /** The number of instruments. */
    public static final int NUM_INSTRUMENTS = 31;

    /**
     * The largest value that a note velocity can be; a note played at this will
     * be played as loudly as possible.
     */
    public static final int MAX_VELOCITY = 127;

    /**
     * The median value that a note velocity can be. This should be the
     * half-volume level.
     */
    public static final int HALF_VELOCITY = 64;

    /**
     * This is the smallest value a note velocity can be.
     */
    public static final int ZERO_VELOCITY = 0;
    
    public static final TimeSignature DEFAULT_TIME_SIGNATURE = TimeSignature.FOUR_FOUR;

    /**
     * The smallest value that a note velocity can be; a note will basically be
     * silent if played at this.
     */
    public static final int MIN_VELOCITY = 0;

    /**
     * The default volume that we will be playing notes at. This can be changed
     * over the course of the use of this program.
     */
    private static int DEFAULT_VELOCITY = 96;

    /**
     * The number of distinct steps of notes in a note line on the staff. This
     * number is typically 18.
     */
    public static final int NOTES_IN_A_LINE = 29;
    
    /**
     * If there are more notes than this number at the same location on the screen,
     * excess notes will not be displayed.
     */
    public static final int MAX_STACKABLE_NOTES = 32;

    /** Location of the high C note. */
    public static final int HIGH_C = 28;

    /** Location of the high A note. */
    public static final int HIGH_A = 26;

    /** Location of the middle C note. */
    public static final int MIDDLE_C = 14;

    /** Location of the low C note. */
    public static final int LOW_C = 2;

    /** Location of the low A note. */
    public static final int LOW_A = 0;

    /**
     * The number of distinct lines of notes that exist on the staff. This
     * number is typically 10.
     */
    public static final int NOTELINES_IN_THE_WINDOW = 13;

    /**
     * The number of lines in the staff, by default. This number is typically
     * 400.
     */
    public static final int DEFAULT_LINES_PER_SONG = 400;

    /** The default speed. */
    public static final int DEFAULT_TEMPO = 400;
    
    /** Pitch to play when an instrument is selected */
    public static final int DEFAULT_NOTE = Pitch.DEFAULT.getValue();

    /**
     * The amount of time the mouse must be held before we start repeating
     * clicks.
     */
    public static final int HOLD_TIME = 250;

    /** The amount of time between the repetition of two mouse click events. */
    public static final int REPEAT_TIME = 40;

    /** This is the number of lines in a Mario Paint Composer song. */
    public static final int LINES_PER_MPC_SONG = 384;

    /** The number of tracks in the Mario Paint Composer song. */
    public static final int NUM_TRACKS = 19;

    /** The MIDI control channel for modulation. */
    public static final int MODULATION = 0x1;

    /** The MIDI control channel for volume. */
    public static final int VOLUME = 0x7;

    /** The MIDI control channel for pan. */
    public static final int PAN = 0xA;

    /** The MIDI control channel for sustain pedal. */
    public static final int SUSTAIN = 0x40;

    /** The MIDI control channel for reverb. */
    public static final int REVERB = 0x5B;

    /** The MIDI control channel for chorus. */
    public static final int CHORUS = 0x5D;

    /** The number of MIDI channels that exist. */
    public static final int MIDICHANNELS = 16;

    /** The channel that is reserved for drums in MIDI. */
    public static final int DRUMCHANNEL = 10;

    /** Array of notes that we can see on the staff. */
    private static final NoteInfo[] STAFF_NOTES = {
    		NoteInfo.of(36, "C2"), NoteInfo.of(38, "D2"), NoteInfo.of(40, "E2"),
        	NoteInfo.of(41, "F2"), NoteInfo.of(43, "G2"), NoteInfo.of(45, "A2"),
        	NoteInfo.of(47, "B2"), NoteInfo.of(48, "C3"), NoteInfo.of(50, "D3"),
        	NoteInfo.of(52, "E3"), NoteInfo.of(53, "F3"), NoteInfo.of(55, "G3"),
        	NoteInfo.of(57, "A3"), NoteInfo.of(59, "B3"), NoteInfo.of(60, "C4"),
        	NoteInfo.of(62, "D4"), NoteInfo.of(64, "E4"), NoteInfo.of(65, "F4"),
        	NoteInfo.of(67, "G4"), NoteInfo.of(69, "A4"), NoteInfo.of(71, "B4"),
        	NoteInfo.of(72, "C5"), NoteInfo.of(74, "D5"), NoteInfo.of(76, "E5"),
        	NoteInfo.of(77, "F5"), NoteInfo.of(79, "G5"), NoteInfo.of(81, "A5"),
        	NoteInfo.of(83, "B5"), NoteInfo.of(84, "C6")
    };
    
    /**
     * The max undo/redo stack size for recorded commands.
     * 
     * @since v1.1.1
     */
    public static final int MAX_UNDO_REDO_SIZE = 1000;
    
    /** The list of illegal characters in a sequence name. */
    private static final Character[] ILLEGAL_CHARS = new Character[] {
    		'<', '>', '/', '\\', ':', '?', '|', '*', '"', '^'
    };

    /**
     * This works in lieu of preprocessor directives to define the
     * platform-specific folder for AppData. Use like this: 
     * 'new PlatformDependency().getPlatformFolder()'
     * 
     * @author j574y923
     * @since v1.1.2
     */
    private static class PlatformDependency {

        /** the platform specific folder for appdata */
        private String platformFolder;
        
        /**
         * Instantiates the platformFolder. Uses if-else statements because
         * preprocessor directives are non-existent in Java.
         */
        public PlatformDependency() {

            /* cross-platform solution taken from https://stackoverflow.com/a/16660314/9363442 */
            String os = (System.getProperty("os.name")).toUpperCase();
            /* if it is some version of Windows */
            if (os.contains("WIN")) {
                // it is simply the location of the "AppData" folder
            	platformFolder = preparePath(
            			System.getenv("AppData"), "Super Mario Paint");
            }
            /* Otherwise, we assume Linux or Mac */
            else if (os.contains("MAC")) {
            	platformFolder = preparePath(
            			System.getProperty("user.home"),
            			"Library", "Application Support", "Super Mario Paint");
            } else {
                /* Assuming we are on Linux */
            	platformFolder = preparePath(
            			System.getProperty("user.home"), ".supermariopaint");
            }
        }
        
        /**
         * Convenience method to build a path given its parts.
         * 
         * @param parts One or more paths to join together.
         * @return The joined path as a {@link String}.
         */
        private String preparePath(String... parts) {
        	return String.join(File.separator, parts);
        }
        
        /**
         * @return the platform specific folder for appdata
         */
        public String getPlatformFolder() {
            return platformFolder;
        }
    }
    
    public static final String SMP_FOLDER = new PlatformDependency().getPlatformFolder();
    
    public static final String FXML = "MainWindow.fxml";
    
    /**
     * This is where we store soundfonts.
     */
    public static final String SOUNDFONTS_FOLDER =
            SMP_FOLDER + File.separatorChar + "SoundFonts" + File.separatorChar;
    
    public static final String DEFAULT_SOUNDFONT = "soundset3.sf2";
    
    /**
     * Sprites for the default theme are stored here. Replacing them will change the default
     * appearance of the program.
     */
    public static final String SPRITES_FOLDER =
            SMP_FOLDER + File.separatorChar + "sprites";
    
    // Synchronize animations for all buttons in this group
    public static final SMPInstrumentButtonGroup INSTRUMENT_BTNS_GROUP = new SMPInstrumentButtonGroup();
        
    /**
     * Gets the current default volume value.
     * 
     * @return An {@code int} representing the current default volume.
     */
    public static int getDefaultVolume() {
    	return Values.DEFAULT_VELOCITY;
    }
    
    /**
     * Sets the current default volume value.
     * 
     * @param vol The volume amount to set as default.
     */
    public static void setDefaultVolume(int vol) {
    	Values.DEFAULT_VELOCITY = vol;
    }
    
    /**
     * Get a {@link Pitch} note given its vertical staff bar position,
     * optionally altered by an accidental offset. 
     * 
     * @param verticalPosition The vertical staff bar position of the base note.
     * @param accidentalOffset Amount of offset to add due to accidentals.
     * @return The {@link Pitch} representing the obtained note.
     */
    public static Pitch getNotePitch(int verticalPosition, int accidentalOffset) {
    	return Pitch.valueOf(Values.STAFF_NOTES[verticalPosition].getValue() + accidentalOffset);
    }
    
    /**
     * Get the name associated to the note
     * in the vertical staff bar position.
     * 
     * @param verticalPosition The vertical staff bar position of the note.
     * @return The name of the note.
     */
    public static String getNoteName(int verticalPosition) {
    	return Values.STAFF_NOTES[verticalPosition].getName();
    }
    
    /**
     * Get the staff notes as a {@link List}.
     * 
     * @return The staff notes described as a
     *         {@link List} of {@link NodeInfo} objects.
     */
    public static List<NoteInfo> getNotes() {
    	return Arrays.asList(Values.STAFF_NOTES);
    }
    
    /**
     * Get the list of illegal characters in a sequence name.
     * 
     * @return The list of characters. This list cannot be modified.
     */
    public static List<Character> getIllegalChars() {
    	return Arrays.asList(ILLEGAL_CHARS).stream().toList();
    }
    
}
