package gui;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import backend.saving.Parser;
import backend.songs.StaffArrangement;
import backend.songs.StaffSequence;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Window;

/**
 * A somewhat useful utilities class for images and such. Not so much use right
 * now yet, but will gain some functionality eventually.
 *
 * @author RehdBlob
 * @since 2012.08.20
 */
public class Utilities {
    
    /**
     * <p>Get a file that's also a resource of the program (typically a default sprite or soundfont).</p>
     * 
     * <p>This method will attempt to load the file from its expected location. If the file is not found
     * there, it is loaded as a resource and then copied onto the expected location for future calls.</p>
     * 
     * @param filename name of the file or resource
     * @param dir expected location for the file
     */
    public static File getResourceFile(String filename, String dir, boolean forceCopy) throws IOException {
        File ret = new File(dir, filename);
        
        if (!forceCopy && ret.exists())
            return ret;
        
        URL url = Utilities.getResourceURL(filename);
        
        Files.createDirectories(ret.getParentFile().toPath());
        Files.copy(url.openStream(), ret.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        return ret;
    }
    
    public static File getResourceFile(String filename, String dir) throws IOException {
        return getResourceFile(filename, dir, false);
    }
    
    public static URL getResourceURL(String filename) {
        URL url = Utilities.class.getResource("/resources/" + filename);
        if (url == null)
            throw new NullPointerException("Cannot load resource: " + filename);
        
        return url;
    }
    
    /**
     * Find the window owning an event. Useful for events that trigger a popup window.
     * Returns {@code null} if the owner cannot be found.
     */
    public static Window getOwner(Event evt) {
        if (evt == null)
            return null;
        
        Object src = evt.getSource();
        if (!(src instanceof Node))
            return null;
        
        return ((Node) src).getScene().getWindow();
    }
	
	/**
	 * Creates the soundfont folder if it does not already exists.
	 */
	public static File getSoundfontFolder() throws IOException {
	    File dir = new File(Values.SOUNDFONTS_FOLDER);
	    Files.createDirectories(dir.toPath());
	    return dir;
	}
    
	/**
	 * @return The list of filenames *.sf2 in the soundfonts folder.
	 * @since v1.1.2
	 */
	public static String[] getSoundfontsList() throws IOException {
		File soundfontsFolder = getSoundfontFolder();
		
		return soundfontsFolder.list(new FilenameFilter() {
		    @Override
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".sf2");
		    }
		});
    }

	/**
	 * Creates a boolean array from a parsed long array, from a loaded file.
	 *
	 * @param parseLong
	 *            The long that we want to parse.
	 * @return A boolean array based on the long integer that we have loaded.
	 */
	public static boolean[] boolFromLong(long parseLong) {
		boolean[] loaded = new boolean[Values.NUMINSTRUMENTS];
		for (int i = 0; i < Values.NUMINSTRUMENTS; i++) {
			loaded[i] = ((1 << i) & parseLong) != 0 ? true : false;
		}
		return loaded;
	}

    /**
     * Creates a long integer from a parsed boolean array.
     *
     * @param parseBool
     *            The boolean array that we want to parse.
     * @return A long integer that is a bitfield that represents the boolean
     *         array.
     */
    public static long longFromBool(boolean[] parseBool) {
        long parsed = 0;
        for (int i = 0; i < parseBool.length; i++) {
            if (parseBool[i]) {
                parsed |= (1 << i);
            }
        }
        return parsed;
    }

    /**
     * Loads a sequence from an input file. Intended usage is in arranger mode.
     * We assume that the input files are located in a folder named "Songs" for
     * now.
     *
     * @param inputFile
     *            This is the input filename.
     */
    public static StaffSequence loadSequenceFromArrangement(File inputFile,
            Staff theStaff, SMPFXController controller, Window owner) {
        try {
            inputFile = new File(inputFile.getParent() + File.separatorChar
                    + inputFile.getName());
            if (!inputFile.exists()) {
                inputFile = new File(inputFile.getParent() + File.separatorChar
                        + inputFile.getName());
            }
            StaffSequence loaded = null;
            loaded = Parser.SEQUENCE_PARSER.parse(inputFile).orElseThrow(IOException::new);
            populateStaff(loaded, inputFile, false, theStaff, controller);
            return loaded;

        } catch (ClassCastException | EOFException | StreamCorruptedException
                | NullPointerException e) {
            Dialog.showDialog("Not a valid song file.", owner);
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Dialog.showDialog("Not a valid song file.", owner);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Dialog.showDialog("Not a valid song file.", owner);
            return null;
        }
    }

	/**
	 * Populates the staff with the sequence given. Sets the program's soundfont
	 * to the sequence's.
	 *
	 * @param loaded
	 *            The loaded sequence.
	 * @param inputFile
	 *            The input file.
	 * @param mpc
	 *            Whether this is an MPC file.
	 */
    public static String populateStaff(StaffSequence loaded, File inputFile,
            boolean mpc, Staff theStaff, SMPFXController controller) {
        loaded.normalize();
        theStaff.setSequence(loaded);
        theStaff.setTimeSignature(loaded.getTimeSignature());
        theStaff.setSequenceFile(inputFile);
        StateMachine.setTempo(loaded.getTempo());
        StateMachine.setMaxLine(loaded.getLength());
        theStaff.resetLocation();
        String fname = inputFile.getName();
        try {
            if (mpc)
                fname = fname.substring(0, fname.lastIndexOf(']'));
            else
                fname = fname.substring(0, fname.lastIndexOf("."));
        } catch (IndexOutOfBoundsException e) {
            // Do nothing
        }
        StateMachine.setCurrentSongName(fname);
        StateMachine.setNoteExtensions(loaded.getNoteExtensions());
        
        try {
			theStaff.getSoundPlayer().loadFromAppData(theStaff.getSequence().getSoundset());
		} catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
			e.printStackTrace();
		}
        
        controller.getModifySongManager().reset();
        
        return fname;
    }

    /**
     * Arrangement files might have absolute paths in them. Here's something
     * that will help a little bit with relative paths.
     *
     * @param loaded
     *            The loaded staff arrangement.
     * @param filePath
     *            The location in which this arrangement was found.
     * @throws IOException
     * @throws NullPointerException
     * @throws FileNotFoundException
     */
    public static void normalizeArrangement(StaffArrangement loaded,
            File filePath) throws FileNotFoundException, IOException {
        String basePath = filePath.getParent() + File.separatorChar;
        int sz1 = loaded.getTheSequenceFiles().size();
        int sz2 = loaded.getTheSequences().size();
        if (sz2 < sz1) {
            for (int i = 0; i < sz1 - sz2; i++) {
                loaded.getTheSequences().add(new StaffSequence());
            }
        }
        for (int i = 0; i < loaded.getTheSequenceFiles().size(); i++) {
            String fName = loaded.getTheSequenceNames().get(i);
            String newPath = basePath + fName + ".txt";
            File f2 = new File(newPath);
            if (!f2.exists()) {
                fName = fName + "]MarioPaint.txt";
                newPath = basePath + fName;
                loaded.getTheSequenceFiles().set(i, new File(newPath));
                loaded.getTheSequences().set(i, Parser.SEQUENCE_PARSER.parse(new File(newPath)).orElseThrow(IOException::new));
                continue;
            }
            loaded.getTheSequenceFiles().set(i, new File(newPath));
        }
    }
    
    /**
     * Turns out a lot of file names can have bad characters in them.
     * @param f String to clean
     * @return String with all apostrophes, backslashes, and forward
     * slashes replaced with underscores.
     */
    public static String cleanName(String f) {
        // unused---seems to only be causing problems
        // TODO consider normalizing all file names for saving and loading

        f = f.replaceAll("'", "_");
        f = f.replaceAll("\\\\", "_");
        f = f.replaceAll("/", "_");
        f = f.replaceAll(":", "_");
        f = f.replaceAll("\\&", "_");
        
        return f;
    }
    
    /**
     * Check if a string does not contain any illegal character.
     */
    public static boolean legalFileName(String s) {
        for (char c : s.toCharArray()) {
            if (!legalCharacter(c))
                return false;
        }
        return true;
    }
    
    private static boolean legalCharacter(char c) {
        switch (c) {
        case '<':
        case '>':
        case '/':
        case '\\':
        case ':':
        case '?':
        case '|':
        case '*':
        case '"':
        case '^':
            return false;
        }
        return true;
    }

	/**
	 * Populates the staff with the arrangement given. Loads each soundfont
	 * from each song in the arrangement into cache.
	 *
	 * @param loaded
	 *            The loaded arrangement.
	 * @param inputFile
	 *            The input file.
	 * @param mpc
	 *            Whether this is an MPC file.
	 */
    public static void populateStaffArrangement(StaffArrangement loaded,
            File inputFile, boolean mpc, Staff theStaff,
            final SMPFXController controller, Window owner) {
        File firstFile = loaded.getTheSequenceFiles().get(0);
        StaffSequence first = loadSequenceFromArrangement(firstFile, theStaff,
                controller, owner);
        String fname = inputFile.getName();
        boolean[] j = first.getNoteExtensions();
        controller.getNameTextField().setText(fname);
        StateMachine.setNoteExtensions(j);

        try {
            if (mpc)
                fname = fname.substring(0, fname.lastIndexOf(']'));
            else
                fname = fname.substring(0, fname.lastIndexOf("."));
        } catch (IndexOutOfBoundsException e) {
            // Do nothing
        }
        theStaff.setArrangement(loaded);
        StateMachine.setCurrentArrangementName(fname);
        theStaff.getArrangementList().getItems().clear();
        theStaff.getArrangementList().getItems()
                .addAll(loaded.getTheSequenceNames());

        ArrayList<File> files = loaded.getTheSequenceFiles();
        final ArrayList<StaffSequence> seq = new ArrayList<StaffSequence>();
        for (int i = 0; i < files.size(); i++) {
            try {
                seq.add(Parser.SEQUENCE_PARSER.parse(files.get(i)).orElseThrow(IOException::new));
            } catch (ClassCastException | EOFException
                    | StreamCorruptedException | NullPointerException e) {
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        theStaff.getArrangement().setTheSequences(seq);

        controller.getNameTextField().setText(fname);
        
        Task<Void> soundsetsTaskUtilities = new Task<Void>() {
        	@Override
            public Void call() {
        		theStaff.getSoundPlayer().clearCache();
        		for(StaffSequence s : seq) {
        			try {
						theStaff.getSoundPlayer().loadToCache(s.getSoundset());
					} catch (InvalidMidiDataException | IOException e) {
						e.printStackTrace();
					}
        		}
                return null;
            }
        };
        new Thread(soundsetsTaskUtilities).start();
        
        try {
			theStaff.getSoundPlayer().loadFromAppData(first.getSoundset());
		} catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
			e.printStackTrace();
		}
    }

}
