package smp.components.general;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.concurrent.Task;
import smp.components.Values;
import smp.components.staff.Staff;
import smp.components.staff.sequences.StaffArrangement;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.staff.sequences.StaffSequence;
import smp.components.staff.sequences.mpc.MPCDecoder;
import smp.fx.Dialog;
import smp.fx.SMPFXController;
import smp.stateMachine.StateMachine;

/**
 * A somewhat useful utilities class for images and such. Not so much use right
 * now yet, but will gain some functionality eventually.
 *
 * @author RehdBlob
 * @since 2012.08.20
 */
public class Utilities {

    /**
     * From StackOverflow - "PhiLo" and "corgrath" made this.
     *
     * @param im
     *            An image to have its background be made transparent.
     * @param color
     *            The color t o be made transparent.
     * @return A BufferedImage that now has all of <code>color</code> removed.
     */
    public static BufferedImage makeColorTransparent(BufferedImage im,
            final Color color) {
        ImageFilter filter = new RGBImageFilter() {

             /* the color we are looking for... Alpha bits are set to opaque */
            public int markerRGB = color.getRGB() | 0xFF000000;

            @Override
            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    /* Mark the alpha bits as zero - transparent */
                    return 0x00FFFFFF & rgb;
                } else {
                    /* nothing to do */
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return imageToBufferedImage(Toolkit.getDefaultToolkit().createImage(ip));
    }

    /**
     * From StackOverflow - "PhiLo" and "corgrath" made this.
     *
     * @param image
     *            An Image to be converted to a BufferedImage
     * @return A BufferedImage from an Image.
     */
    private static BufferedImage imageToBufferedImage(Image image) {

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        /*
         * Graphics2D g2 = bufferedImage.createGraphics(); g2.drawImage(image,
         * 0, 0, null); g2.dispose();
         */

        return bufferedImage;

    }

    /**
     * Opens a file dialog that people can choose a file from. This is a Java
     * Swing version.
     *
     * @return A File reference that the user has chosen.
     * @throws IOException
     */
    public static File openFileDialog() throws IOException,
            NullPointerException {
        JFileChooser fd = new JFileChooser(".");
        FileFilter filter = new FileNameExtensionFilter("Text File (*.txt)",
                "txt");
        FileFilter filter2 = new FileNameExtensionFilter(
                "AMS Sequence (*.mss)", "mss");
        fd.addChoosableFileFilter(filter);
        fd.addChoosableFileFilter(filter2);
        fd.setFileFilter(filter);
        int returnVal = fd.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fd.getSelectedFile();
        } else if (returnVal == JFileChooser.CANCEL_OPTION) {
            throw new NullPointerException();
        } else {
            System.out.print("Error opening file!");
            throw new IOException();
        }
    }

    /**
     * Opens some dialog box that contains some text in it. Whether this is done
     * in Swing or JavaFX is TBD.
     *
     * @param in
     *            What should be shown in this dialog box.
     */
    public static void openDialog(String in) {
        JOptionPane.showMessageDialog(null, in, "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Makes a sequence fit on the screen, and also trims excessive whitespace
     * at the end of a file.
     *
     * @param theSeq
     *            The sequence to normalize.
     */
    public static void normalize(StaffSequence theSeq) {
        ArrayList<StaffNoteLine> theLines = theSeq.getTheLines();
        int last = theLines.size() - 1;
        for (int i = theLines.size() - 1; i >= 0; i--) {
            if (i < Values.DEFAULT_LINES_PER_SONG)
                break;
            else if (!theLines.get(i).isEmpty()) {
                last = i;
                break;
            }
        }
        last = theLines.size() - last;
        for (int i = 0; i < last - 1; i++)
            theLines.remove(theLines.size() - 1);
        while (theLines.size() % 4 != 0
                || theLines.size() % Values.NOTELINES_IN_THE_WINDOW != 0) {
            theLines.add(new StaffNoteLine());
        }

    }

    /**
     * Loads a song from the file specified.
     *
     * @param inputFile
     *            The file to load from.
     * @return A loaded song file. The format is a StaffSequence.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static StaffSequence loadSong(File inputFile)
            throws FileNotFoundException, IOException, NullPointerException {
        FileInputStream f_in = new FileInputStream(inputFile);
        StaffSequence loaded = null;
        try {
        loaded = MPCDecoder.decode(inputFile);
        } catch (ParseException e1) {
            try {
                // Decode as object
                ObjectInputStream o_in = new ObjectInputStream(f_in);
                loaded = (StaffSequence) o_in.readObject();
                o_in.close();
            } catch (ClassNotFoundException | ClassCastException | EOFException
                    | StreamCorruptedException e) {
                // If it's not an object, try using the human-readable option.
                f_in.close();
                f_in = new FileInputStream(inputFile);
                Scanner sc = new Scanner(f_in);
                ArrayList<String> read = new ArrayList<String>();
                while (sc.hasNext()) {
                    read.add(sc.nextLine());
                }
                sc.close();
                loaded = parseText(read);
            }
        }
        f_in.close();
        if (loaded == null) {
            throw new NullPointerException();
        }
        return loaded;
    }

    /**
     * Parses a bunch of text from a save file and makes a
     * <code>StaffSequence</code> out of it.
     *
     * @param read
     *            <code>ArrayList</code> of notes and parameters.
     * @return Hopefully, a decoded <code>StaffSequence</code>
     */
    private static StaffSequence parseText(ArrayList<String> read) {
        StaffSequence loaded = new StaffSequence();
        for (String s : read) {
            if (s.contains("TEMPO") || s.contains("EXT") || s.contains("TIME") || s.contains("SOUNDSET")) {
                String[] sp = s.split(",");
                for (String spl : sp) {
                    String num = spl.substring(spl.indexOf(":") + 1);
                    if (spl.contains("TEMPO")) {
                        loaded.setTempo(Double.parseDouble(num.trim()));
                    } else if (spl.contains("EXT")) {
                        loaded.setNoteExtensions(Utilities.boolFromLong(Long
                                .parseLong(num.trim())));
                    } else if (spl.contains("TIME")) {
                        loaded.setTimeSignature(num.trim());
                    } else if (spl.contains("SOUNDSET")) {
                        loaded.setSoundset(num.trim());
                    }
                }
            } else {
                String[] sp = s.split(",");
                int lineNum = 0;
                StaffNoteLine st = new StaffNoteLine();
                for (String spl : sp) {
                    if (spl.contains(":") && !spl.contains("VOL")) {
                        String[] meas = spl.split(":");
                        if (meas.length != 2) {
                            continue;
                        }
                        lineNum = (Integer.parseInt(meas[0]) - 1)
                                * loaded.getTimeSignature().top()
                                + Integer.parseInt(meas[1]);
                    } else {
                        if (spl.contains("VOL")) {
                            st.setVolume(Double.parseDouble(spl.substring(
                                    spl.indexOf(":") + 1).trim()));
                        } else {
                            try {
                                st.add(new StaffNote(spl));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (loaded.getTheLines().size() < lineNum + 1) {
                    int add = lineNum - loaded.getTheLines().size();
                    for (int i = 0; i < add; i++) {
                        loaded.addLine(new StaffNoteLine());
                    }
                }
                if (lineNum >= loaded.getTheLines().size()) {
                    loaded.addLine(st);
                } else {
                    loaded.setLine(lineNum, st);
                }
            }
        }
        if (loaded.getTheLines().size() % 10 != 0) {
            do {
                loaded.addLine(new StaffNoteLine());
            } while (loaded.getTheLines().size() % 10 != 0);
        }
        return loaded;
    }

    /**
     * Creates a boolean array from a parsed long array, from a loaded file.
     *
     * @param parseLong
     *            The long that we want to parse.
     * @return A boolean array based on the long integer that we have loaded.
     */
    private static boolean[] boolFromLong(long parseLong) {
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
     * Parses a bunch of text from a save file and makes a
     * <code>StaffArrangement</code> out of it.
     *
     * @param read
     *            <code>ArrayList</code> of filenames and paths.
     * @return Hopefully, a decoded <code>StaffArrangement</code>
     */
    private static StaffArrangement parseArrText(ArrayList<String> read) {
        StaffArrangement loaded = new StaffArrangement();
        File f = null;
        ArrayList<File> files = new ArrayList<File>();
        ArrayList<String> names = new ArrayList<String>();
        for (String s : read) {
            names.add(s);
            f = new File(s + ".txt");
            files.add(f);
        }
        loaded.setTheSequenceNames(names);
        loaded.setTheSequenceFiles(files);
        return loaded;
    }

    /**
     * Loads an arrangement from the file specified.
     *
     * @param inputFile
     *            The file to load from.
     * @return A loaded arrangement file. The format is StaffArrangement.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static StaffArrangement loadArrangement(File inputFile)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream f_in = new FileInputStream(inputFile);
        StaffArrangement loaded = null;
        try {
            ObjectInputStream o_in = new ObjectInputStream(f_in);
            loaded = (StaffArrangement) o_in.readObject();
            o_in.close();
        } catch (ClassNotFoundException | ClassCastException | EOFException
                | StreamCorruptedException e) {
            // If it's not an object, try using the human-readable option.
            f_in.close();
            f_in = new FileInputStream(inputFile);
            Scanner sc = new Scanner(f_in);
            ArrayList<String> read = new ArrayList<String>();
            while (sc.hasNext()) {
                read.add(sc.nextLine());
            }
            sc.close();
            loaded = parseArrText(read);
        }
        f_in.close();
        return loaded;
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
            Staff theStaff, SMPFXController controller) {
        try {
            inputFile = new File(inputFile.getParent() + "\\"
                    + inputFile.getName());
            if (!inputFile.exists()) {
                inputFile = new File(inputFile.getParent() + "\\"
                        + inputFile.getName());
            }
            StaffSequence loaded = null;
            try {
                loaded = MPCDecoder.decode(inputFile);
            } catch (ParseException e1) {
                loaded = Utilities.loadSong(inputFile);
            }
            if (loaded == null) {
                throw new IOException();
            }
            populateStaff(loaded, inputFile, false, theStaff, controller);
            return loaded;

        } catch (ClassCastException | EOFException | StreamCorruptedException
                | NullPointerException e) {
            Dialog.showDialog("Not a valid song file.");
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Dialog.showDialog("Not a valid song file.");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Dialog.showDialog("Not a valid song file.");
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
        Utilities.normalize(loaded);
        theStaff.setSequence(loaded);
        theStaff.setSequenceFile(inputFile);
        StateMachine.setTempo(loaded.getTempo());
        theStaff.updateCurrTempo();
        theStaff.getControlPanel()
                .getScrollbar()
                .setMax(loaded.getTheLines().size()
                        - Values.NOTELINES_IN_THE_WINDOW);
        theStaff.setLocation(0);
        theStaff.getNoteMatrix().redraw();
        String fname = inputFile.getName();
        try {
            if (mpc)
                fname = fname.substring(0, fname.lastIndexOf(']'));
            else
                fname = fname.substring(0, fname.lastIndexOf("."));
        } catch (IndexOutOfBoundsException e) {
            // Do nothing
        }
        theStaff.setSequenceName(fname);
        StateMachine.setNoteExtensions(loaded.getNoteExtensions());
        controller.getInstBLine().updateNoteExtensions();
        
        try {
			controller.getSoundfontLoader().loadFromAppData(theStaff.getSequence().getSoundset());
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
            File filePath) throws FileNotFoundException, NullPointerException,
            IOException {
        String basePath = filePath.getParent() + "\\";
        int sz1 = loaded.getTheSequenceFiles().size();
        int sz2 = loaded.getTheSequences().size();
        if (sz2 < sz1) {
            for (int i = 0; i < sz1 - sz2; i++) {
                loaded.getTheSequences().add(new StaffSequence());
            }
        }
        for (int i = 0; i < loaded.getTheSequenceFiles().size(); i++) {
            String fName = cleanName(loaded.getTheSequenceNames().get(i));
            String newPath = basePath + fName + ".txt";
            File f2 = new File(newPath);
            if (!f2.exists()) {
                fName = fName + "]MarioPaint.txt";
                newPath = basePath + fName;
                loaded.getTheSequenceFiles().set(i, new File(newPath));
                loaded.getTheSequences().set(i, loadSong(new File(newPath)));
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

        f = f.replaceAll("'", "_");
        f = f.replaceAll("\\\\", "_");
        f = f.replaceAll("/", "_");
        f = f.replaceAll(":", "_");
        f = f.replaceAll("\\&", "_");
        
        return f;
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
            final SMPFXController controller) {
        File firstFile = loaded.getTheSequenceFiles().get(0);
        StaffSequence first = loadSequenceFromArrangement(firstFile, theStaff,
                controller);
        String fname = inputFile.getName();
        boolean[] j = first.getNoteExtensions();
        controller.getNameTextField().setText(fname);
        StateMachine.setNoteExtensions(j);
        controller.getInstBLine().updateNoteExtensions();

        try {
            if (mpc)
                fname = fname.substring(0, fname.lastIndexOf(']'));
            else
                fname = fname.substring(0, fname.lastIndexOf("."));
        } catch (IndexOutOfBoundsException e) {
            // Do nothing
        }
        theStaff.setArrangement(loaded);
        theStaff.setArrangementName(fname);
        theStaff.getArrangementList().getItems().clear();
        theStaff.getArrangementList().getItems()
                .addAll(loaded.getTheSequenceNames());

        ArrayList<File> files = loaded.getTheSequenceFiles();
        final ArrayList<StaffSequence> seq = new ArrayList<StaffSequence>();
        for (int i = 0; i < files.size(); i++) {
            try {
                seq.add(Utilities.loadSong(files.get(i)));
            } catch (ClassCastException | EOFException
                    | StreamCorruptedException | NullPointerException e) {
                try {
                    seq.add(MPCDecoder.decode(files.get(i)));
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return;
                }
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
        		controller.getSoundfontLoader().clearCache();
        		for(StaffSequence s : seq) {
        			try {
						controller.getSoundfontLoader().loadToCache(s.getSoundset());
					} catch (InvalidMidiDataException | IOException e) {
						e.printStackTrace();
					}
        		}
                return null;
            }
        };
        new Thread(soundsetsTaskUtilities).start();
        
        try {
			controller.getSoundfontLoader().loadFromAppData(first.getSoundset());
		} catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
			e.printStackTrace();
		}
    }

}
