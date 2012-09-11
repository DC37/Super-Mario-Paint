package smp.components.staff.sequences;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Decodes Mario Paint Composer songs into Super Mario Paint-
 * readable songs.
 * @author RehdBlob
 * @since 2012.09.01
 */
public class MPCDecoder {

    /**
     * Decodes a Mario Paint Composer song into an SMP-readable format.
     * Uses <code>TextUtil</code> from <code>MPCTxtTools</code>.
     * @param in The input String that contains (supposedly) Mario Paint
     * Composer song file data.
     * @throws ParseException If someone tries to feed this method an invalid
     * text file.
     */
    public static SMPSequence decode(String in) throws ParseException {
        if (in.indexOf('*') == -1 || in.isEmpty() || in == null) {
            throw new ParseException("Invalid MPC Text File.", 0);
        }
        ArrayList<String> everything = TextUtil.extract(in);
        String timeSig = everything.remove(0);
        String tempo = everything.remove(everything.size() - 1);
        return populateSequence(timeSig, everything, tempo);
    }

    /**
     * Creates a new Super Mario Paint sequence from the input Mario
     * Paint Composer text data.
     * @param timeSig The time signature of the Mario Paint Composer song.
     * @param songData The text data of the Mario Paint Composer song. This
     * defines the notes and instruments on each note line.
     * @param tempo The tempo at which this should be played at.
     * @return A new <code>SMPSequence</code> that is to be loaded by the
     * main program.
     */
    private static SMPSequence populateSequence(String timeSig,
            ArrayList<String> songData, String tempo) {
        // TODO Auto-generated method stub
        return null;
    }

}
