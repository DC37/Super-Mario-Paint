package smp.components.staff.sequences;

import javax.sound.midi.MidiChannel;

import smp.SoundfontLoader;
import smp.components.InstrumentIndex;
import smp.stateMachine.Settings;

/**
 * A note on the Staff, to be added to the noteMatrix of the Staff
 * since
 * @author RehdBlob
 * @since 2012.08.31
 */
public class StaffNote {

    /**
     * The offset that this note will have.
     */
    private int accidental = 0;

    /**
     * The Instrument that the note on the staff is to use.
     */
    private InstrumentIndex theInstrument;

    /**
     * The location on the matrix that this note is located.
     */
    private StaffNoteIndex noteLocation;

    /**
     * The actual note that this note holds, which holds an int value
     * that a MIDI event can handle.
     */
    private Note theNote;

    /**
     * Plays this note's sound.
     */
    public void playSound() {
        SoundfontLoader.playSound(theNote, theInstrument);
    }

    /**
     * @return the noteLocation
     */
    public StaffNoteIndex getNoteLocation() {
        return noteLocation;
    }

    /**
     * @param noteLocation the noteLocation to set
     */
    public void setNoteLocation(StaffNoteIndex noteLocation) {
        this.noteLocation = noteLocation;
    }

}
