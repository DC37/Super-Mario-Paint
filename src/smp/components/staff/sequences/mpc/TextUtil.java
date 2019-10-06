package smp.components.staff.sequences.mpc;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import smp.components.Values;

/**
 * Pulled from MPCTxtTools; this should be useful in decoding Mario Paint
 * Composer songs. Updated to v1.07a from MPCTxtTools. Now uses regex which
 * should be more "intuitive"
 *
 * @author RehdBlob
 * @since MPCTxtTools 1.05 / MPCTxtTools 1.07a
 * @since 2011.09.02
 */
public class TextUtil {

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
     * @param s
     *            The volume character that we're going to parse.
     * @return The volume. "a" is 0 and "q" is 127.
     */
    public static int parseVolume(char s) {
        if (s > 'r' || s < 'a')
            return Values.MAX_VELOCITY;
        return (int) ((s - (double) 'a') / ('q' - 'a') * Values.MAX_VELOCITY);
    }

    /**
     * @param s
     *            The character denoting the position on the staff.
     * @return The position on the staff that an MPC note is located.
     */
    public static int parsePosition(char s) {
        return Values.NOTES_IN_A_LINE - (s - 'a') - 5;
    }

    /**
     * @param s
     *            The character denoting whether this is a sharp or a flat.
     * @return The offset that this note will receive.
     */
    public static int parseAccidental(char s) {
        switch(s) {
        case ';':
            return -1;
        case '#':
            return 1;
        default:
            return 0;
        }
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
