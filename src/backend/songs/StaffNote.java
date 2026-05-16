package backend.songs;

import gui.InstrumentIndex;
import gui.Values;
import gui.loaders.ImageIndex;

/**
 * A note on the Staff, to be added to the noteMatrix of the Staff.
 *
 * @author RehdBlob
 * @since 2012.08.31
 */
public class StaffNote {

    /**
     * This is the location on the matrix where the note exists. (y-axis). One
     * can recover the note that you are supposed to actually play by adding a
     * constant offset to this position value.
     */
    private int verticalPosition;

    private Accidental accidental = Accidental.NATURAL;

    /**
     * Modifier indicating if this is a mute note and of what type.
     */
    private MuteModifier muteModifier;

    /**
     * The Instrument that the note on the staff is to use.
     */
    private InstrumentIndex instrument;
    
    /**
     * True if this note is currently selected with the clipboard.
     */
    private boolean selected = false;

    /**
     * Default constructor that makes the note by default at half volume.
     *
     * @param theInd
     *            The instrument that this StaffNote will play.
     * @param verticalPosition
     *            The vertical position of this note on the staff.
     * @param acc
     *            The sharp / flat / whatever that we are offsetting this note
     *            by.
     */
    public StaffNote(InstrumentIndex theInd, int verticalPosition, Accidental acc) {
        this(theInd, verticalPosition, acc, MuteModifier.REGULAR);
    }

    /**
     * @param theInd
     *            The instrument that this StaffNote will play.
     * @param verticalPosition
     *            The vertical position of this note on the staff.
     * @param acc
     *            The sharp / flat / whatever that we are offsetting this note
     *            by.
     * @param mod
     * 			  The mute modifier of this note
     * @param vol
     *            The volume that we want this note to play at.
     */
    public StaffNote(InstrumentIndex theInd, int pos, Accidental acc, MuteModifier mod) {
        instrument = theInd;
        accidental = acc;
        verticalPosition = pos;
        muteModifier = mod;
    }
    
    public StaffNote(StaffNote note) {
        this(note.instrument, note.verticalPosition, note.accidental, note.muteModifier);
    }

    /**
     * The vertical position of this note on the staff. 0 is associated to the
     * lowest note (C2 in the current setup).
     * @return The vertical position of the note on the staff
     */
    public int getVerticalPosition() {
        return verticalPosition;
    }

    /**
     * The accidental of this note.
     * @return The accidental of this note
     */
    public Accidental getAccidental() {
        return accidental;
    }

    /**
     * The mute modifier of this note.
     * @return The mute modifier of this note
     */
    public MuteModifier getMuteModifier() {
        return muteModifier;
    }

    /** @return The instrument that this StaffNote is. */
    public InstrumentIndex getInstrument() {
        return instrument;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean b) {
        this.selected = b;
    }
    
    public ImageIndex getImageIndex() {
        switch (muteModifier) {
        case REGULAR:
            return instrument.imageIndex();
        case MUTE_THIS_PITCH:
            return instrument.imageIndex().alt();
        case MUTE_THIS_INST:
            return instrument.imageIndex().silhouette();
        default:
        	throw new IllegalArgumentException("Unrecognized mute modifier: " + muteModifier);   
        }
    }
    
    /**
     * Get the pitch of this note.
     * @return The pitch of this note
     */
    public Pitch getPitch() {
    	return Pitch.valueOf(Values.staffNotes[verticalPosition].getValue() + accidental.getOffset());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StaffNote)) {
            return false;
        } else {
            StaffNote other = (StaffNote) o;
            return other.verticalPosition == verticalPosition
                    && other.instrument == instrument
                    && other.accidental == accidental
                    && other.muteModifier == muteModifier;
        }
    }

}
