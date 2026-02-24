package backend.sound;

import java.util.ArrayList;

import backend.songs.Accidental;
import backend.songs.StaffNote;
import backend.songs.StaffNoteLine;
import backend.songs.StaffSequence;
import gui.InstrumentIndex;
import gui.StateMachine;
import gui.Values;
import gui.loaders.SoundfontLoader;

/**
 *
 * A sound player thread that is spawned when one hits play. This thread's
 * purpose is to play sound on a non-JavaFX application thread, to hopefully
 * speed up the program's execution speed.
 *
 * @author RehdBlob
 * @since 2014.01.07
 *
 */
public class SoundPlayer {

    /** This keeps track of which notes are actually playing. */
    private NoteTracker tracker = new NoteTracker();

    /**
     * Plays the current line of notes.
     */
    public void playSoundLine(StaffSequence seq, int index) {
        StaffNoteLine s = seq.getLineSafe(StateMachine.getMeasureLineNum() + index);
        ArrayList<StaffNote> theNotes = s.getNotes();
        tracker.stopNotes(s);
        for (StaffNote sn : theNotes) {
            if (sn.muteNoteVal() == 1)
                stopSound(sn);
            else if (sn.muteNoteVal() == 2)
                stopInstrument(sn);
        }
        for (StaffNote sn : theNotes) {
            if (sn.muteNoteVal() == 0)
                playSound(sn, s.getVolume());
        }
    }

    /**
     * Plays a sound.
     *
     * @param sn
     *            The StaffNote.
     * @param s
     *            The StaffNoteLine.
     */
    public void playSound(StaffNote sn, int vel) {
        SoundfontLoader.playSound(
                Values.staffNotes[sn.getPosition()].getKeyNum(),
                sn.getInstrument(), sn.getAccidental(), vel);
        tracker.addNotePlaying(Values.staffNotes[sn.getPosition()].getKeyNum(),
                sn.getInstrument(), sn.getAccidental());
    }

    /**
     * Stops a sound.
     *
     * @param sn
     *            The StaffNote.
     */
    private void stopSound(StaffNote sn) {
        SoundfontLoader.stopSound(
                Values.staffNotes[sn.getPosition()].getKeyNum(),
                sn.getInstrument(), sn.getAccidental());
    }

    /**
     * Stops a full set of instruments from playing sounds.
     *
     * @param sn
     *            The StaffNote.
     */
    private void stopInstrument(StaffNote sn) {
        tracker.stopInstrument(sn);
    }

    
    /**
     * Turn off all sounds.
     */
    public void stopAllInstruments() {
    	for (InstrumentIndex in : InstrumentIndex.values()) {
    		tracker.stopInstrument(new StaffNote(in, 0, Accidental.NATURAL));
    	}
    	
    }

}
