package smp.components.staff.sequences;

import smp.components.topPanel.instrumentLine.InstrumentIndex;

/**
 * A note on the Staff, to be added to the noteMatrix of the Staff
 * since
 * @author RehdBlob
 * @since 2012.08.31
 */
public class StaffNote {

    /**
     * The Instrument that the note on the staff is to use.
     */
    private InstrumentIndex theInstrument;

    /**
     * The location on the matrix that this note is located.
     */
    private StaffNoteIndex noteLocation;

    /**
     * The actual note that this note holds.
     */
    private Note theNote;

}
