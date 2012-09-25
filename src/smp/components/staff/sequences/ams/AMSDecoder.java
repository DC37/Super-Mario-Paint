package smp.components.staff.sequences.ams;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;

import smp.components.staff.sequences.Bookmark;
import smp.components.staff.sequences.Speedmark;
import smp.components.staff.sounds.SMPSequence;
import smp.components.staff.sounds.SMPSoundfont;

/**
 * A class for decoding Advanced Mario Sequencer songs.
 * @author RehdBlob
 * @since 2012.09.10
 */
public class AMSDecoder {

    public static void decode(File file) {
    }

    /**
     * Decodes an Advanced Mario Sequencer song into an SMP-readable format.
     * @param in The input String that contains (supposedly) Advanced Mario
     * Sequencer data.
     * @throws ParseException If someone tries to feed this method an invalid
     * text file.
     */
    public static SMPSequence decode(String in) throws ParseException {
        if (!isValid(in)) {
            throw new ParseException("Invalid File", 0);
        }
        String timeSig = null, tempo = null;
        SMPSoundfont soundfont = null;
        ArrayList<Speedmark> speedmarks = null;
        ArrayList<Bookmark> bookmarks = null;
        ArrayList<String> data = new ArrayList<String>();
        return populateSequence(timeSig, soundfont, speedmarks,
                bookmarks, data, tempo);
    }

    /**
     * Determines whether an input file is a valid Advanced Mario Sequencer
     * file.
     * @param in The String that is supposedly a representation of the Advanced
     * Mario Sequencer file.
     * @return <b>True</b> if the file happens to be valid. <b>False</b>
     * otherwise.
     */
    private static boolean isValid(String in) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Creates a new Super Mario Paint song
     * @param timeSig The time signature of the song.
     * @param soundfont The soundfont of the song
     * @param speedmarks The speedmarks present in the Advanced Mario Sequencer
     * song.
     * @param bookmarks The bookmarks present in the Advanced Mario Sequencer
     * song.
     * @param tempo
     * @param data
     * @return A new Super Mario Paint sequence that is to be loaded
     * immediately upon creation.
     */
    private static SMPSequence populateSequence(String timeSig,
            SMPSoundfont soundfont, ArrayList<Speedmark> speedmarks,
            ArrayList<Bookmark> bookmarks, ArrayList<String> data, String tempo) {
        return null;
    }

}
