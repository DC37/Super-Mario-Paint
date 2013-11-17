package smp.components.staff;
import java.util.ArrayList;
import smp.components.InstrumentIndex;

/**
 * This is a class that keeps track of the different channels in Super
 * Mario Paint and which ones are currently playing sounds.
 * @author RehdBlob
 * @since 2013.11.10
 *
 */
public class NoteTracker {

    /**
     * This is the list of lists, arranged by channel, of notes that are
     * playing.
     */
    private ArrayList<ArrayList<PlayingNote>> notesOn;

    /** This tells us which channels are currently playing notes. */
    private ArrayList<Boolean> channelOn;

    /**
     * Creates a new <code>NoteTracker</code> object and initializes
     * a list that is linked to each channel of sound.
     */
    public NoteTracker() {
        channelOn = new ArrayList<Boolean>();
        notesOn = new ArrayList<ArrayList<PlayingNote>>();
        for (int i = 0; i < InstrumentIndex.values().length; i++) {
            channelOn.add(false);
            notesOn.add(new ArrayList<PlayingNote>());
        }
    }

    /** Tells us whether this note channel has a note playing. */
    public boolean isNoteOn(int channel) {
        return channelOn.get(channel);
    }

    /**
     * Sets a channel to "on"
     * @param channel The channel we want to set to on.
     */
    public void setNoteOn(int channel) {
        channelOn.set(channel, true);
    }

    /**
     * Sets a channel to "off"
     * @param channel The channel we want to set to off.
     */
    public void setNoteOff(int channel) {
        channelOn.set(channel, false);
    }

    /** This adds a note that is currently
     * playing to our list of notes playing.
     * @param keyNum The key number of the note that is playing.
     * @param instrument The instrument of the note that is playing.
     * @param accidental The accidental of the note that is playing.
     */
    public void addNotePlaying(int keyNum, InstrumentIndex instrument,
            int accidental) {
        notesOn.get(instrument.getChannel()).add(
                new PlayingNote(keyNum, instrument, accidental));
    }

    /**
     * Makes new lists of notes that are playing. Use this to prevent
     * memory leaks while using the program.
     */
    public void reset() {
        channelOn.clear();
        for (ArrayList<PlayingNote> a : notesOn)
            a.clear();
        for (int i = 0; i < InstrumentIndex.values().length; i++)
            channelOn.add(false);
    }



    /**
     * This is a note that is currently playing.
     * @author RehdBlob
     * @since 2013.11.10
     */
    class PlayingNote {

        /** The key number of the note that is playing. */
        private int keyNum;

        /** The instrument of the note that is playing. */
        private InstrumentIndex instrument;

        /** The accidental of the note that is playing. */
        private int accidental;

        public PlayingNote(int k, InstrumentIndex ins, int acc) {
            keyNum = k;
            instrument = ins;
            accidental = acc;
        }

        /**
         * @return The key number of the note that is playing.
         */
        public int keyNum() {
            return keyNum;
        }

        /**
         * @return The instrument of the note that is playing.
         */
        public InstrumentIndex instrument() {
            return instrument;
        }

        /**
         * @return The accidental of the note that is playing.
         */
        public int accidental() {
            return accidental;
        }
    }

}
