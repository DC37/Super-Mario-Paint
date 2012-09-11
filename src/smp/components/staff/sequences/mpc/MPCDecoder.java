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
     * Links an <code>InstrumentIndex</code> to a Character.
     * @author RehdBlob
     * @since 2012.09.11
     */
    private enum MPCInstrumentIndex {
        a (InstrumentIndex.MARIO),
        b (InstrumentIndex.MUSHROOM),
        c (InstrumentIndex.YOSHI),
        d (InstrumentIndex.STAR),
        e (InstrumentIndex.FLOWER),
        f (InstrumentIndex.GAMEBOY),
        g (InstrumentIndex.DOG),
        h (InstrumentIndex.CAT),
        i (InstrumentIndex.PIG),
        j (InstrumentIndex.SWAN),
        k (InstrumentIndex.FACE),
        l (InstrumentIndex.PLANE),
        m (InstrumentIndex.BOAT),
        n (InstrumentIndex.CAR),
        o (InstrumentIndex.HEART),
        p (InstrumentIndex.PIRANHA),
        q (InstrumentIndex.SHYGUY),
        r (InstrumentIndex.COIN),
        s (InstrumentIndex.BOO);

        private InstrumentIndex ind;

        private MPCInstrumentIndex(InstrumentIndex i) {
            ind = i;
        }

        public InstrumentIndex getInstrument() {
            return ind;
        }

        public static InstrumentIndex valueOf(char c) {
            String str = "" + c;
            return MPCInstrumentIndex.valueOf(str).getInstrument();
        }


    }

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
     * @throws InvalidMidiDataException If something goes wrong in the decoding
     * process.
     */
    private static SMPSequence populateSequence(String timeSig,
            ArrayList<String> songData, String tempo)
                    throws InvalidMidiDataException {
        SMPSequence song = new SMPSequence(NUM_TRACKS);

        return null;
    }
}