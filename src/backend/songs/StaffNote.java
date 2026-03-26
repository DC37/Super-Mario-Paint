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
    private int verticalPosition;

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
     */
    public StaffNote(InstrumentIndex theInd, int pos, Accidental acc, MuteModifier mod) {
        this(theInd, pos, acc, mod, Values.HALF_VELOCITY);
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
    public StaffNote(InstrumentIndex theInd, int pos, Accidental acc, MuteModifier mod, int vol) {
        theInstrument = theInd;
        accidental = acc;
        verticalPosition = pos;
        muteMod = mod;
        volume = vol;
    }
    
    public StaffNote(StaffNote note) {
        this(note.theInstrument, note.verticalPosition, note.accidental, note.muteMod, note.volume);
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
    public static StaffNote valueOf(String spl) throws ParseException {
    	InstrumentIndex theInstrument;
    	int verticalPosition = -1;
    	Accidental accidental;
    	MuteModifier muteMod;
    	int volume;
    	
        String[] sp = spl.split(" ");
        if (sp.length != 2) {
            throw new ParseException("Invalid note", 0);
        }
        theInstrument = InstrumentIndex.valueOf(sp[0]);
        for (int i = 0; i < Values.staffNotes.length; i++) {
            if (sp[1].contains(Values.staffNoteNames[i])) {
                verticalPosition = i;
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
            muteMod = muteModifierFromInt(Integer.parseInt("" + sp[1].charAt(sp[1].length() - 1)));
            break;
        case 5:
            accidental = decodeAccidental(sp[1].charAt(2));
            muteMod = muteModifierFromInt(Integer.parseInt("" + sp[1].charAt(sp[1].length() - 1)));
            break;
        default:
            accidental = Accidental.NATURAL;
            muteMod = MuteModifier.REGULAR;
            break;
        }
        
        return new StaffNote(theInstrument, verticalPosition, accidental, muteMod, volume);
    }
	
	private static MuteModifier muteModifierFromInt(int v) {
		switch (v) {
		case 0:
			return MuteModifier.REGULAR;
		case 1:
			return MuteModifier.MUTE_THIS_PITCH;
		case 2:
			return MuteModifier.MUTE_THIS_INST;
		default:
			throw new IllegalArgumentException("No mute modifier associated to value " + v);
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
    private static Accidental decodeAccidental(char c) {
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
            return other.verticalPosition == verticalPosition
                    && other.theInstrument == theInstrument
                    && other.accidental == accidental
                    && other.muteMod == muteMod;
        }
    }

    /**
     * The accidental of this note.
     * @return The accidental of this note
     */
    public Accidental getAccidental() {
        return accidental;
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
     * The volume of this note.
     * @return The volume of this note
     */
    @Deprecated
    public int volume() {
        return volume;
    }

    /**
     * Sets the volume to some number that we give to this method.
     *
     * @param v
     *            The volume we want to set.
     */
    @Deprecated
    public void setVolume(int v) {
        if (v >= Values.MIN_VELOCITY && v <= Values.MAX_VELOCITY)
            volume = v;
    }

    /** @return The instrument that this StaffNote is. */
    public InstrumentIndex getInstrument() {
        return theInstrument;
    }

    /**
     * The mute modifier of this note.
     * @return The mute modifier of this note
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
    
    /**
     * Get the pitch of this note.
     * @return The pitch of this note
     */
    public Pitch getPitch() {
    	return Pitch.valueOf(Values.staffNotes[verticalPosition].getValue() + accidental.getOffset());
    }

    @Override
    public String toString() {
    	String instName = theInstrument.toString();
        String noteName = Values.staffNoteNames[verticalPosition];
        String noteAcc = accidentalToString(accidental);
        String muteName = muteModifierToString(muteMod);
        return instName + " " + noteName + noteAcc + muteName;
    }
    
    private static String accidentalToString(Accidental acc) {
    	switch (acc) {
    	case Accidental.DOUBLE_FLAT:
    		return "B";
    	case Accidental.FLAT:
    		return "b";
    	case Accidental.NATURAL:
    		return "";
    	case Accidental.SHARP:
    		return "#";
    	case Accidental.DOUBLE_SHARP:
    		return "X";
    	default:
    		throw new IllegalArgumentException("Cannot convert " + acc + " to String");
    	}
    }
    
    private static String muteModifierToString(MuteModifier mod) {
    	switch (mod) {
    	case MuteModifier.REGULAR:
    		return "";
    	case MuteModifier.MUTE_THIS_PITCH:
    		return "m1";
    	case MuteModifier.MUTE_THIS_INST:
    		return "m2";
    	default:
    		throw new IllegalArgumentException("Cannot convert " + mod + " to String");
    	}
    }

}
