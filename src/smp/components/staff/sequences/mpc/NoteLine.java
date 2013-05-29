package smp.components.staff.sequences.mpc;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * A class that denotes a line of notes on the MPC staff. Pulled from
 * MPCTxtTools 1.07a.
 * @author RehdBlob
 * @since MPCTxtTools 1.07
 * @since 2013.04.08
 */
public class NoteLine {

    /** THe number of notes in an MPC song line. There are actually 6 even though
     * MPC itself can only play 5 at a time.
     */
    private static final int NOTESIZE = 6;

    /** List of notes in this line. */
    private ArrayList<String> notes;

    /** A character (a-q) denoting what volume that this note is played at. */
    private char vol;

    /** Makes a new note line without anything in it. */
    public NoteLine() {
        notes = new ArrayList<String>();
        vol = 'q';
    }

    /**
     * Makes a new note line from a String that we assume to be a note line.
     * @param parse This is the String that we are attempting to parse.
     */
    public NoteLine(String parse) throws ParseException {
        if (parse.length() < NOTESIZE + 1) {
            notes = new ArrayList<String>();
            for (int i = 0; i < NOTESIZE; i++)
                notes.add("");
            vol = 'q';
            return;
        }
        notes = TextUtil.dice(parse);
        vol = parse.charAt(parse.length() - 2);
        if (!(vol >= 'a' && vol <= 'q'))
            vol = 'q';
        if (notes.size() != NOTESIZE)
            throw new ParseException("Incorrect number of notes in a line!", 0);
    }

    /**
     * @return The volume of the notes in this note line.
     */
    public char vol() {
        return vol;
    }

    /**
     * @return The list of notes that this note line has.
     */
    public ArrayList<String> notes() {
        return notes;
    }

    /**
     * Gets a specific note at location x from this note line.
     * @param x The index at which we want to grab a note from.
     * @return The note at that index.
     */
    public String notes(int x) {
        if (x < 0 || x > notes.size() - 1)
            return null;
        return notes.get(x);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        int empty = 0;
        for (String s1 : notes)
            if (s1.isEmpty())
                empty++;
        if (!(empty >= NOTESIZE && vol == 'q')) {
            for (String s1 : notes) {
                s.append(s1);
                s.append("+");
            }
            s.append(vol);
        }
        s.append(":");
        return s.toString();
    }
}
