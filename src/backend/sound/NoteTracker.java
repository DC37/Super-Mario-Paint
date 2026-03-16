package backend.sound;
import java.util.ArrayList;

import backend.songs.Pitch;
import backend.songs.StaffNote;
import backend.songs.StaffNoteLine;
import gui.InstrumentIndex;
import gui.StateMachine;
import gui.Values;

/**
 * This is a class that keeps track of the different channels in Super
 * Mario Paint and which ones are currently playing sounds.
 * @author RehdBlob
 * @since 2013.11.10
 *
 */
class NoteTracker {

    /**
     * This is the list of lists, arranged by channel, of notes that are
     * playing.
     */
    private ArrayList<ArrayList<PlayingNote>> notesOn;

    /** This tells us which channels are currently playing notes. */
    private ArrayList<Boolean> channelOn;
    
    final private SoundPlayer soundPlayer;

    /**
     * Creates a new <code>NoteTracker</code> object and initializes
     * a list that is linked to each channel of sound.
     */
    public NoteTracker(SoundPlayer soundPlayer) {
        channelOn = new ArrayList<Boolean>();
        notesOn = new ArrayList<ArrayList<PlayingNote>>();
        for (int i = 0; i < InstrumentIndex.values().length; i++) {
            channelOn.add(false);
            notesOn.add(new ArrayList<PlayingNote>());
        }
        this.soundPlayer = soundPlayer;
    }

    /**
     * Stops the instruments contained in theNotes.
     * @param s The StaffNoteLine.
     */
    public void stopNotes(StaffNoteLine s) {
        boolean[] turnOff = new boolean[Values.NUMINSTRUMENTS];
        ArrayList<StaffNote> theNotes = s.getNotes();
        for(StaffNote sn : theNotes)
            turnOff[sn.getInstrument().getChannel() - 1] = true;

        boolean[] ext = StateMachine.getNoteExtensions();

        for (int i = 0; i < turnOff.length; i++) {
        	int j = (i == 15) ? 16 : (i == 16) ? 15 : i; // swap coin and piranha
            if (turnOff[i] && isChannelOn(i) && !ext[j]) {
                ArrayList<PlayingNote> pna = getNotesPlaying(i);
                for (PlayingNote pn : pna)
                    stopSound(pn);
                pna.clear();
                setChannelOff(i);
            }
        }
    }

    /** Tells us whether this note channel has a note playing. */
    private boolean isChannelOn(int channel) {
        return channelOn.get(channel);
    }

    /**
     * Sets a channel to "on"
     * @param channel The channel we want to set to on.
     */
    private void setChannelOn(int channel) {
        channelOn.set(channel, true);
    }

    /**
     * Sets a channel to "off"
     * @param channel The channel we want to set to off.
     */
    private void setChannelOff(int channel) {
        channelOn.set(channel, false);
    }

    /** This adds a note that is currently
     * playing to our list of notes playing.
     * @param instrument The instrument of the note that is playing.
     * @param pitch The pitchof the note that is playing.
     */
    public void addNotePlaying(InstrumentIndex instrument, Pitch pitch) {
        notesOn.get(instrument.getChannel() - 1).add(
                new PlayingNote(instrument, pitch));
        setChannelOn(instrument.getChannel() - 1);
    }

    /**
     * @param channel The channel we want to get playing notes from.
     * @return A list of notes that are currently playing from that channel.
     */
    private ArrayList<PlayingNote> getNotesPlaying(int channel) {
        return notesOn.get(channel);
    }

    /**
     * Stops a sound.
     * @param pn The playing note.
     * @param s The StaffNoteLine.
     */
    private void stopSound(PlayingNote pn) {
        soundPlayer.stopSound(pn.instrument(), pn.pitch());
    }

    /**
     * Stops the sound of a set of instruments.
     * @param inst The instrument to stop
     */
    public void stopInstrument(InstrumentIndex inst) {
        int i = inst.getChannel() - 1;
        ArrayList<PlayingNote> pna = getNotesPlaying(i);
        for (PlayingNote pn : pna)
            stopSound(pn);
        pna.clear();
        setChannelOff(i);
    }


    /**
     * This is a note that is currently playing.
     * @author RehdBlob
     * @since 2013.11.10
     */
    class PlayingNote {

        /** The instrument of the note that is playing. */
        private InstrumentIndex instrument;

        /** The pitch of the note that is playing. */
        private Pitch pitch;

        /**
         * Makes a PlayingNote object, which keeps track of which notes
         * are playing when.
         * @param ins The InstrumentIndex.
         * @param acc The pitch.
         */
        public PlayingNote(InstrumentIndex ins, Pitch pitch) {
            this.instrument = ins;
            this.pitch = pitch;
        }

        /**
         * @return The instrument of the note that is playing.
         */
        public InstrumentIndex instrument() {
            return instrument;
        }

        /**
         * @return The pitch of the note that is playing.
         */
        public Pitch pitch() {
            return pitch;
        }
    }

}
