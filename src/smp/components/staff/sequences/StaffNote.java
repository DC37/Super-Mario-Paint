package smp.components.staff.sequences;

import java.io.Serializable;

import javafx.scene.image.ImageView;
import smp.components.Values;
import smp.components.InstrumentIndex;

/**
 * A note on the Staff, to be added to the noteMatrix of the Staff.
 * @author RehdBlob
 * @since 2012.08.31
 */
public class StaffNote extends ImageView implements Serializable {

    /**
     * Generated serial ID.
     */
    private static final long serialVersionUID = 6827248837281952104L;

    /**
     * This is the location on the matrix where the note exists. (y-axis).
     * One can recover the note that you are supposed to actually play by
     * adding a constant offset to this position value.
     */
    private int position;

    /** The offset that this note will have. */
    private int accidental = 0;

    /** This is the volume of the note. */
    private int volume = Values.DEFAULT_VELOCITY;

    /** Whether this is a mute note or not. */
    private boolean isMuteNote;

    /**
     * The Instrument that the note on the staff is to use.
     */
    private InstrumentIndex theInstrument;


    /**
     * Default constructor that makes the note by default at half volume.
     * @param theInd The instrument that this StaffNote will play.
     * @param position The physical location of this note on the staff.
     * @param acc The sharp / flat / whatever that we are offsetting this
     * note by.
     */
    public StaffNote(InstrumentIndex theInd, int pos, int acc) {
        this(theInd, pos, acc, Values.HALF_VELOCITY);
    }


    /**
     * @param theInd The instrument that this StaffNote will play.
     * @param position The physical location of this note on the staff.
     * @param acc The sharp / flat / whatever that we are offsetting this
     * note by.
     * @param vol The volume that we want this note to play at.
     */
    public StaffNote(InstrumentIndex theInd, int pos, int acc, int vol) {
        theInstrument = theInd;
        accidental = acc;
        position = pos;
        volume = vol;
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
                    other.accidental == accidental &&
                    other.isMuteNote == isMuteNote;
        }
    }

    /** @return The offset from the actual note that we have here. */
    public int getAccidental() {
        return accidental;
    }

    /** @return The numerical position that this note is located at. */
    public int getPosition() {
        return position;
    }

    /** @return The numerical volume of this note. */
    public int volume() {
        return volume;
    }

    /**
     * Sets the volume to some number that we give to this method.
     * @param v The volume we want to set.
     */
    public void setVolume(int v) {
        if (v >= Values.MIN_VELOCITY && v <= Values.MAX_VELOCITY)
            volume = v;
    }

    @Override
    public String toString() {
        String noteName = Values.staffNotes[position].name();
        String noteAcc = "";
        switch(accidental) {
        case 2:
            noteAcc = "X";
            break;
        case 1:
            noteAcc = "#";
            break;
        case -1:
            noteAcc = "b";
            break;
        case -2:
            noteAcc = "bb";
            break;
        default:
            break;

        }
        return theInstrument.toString() + " " + noteName + noteAcc;
    }

    /** @return The instrument that this StaffNote is. */
    public InstrumentIndex getInstrument() {
        return theInstrument;
    }

    /**
     * @return <code>True</code> if this is a normal note and not a mute note.
     */
    public boolean isMuteNote() {
        return isMuteNote;
    }

    /** @param m Whether this note is a mute note or not. */
    public void setMuteNote(boolean m) {
        isMuteNote = m;
    }


}
