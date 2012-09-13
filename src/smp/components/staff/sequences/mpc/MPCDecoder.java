package smp.components.staff.sequences.mpc;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.sound.midi.InvalidMidiDataException;

import smp.components.staff.sequences.SMPSequence;
import smp.components.staff.sequences.TextUtil;
import smp.components.topPanel.instrumentLine.InstrumentIndex;

/**
 * Decodes Mario Paint Composer songs into Super Mario Paint-
 * readable songs.
 * @author RehdBlob
 * @since 2012.09.01
 */
public class MPCDecoder {

    /**
     * The number of tracks in the Mario Paint Composer song.
     */
    private static final int NUM_TRACKS = 19;

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
    public static SMPSequence decode(String in)
            throws ParseException, InvalidMidiDataException {
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
     * @throws InvalidMidiDataException If something goes wrong in the decoding
     * process.
     */
    private static SMPSequence populateSequence(String timeSig,
            ArrayList<String> songData, String tempo)
                    throws InvalidMidiDataException {
        SMPSequence song = new SMPSequence(NUM_TRACKS);
        song.setTime(timeSig);
        song.setData(decodeSong(songData));
        song.setTempo(tempo);
        return song;
    }

    /**
     * 
     * @param songData
     * @return The MIDI events in the MPC sequence
     */
    private static SongData decodeSong(ArrayList<String> songData) {
        for (String s : songData) {
            String[] sp = songData.split("+");
            for (String s : sp) {
                char[] c = s.toCharArray();
                InstrumentIndex i = MPCInstrumentIndex.valueOf(c[0]);

                // TODO: Fix everything!
            }
        }
        return null;
    }
}