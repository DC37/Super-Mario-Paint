package smp.components.staff.sequences.mpc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;

import smp.components.staff.sequences.StaffSequence;
import static smp.components.general.Utilities.*;

/**
 * Decodes Mario Paint Composer songs into Super Mario Paint-
 * readable songs.
 * @author RehdBlob
 * @since 2012.09.01
 */
public class MPCDecoder {

    /**
     * Opens a file and decodes the Mario Paint Composer song data from
     * it, changing it into a Super Mario Paint sequence.
     * @param f A File, that supposedly contains Mario Paint Composer
     * song data.
     * @return An <code>StaffSequence</code> that has been converted from the
     * Mario Paint Composer song.
     * @throws InvalidMidiDataException If the file does not have valid
     * Mario Paint Composer data.
     * @throws ParseException If for some reason the parsing fails at some
     * point in the conversion process.
     * @throws IOException IF some error occurs during the decoding process.
     */
    public static StaffSequence decode(File f)
            throws ParseException, InvalidMidiDataException, IOException {
        BufferedReader bf = new BufferedReader(new FileReader(f));
        String line = "";
        String str = "";
        do {
            str += line;
            line = bf.readLine();
        } while (line != null);
        bf.close();
        return decode(str);
    }

    /**
     * Decodes a Mario Paint Composer song into an SMP-readable format.
     * Uses <code>TextUtil</code> from <code>MPCTxtTools</code>.
     * @param in The input String that contains (supposedly) Mario Paint
     * Composer song file data.
     * @throws ParseException If someone tries to feed this method an invalid
     * text file.
     * @throws InvalidMidiDataException If the sequence population fails to
     * work correctly.
     */
    public static StaffSequence decode(String in)
            throws ParseException, InvalidMidiDataException {
        if (in.indexOf('*') == -1 || in.isEmpty() || in == null) {
            throw new ParseException("Invalid MPC Text File.", 0);
        }
        ArrayList<String> everything = chop(clean(in));
        String timeSig = in.substring(0, in.indexOf('*'));
        String tempo = in.substring(in.indexOf('%') + 1, in.length() - 1);
        return populateSequence(timeSig, everything, tempo);
    }

    /**
     * Creates a new Super Mario Paint sequence from the input Mario
     * Paint Composer text data.
     * @param timeSig The time signature of the Mario Paint Composer song.
     * @param songData The text data of the Mario Paint Composer song. This
     * defines the notes and instruments on each note line.
     * @param tempo The tempo at which this should be played at.
     * @return A new <code>StaffSequence</code> that is to be loaded by the
     * main program.
     */
    private static StaffSequence populateSequence(String timeSig,
            ArrayList<String> songData, String tempo)
                    throws InvalidMidiDataException {
        StaffSequence song = new StaffSequence();
        return song;
    }

}