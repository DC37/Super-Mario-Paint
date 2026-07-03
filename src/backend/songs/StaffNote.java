package backend.songs;

import gui.InstrumentIndex;
import gui.Values;

/**
 * <p>The representation for a note. All the information required for playing a
 * sound is contained in that class: instrument, position on a vertical line
 * and accidental (determines the pitch, see {@link StaffNote#getPitch}), and
 * whether this is a regular note or a certain mute note.
 */
public class StaffNote {

    /**
     * The position of this note in a vertical line.
     */
    private int verticalPosition;

    /**
     * The accidental of this note.
     */
    private Accidental accidental = Accidental.NATURAL;

    /**
     * Modifier indicating if this is a mute note and of what type.
     */
    private MuteModifier muteModifier;

    /**
     * The instrument this note belongs to.
     */
    private InstrumentIndex instrument;
    
    /**
     * True if this note is currently selected with the clipboard.
     */
    private boolean selected = false;

    /**
     * Creates a regular note of a certain instrument with a given position and
     * accidental.
     * @param instrument The instrument
     * @param verticalPosition The position
     * @param accidental The accidental
     */
    public StaffNote(InstrumentIndex instrument, int verticalPosition, Accidental accidental) {
        this(instrument, verticalPosition, accidental, MuteModifier.REGULAR);
    }

    /**
     * Creates a note of a certain instrument with a given position,
     * accidental, and possibly a mute modifier.
     * @param instrument The instrument
     * @param verticalPosition The position
     * @param accidental The accidental
     * @param muteModifier The modifier (regular, mute pitch, mute instrument)
     */
    public StaffNote(InstrumentIndex instrument, int verticalPosition, Accidental accidental, MuteModifier muteModifier) {
        this.instrument = instrument;
        this.accidental = accidental;
        this.verticalPosition = verticalPosition;
        this.muteModifier = muteModifier;
    }
    
    /**
     * Creates a copy of another note.
     * @param note The note to copy
     */
    public StaffNote(StaffNote note) {
        this(note.instrument, note.verticalPosition, note.accidental, note.muteModifier);
    }

    /**
     * Get the vertical position of this note on the staff.
     * @return The vertical position of this note on the staff
     */
    public int getVerticalPosition() {
        return verticalPosition;
    }

    /**
     * Get the accidental of this note.
     * @return The accidental of this note
     */
    public Accidental getAccidental() {
        return accidental;
    }

    /**
     * Get the mute modifier of this note.
     * @return The mute modifier of this note
     */
    public MuteModifier getMuteModifier() {
        return muteModifier;
    }

    /**
     * Get the instrument of this note.
     * @return The instrument of this note
     */
    public InstrumentIndex getInstrument() {
        return instrument;
    }
    
    /**
     * Checks whether this note is selected.
     * @return True if the note is selected
     */
    public boolean isSelected() {
        return selected;
    }
    
    /**
     * Set the selected flag of this note.
     * @param b The new value for the selected flag
     */
    public void setSelected(boolean b) {
        this.selected = b;
    }
    
    /**
     * Get the pitch of this note.
     * @return The pitch of this note
     */
    public Pitch getPitch() {
        return Pitch.valueOf(Values.STAFF_NOTES[verticalPosition].getValue() + accidental.getOffset());
    }

    /**
     * Notes of the same instrument, position, accidental and mute modifier are
     * considered equal.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StaffNote)) {
            return false;
        }
        
        StaffNote other = (StaffNote) o;
        return other.verticalPosition == verticalPosition
                && other.instrument == instrument
                && other.accidental == accidental
                && other.muteModifier == muteModifier;
    }

}
