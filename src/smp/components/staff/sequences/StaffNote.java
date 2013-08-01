package smp.components.staff.sequences;

import javafx.scene.image.ImageView;
import smp.components.InstrumentIndex;

/**
 * A note on the Staff, to be added to the noteMatrix of the Staff.
 * @author RehdBlob
 * @since 2012.08.31
 */
public class StaffNote extends ImageView {

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
     * @param theInd The instrument that this StaffNote will play.
     * @param position The physical location of this note on the staff.
     * @param acc The sharp / flat / whatever that we are offsetting this
     * note by.
     */
    public StaffNote(InstrumentIndex theInd, int position, int acc) {
        theInstrument = theInd;
        accidental = acc;
    }

    /**
     * @return The location of the note in the staff matrix.
     */
    public StaffNoteIndex getNoteLocation() {
        return noteLocation;
    }

    /**
     * @param noteL This is where the note is located on the staff.
     */
    public void setNoteLocation(StaffNoteIndex noteL) {
        noteLocation = noteL;
    }

    /**
     * Sets the accidental of this note to whatever <code>a</code>
     * is.
     * @param a The accidental that we're trying to set this note to.
     */
    public void setAccidental(int a) {
        accidental = a;
    }

}
