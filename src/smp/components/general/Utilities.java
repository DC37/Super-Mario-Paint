package smp.components.general;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import smp.components.Values;
import smp.components.staff.Staff;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.staff.sequences.StaffSequence;
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
     *            The color to be made transparent.
     * @return A BufferedImage that now has all of <code>color</code> removed.
     */
    public static BufferedImage makeColorTransparent(BufferedImage im,
            final Color color) {
        ImageFilter filter = new RGBImageFilter() {

            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;

            @Override
            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
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
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static StaffSequence loadSong(File inputFile)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream f_in = new FileInputStream(inputFile);
        ObjectInputStream o_in = new ObjectInputStream(f_in);
        StaffSequence loaded = (StaffSequence) o_in.readObject();
        o_in.close();
        f_in.close();
        return loaded;
    }

    /**
     * Loads a sequence from an input file. Intended usage is in arranger mode.
     *
     * @param inputFile
     *            This is the input filename.
     */
    public static void loadSequenceFromArrangement(File inputFile,
            Staff theStaff) {
        try {
            StaffSequence loaded = loadSong(inputFile);
            normalize(loaded);
            theStaff.setSequence(loaded);
            theStaff.setSequenceFile(inputFile);
            StateMachine.setTempo(loaded.getTempo());
            theStaff.getControlPanel().updateCurrTempo();
            theStaff.getControlPanel()
                    .getScrollbar()
                    .setMax(loaded.getTheLines().size()
                            - Values.NOTELINES_IN_THE_WINDOW);
            theStaff.setLocation(0);
            theStaff.getNoteMatrix().redraw();
            String fname = inputFile.getName();
            try {
                fname = fname.substring(0, fname.indexOf("."));
            } catch (IndexOutOfBoundsException e) {
                // Do nothing
            }
            theStaff.setSequenceName(fname);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is the regex pattern that denotes a note in an MPC text file. It
     * should also be able to catch "glitch" notes of the form a17
     */
    private static final String note = "(([a-z][a-z0-9][0-9]?[#;]?)?)";

    /** This is the regex pattern that denotes a kind of a concat operator. */
    private static final String plus = "\\+";

    /**
     * This is the regex pattern that denotes a kind of a concat operator but
     * forces you to have at least one of them.
     */
    private static final String plusForce = "\\+{1}";

    /**
     * This is a regex pattern that catches a note and a plus sign of the form
     * ab;+
     */
    private static final String noteCat = note + plus;

    /**
     * This is a regex pattern that catches a note and a plus sign of the form
     * ab;+ but forces you to catch the plus.
     */
    private static final String noteCatForce = note + plusForce;

    /**
     * This is the regex pattern that denotes a line of notes. The format must
     * be able to catch all lines in the form ab+cd+ef+gh+ij+q: or ++++++q: or :
     */
    private static final String noteLine = "(" + noteCat + noteCat + noteCat
            + noteCat + noteCat + noteCat + "[a-z])?:";

    /**
     * This is the regex pattern that denotes a line of notes. The format will
     * be able to catch all of the lines in the form ab+cd+ef+gh+ij+q: or
     * ++++++q: but not :
     */
    private static final String noteLineForce = "(" + noteCatForce
            + noteCatForce + noteCatForce + noteCatForce + noteCatForce
            + "[a-z])?:";

    /**
     * This is the pattern that denotes a line of notes.
     */
    private static final Pattern line = Pattern.compile(noteLine);

    /**
     * This is the pattern that denotes a line of notes that will have at least
     * some form of volume.
     */
    @SuppressWarnings("unused")
    private static final Pattern lineForce = Pattern.compile(noteLineForce);

    /**
     * Uses regex to clean up an MPC text file
     *
     * @param t
     *            The song text file to be "cleaned," that is, the song file
     *            that has unnecessarily empty note lines.
     * @return The "cleaned" string.
     * @since MPCTxtTools 1.07
     * @since 2013.02.05
     */
    public static String clean(String t) {
        return t.replaceAll("[*:]\\+\\+\\+\\+\\+\\+q:", "::");
    }

    /**
     * "Dices" a note line into its component instruments.
     *
     * @param s
     *            The note line.
     * @return An ArrayList of strings that denotes each set of instruments.
     */
    public static ArrayList<String> dice(String s) {
        ArrayList<String> out = new ArrayList<String>();
        Matcher m = Pattern.compile(noteCat).matcher(s);
        Pattern p = Pattern.compile(note);
        while (m.find()) {
            Matcher m1 = p.matcher(m.group());
            if (m1.find())
                out.add(m1.group());
        }
        return out;
    }

    /**
     * @since MPCTxtTools 1.05
     * @since 2011.11.08
     * @param s
     *            The song text file that is to be "chopped" into note lines.
     * @return All note lines that have data in them.
     */
    public static ArrayList<String> chop(String s) {
        s = clean(s);
        ArrayList<String> result = new ArrayList<String>();
        Matcher m = line.matcher(s);
        while (m.find()) {
            String gr = m.group();
            result.add(gr);
        }
        return result;
    }

    /**
     * Checks the number of lines in an MPC text file.
     *
     * @param read
     *            The text file that we have fed to the function.
     * @return The number of lines in this MPC text file. Ideally, this number
     *         is 384.
     */
    public static int countLines(String read) {
        Matcher m = line.matcher(read);
        int counter = 0;
        while (m.find()) {
            counter++;
        }
        return counter;
    }
}
