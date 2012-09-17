package smp.components.staff.sequences.ams;

import java.text.ParseException;
import java.util.ArrayList;

import smp.components.staff.sequences.Bookmark;
import smp.components.staff.sequences.SMPSequence;
import smp.components.staff.sequences.Speedmark;

/**
 * A class for decoding Advanced Mario Sequencer songs.
 * @author RehdBlob
 * @since 2012.09.10
 */
public class AMSDecoder {

    /**
     * Decodes an Advanced Mario Sequencer song into an SMP-readable format.
     * @param in The input String that contains (supposedly) Advanced Mario
     * Sequencer data.
     * @throws ParseException If someone tries to feed this method an invalid
     * text file.
     */
    public static SMPSequence decode(String in) throws ParseException {
        if (/* Some condition */) {
            throw new ParseException("Invalid File", 0);
        }
        String timeSig, tempo;
        SMPSoundfont soundfont;
        ArrayList<Speedmark> speedmarks;
        ArrayList<Bookmark> bookmarks;
        ArrayList<String> data = new ArrayList<String>();
        return populateSequence(timeSig, soundfont, speedmarks,
                bookmarks, data, tempo);
    }

    /**
     * Creates a new Super Mario Paint song
     * @param timeSig The time signature of the song.
     * @param soundfont The soundfont of the song
     * @param speedmarks The speedmarks present in the Advanced Mario Sequencer
     * song.
     * @param bookmarks The bookmarks present in the Advanced Mario Sequencer
     * song.
     * @return A new Super Mario Paint sequence that is to be loaded
     * immediately upon creation.
     */
    private static SMPSequence populateSequence(String timeSig,
            SMPSoundfont soundfont, ArrayList<Speedmark> speedmarks,
            ArrayList<Bookmark> bookmarks) {
        return null;
    }
}
