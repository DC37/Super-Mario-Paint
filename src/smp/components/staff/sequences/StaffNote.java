package smp.components.staff.sequences;

import javafx.scene.image.ImageView;
import smp.components.InstrumentIndex;

/**
 * A note on the Staff, to be added to the noteMatrix of the Staff.
 * @author RehdBlob
 * @since 2012.08.31
 */
public class StaffNote extends ImageView {

    /** This is the location on the matrix where the note exists. (y-axis) */
    private int position;

    /**
     * The offset that this note will have.
     */
    private int accidental = 0;

    /**
     * The Instrument that the note on the staff is to use.
     */
    private InstrumentIndex theInstrument;


    /**
     * @param theInd The instrument that this StaffNote will play.
     * @param position The physical location of this note on the staff.
     * @param acc The sharp / flat / whatever that we are offsetting this
     * note by.
     */
    public StaffNote(InstrumentIndex theInd, int pos, int acc) {
        theInstrument = theInd;
        accidental = acc;
        position = pos;
    }

    /**
     * Sets the accidental of this note to whatever <code>a</code>
     * is.
     * @param a The accidental that we're trying to set this note to.
     */
    public void setAccidental(int a) {
        accidental = a;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StaffNote)) {
            return false;
        } else {
            StaffNote other = (StaffNote) o;
            return other.position == position &&
                    other.theInstrument == theInstrument &&
                    other.accidental == accidental;
        }
    }


    /** @return The offset from the actual note that we have here. */
    public int accidental() {
        return accidental;
    }

    /** @return The numerical position that this note is located at. */
    public int position() {
        return position;
    }

    @Override
    public String toString() {
        return theInstrument.toString() + " " + position;
    }


}
