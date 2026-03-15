package backend.songs;

import java.io.Serializable;
import java.text.ParseException;

import gui.InstrumentIndex;
import gui.Values;
import gui.loaders.ImageIndex;

/**
 * A note on the Staff, to be added to the noteMatrix of the Staff.
 *
 * @author RehdBlob
 * @since 2012.08.31
 */
public class StaffNote implements Serializable {

    /**
     * Generated serial ID.
     */
    private static final long serialVersionUID = 6827248837281952104L;

    /**
     * This is the location on the matrix where the note exists. (y-axis). One
     * can recover the note that you are supposed to actually play by adding a
     * constant offset to this position value.
     */
    private int position;

    private Accidental accidental = Accidental.NATURAL;

    /** This is the volume of the note. */
    private int volume = Values.DEFAULT_VELOCITY;

    /**
     * Modifier indicating if this is a mute note and of what type.
     */
    private MuteModifier muteMod;

    /**
     * The Instrument that the note on the staff is to use.
     */
    private InstrumentIndex theInstrument;
    
    /**
     * True if this note is currently selected with the clipboard.
     */
    private boolean selected = false;

    /**
     * Default constructor that makes the note by default at half volume.
     *
     * @param theInd
     *            The instrument that this StaffNote will play.
     * @param position
     *            The physical location of this note on the staff.
     * @param acc
     *            The sharp / flat / whatever that we are offsetting this note
     *            by.
     */
    public StaffNote(InstrumentIndex theInd, int pos, Accidental acc) {
        this(theInd, pos, acc, Values.HALF_VELOCITY);
    }

    /**
     * @param theInd
     *            The instrument that this StaffNote will play.
     * @param position
     *            The physical location of this note on the staff.
     * @param acc
     *            The sharp / flat / whatever that we are offsetting this note
     *            by.
     * @param vol
     *            The volume that we want this note to play at.
     */
    public StaffNote(InstrumentIndex theInd, int pos, Accidental acc, int vol) {
        theInstrument = theInd;
        accidental = acc;
        position = pos;
        volume = vol;
    }
    
    public StaffNote(StaffNote note) {
        this(note.theInstrument, note.position, note.accidental, note.volume);
    }
    
    public ImageIndex getImageIndex() {
        switch (muteMod) {
        case REGULAR:
            return theInstrument.imageIndex();
        case MUTE_THIS_PITCH:
            return theInstrument.imageIndex().alt();
        case MUTE_THIS_INST:
            return theInstrument.imageIndex().silhouette();
        default:
        	throw new IllegalArgumentException("Unrecognized mute modifier: " + muteMod);   
        }
    }
    
    public void setSelected(boolean b) {
        this.selected = b;
    }
    
    public boolean isSelected() {
        return selected;
    }

    /**
     * Construct a <code>StaffNote</code> given its printed
     * <code>toString()</code>
     *
     * @param spl
     *            The String to attempt to convert to a <code>StaffNote</code>
     * @throws ParseException
     *             In case we are trying to parse an invalid string.
     */
    public StaffNote(String spl) throws ParseException {
        String[] sp = spl.split(" ");
        if (sp.length != 2) {
            throw new ParseException("Invalid note", 0);
        }
        theInstrument = InstrumentIndex.valueOf(sp[0]);
        for (int i = 0; i < Values.staffNotes.length; i++) {
            if (sp[1].contains(Values.staffNotes[i].name())) {
                position = i;
            }
        }
        // Single-note volumes not implemented yet.
        volume = Values.HALF_VELOCITY;
        switch (sp[1].length()) {
        case 2:
            accidental = Accidental.NATURAL;
            muteMod = MuteModifier.REGULAR;
            break;
        case 3:
            accidental = decodeAccidental(sp[1].charAt(2));
            muteMod = MuteModifier.REGULAR;
            break;
        case 4:
            accidental = Accidental.NATURAL;
            muteMod = MuteModifier.fromInt(Integer.parseInt("" + sp[1].charAt(sp[1].length() - 1)));
            break;
        case 5:
            accidental = decodeAccidental(sp[1].charAt(2));
            muteMod = MuteModifier.fromInt(Integer.parseInt("" + sp[1].charAt(sp[1].length() - 1)));
            break;
        default:
            accidental = Accidental.NATURAL;
            muteMod = MuteModifier.REGULAR;
            break;
        }
    }

    /**
     * Given character <code>c</code>, decode it as a doublesharp, sharp, flat,
     * or doubleflat.
     *
     * @param c
     *            The character to decode.
     * @return The accidental to set.
     */
    private Accidental decodeAccidental(char c) {
        switch (c) {
        case 'X':
            return Accidental.DOUBLE_SHARP;
        case '#':
            return Accidental.SHARP;
        case 'b':
            return Accidental.FLAT;
        case 'B':
            return Accidental.DOUBLE_FLAT;
        default:
            return Accidental.NATURAL;
        }
    }

    /**
     * Sets the accidental of this note to whatever <code>a</code> is.
     *
     * @param a
     *            The accidental that we're trying to set this note to.
     */
    public void setAccidental(Accidental a) {
        accidental = a;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StaffNote)) {
            return false;
        } else {
            StaffNote other = (StaffNote) o;
            return other.position == position
                    && other.theInstrument == theInstrument
                    && other.accidental == accidental
                    && other.muteMod == muteMod;
        }
    }

    /** @return The offset from the actual note that we have here. */
    public Accidental getAccidental() {
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
     *
     * @param v
     *            The volume we want to set.
     */
    public void setVolume(int v) {
        if (v >= Values.MIN_VELOCITY && v <= Values.MAX_VELOCITY)
            volume = v;
    }

    /** @return The instrument that this StaffNote is. */
    public InstrumentIndex getInstrument() {
        return theInstrument;
    }

    /**
     * @return The mute modifier of this note.
     */
    public MuteModifier getMuteModifier() {
        return muteMod;
    }

    /**
     * Set the mute modifier for this note.
     * @param m A mute modifier
     */
    public void setMuteModifier(MuteModifier m) {
        muteMod = m;
    }

    @Override
    public String toString() {
        String noteName = Values.staffNotes[position].name();
        String noteAcc = "";
        switch (accidental) {
        case DOUBLE_SHARP:
            noteAcc = "X";
            break;
        case SHARP:
            noteAcc = "#";
            break;
        case FLAT:
            noteAcc = "b";
            break;
        case DOUBLE_FLAT:
            noteAcc = "B";
            break;
        default:
            break;

        }
        
        switch (muteMod) {
        case MUTE_THIS_INST:
            return theInstrument.toString() + " " + noteName + noteAcc + "m2";
        case MUTE_THIS_PITCH:
            return theInstrument.toString() + " " + noteName + noteAcc + "m1";
        default:
            return theInstrument.toString() + " " + noteName + noteAcc;
        }
    }

}
