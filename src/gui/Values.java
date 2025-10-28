package gui;

import java.io.File;

import backend.songs.Note;
import backend.songs.TimeSignature;

/**
 * A "constants" file for holding things like default window size, default
 * modes, etc. These values may change over the operation of the program.
 *
 * @author RehdBlob
 * @since 2012.08.14
 */
public class Values {

    /** The current version number of the program. */
    public static double VERSION = 0.90;

    /** The number of instruments. */
    public static int NUMINSTRUMENTS = 31;

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
    public static int MIN_VELOCITY = 0;

    /**
     * The default volume that we will be playing notes at. This can be changed
     * over the course of the use of this program.
     */
    public static int DEFAULT_VELOCITY = 96;

    /**
     * The number of distinct steps of notes in a note line on the staff. This
     * number is typically 18.
     */
    public static int NOTES_IN_A_LINE = 29;
    
    /**
     * If there are more notes than this number at the same location on the screen,
     * excess notes will not be displayed.
     */
    public static int MAX_STACKABLE_NOTES = 32;

    /** Location of the high C note. */
    public static int highC = 28;

    /** Location of the high A note. */
    public static int highA = 26;

    /** Location of the middle C note. */
    public static int middleC = 14;

    /** Location of the low C note. */
    public static int lowC = 2;

    /** Location of the low A note. */
    public static int lowA = 0;

    /**
     * The number of distinct lines of notes that exist on the staff. This
     * number is typically 10.
     */
    public static int NOTELINES_IN_THE_WINDOW = 13;

    /**
     * The number of lines in the staff, by default. This number is typically
     * 400.
     */
    public static int DEFAULT_LINES_PER_SONG = 400;

    /** The default speed. */
    public static int DEFAULT_TEMPO = 400;
    
    /** Pitch to play when an instrument is selected */
    public static int DEFAULT_NOTE = Note.A3.getKeyNum();

    /**
     * The amount of time the mouse must be held before we start repeating
     * clicks.
     */
    public static int HOLDTIME = 250;

    /** The amount of time between the repetition of two mouse click events. */
    public static int REPEATTIME = 40;

    /**
     * This is the default song folder that we will check for arrangement song
     * files.
     * We're not using this anymore, since we are better than hardcoding
     * specific directories.
     * @deprecated
     */
    public static String SONGFOLDER = "./Prefs/";
    
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
    public static final Note[] staffNotes = { Note.C2, Note.D2, Note.E2, Note.F2, Note.G2, Note.A2, Note.B2, Note.C3,
            Note.D3, Note.E3, Note.F3, Note.G3, Note.A3, Note.B3, Note.C4,
            Note.D4, Note.E4, Note.F4, Note.G4, Note.A4, Note.B4, Note.C5,
            Note.D5, Note.E5, Note.F5, Note.G5, Note.A5, Note.B5, Note.C6 };
    
	/**
	 * The max undo/redo stack size for recorded commands.
	 * 
	 * @since v1.1.1
	 */
	public static final int MAX_UNDO_REDO_SIZE = 1000;

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
				platformFolder = System.getenv("AppData")
				        + File.separatorChar + "Super Mario Paint";
			}
            /* Otherwise, we assume Linux or Mac */
			else if (os.contains("MAC")) {
			    platformFolder = System.getProperty("user.home")
			            + File.separatorChar + "Library"
			            + File.separatorChar + "Application Support"
			            + File.separatorChar + "Super Mario Paint";
			} else {
			    /* Assuming we are on Linux */
			    platformFolder = System.getProperty("user.home")
			            + File.separatorChar + ".supermariopaint";
			}
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
}
