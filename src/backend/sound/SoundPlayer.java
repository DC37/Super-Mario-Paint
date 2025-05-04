package backend.sound;

import java.util.ArrayList;

import backend.songs.Accidental;
import backend.songs.StaffNote;
import backend.songs.StaffNoteLine;
import gui.InstrumentIndex;
import gui.Staff;
import gui.StateMachine;
import gui.Values;
import gui.loaders.SoundfontLoader;
import javafx.beans.property.DoubleProperty;

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

    /** Tells whether we want this thread to keep running. */
    private boolean run = true;

    /** This is the index that we are playing. */
    int ind;

    /** This keeps track of which notes are actually playing. */
    private NoteTracker tracker = new NoteTracker();

    /** This is the staff that we are referencing. */
    private Staff theStaff;
    
    /** Initializes this sound player and binds a staff to it. */
    public SoundPlayer(Staff s) {
        theStaff = s;
    }

    /**
     * Plays the current line of notes.
     */
    public void playSoundLine(int index) {
        if (!run)
            return;
        StaffNoteLine s = theStaff.getSequence().getLineSafe(
                StateMachine.getMeasureLineNum() + index);
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
                playSound(sn, s);
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
    private void playSound(StaffNote sn, StaffNoteLine s) {
        SoundfontLoader.playSound(
                Values.staffNotes[sn.getPosition()].getKeyNum(),
                sn.getInstrument(), sn.getAccidental(), s.getVolume());
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
    
    /**
     *
     * @param r
     */
    public void setRun(boolean r) {
        run = r;
    }
    
    /**
     *
     * @param i
     */
    public void setInd(int i) {
        ind = i;
    }

}
