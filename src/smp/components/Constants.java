package smp.components;

import smp.components.staff.sequences.Note;

/**
 * A constants file for holding things like default
 * window size, default modes, etc.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class Constants {

    /** Default width */
    public static final int DEFAULT_WIDTH = 805;

    /** Default height */
    public static final int DEFAULT_HEIGHT = 635;

    /**
     * The largest value that a note velocity can be;
     * a note played at this will be played as loudly
     * as possible.
     */
    public static final int MAX_VELOCITY = 127;

    /**
     * The median value that a note velocity can be. This
     * should be the half-volume level.
     */
    public static final int HALF_VELOCITY = 64;

    /**
     * The smallest value that a note velocity can be;
     * a note will basically be silent if played at this.
     */
    public static final int MIN_VELOCITY = 0;

    /** The number of distinct steps of notes in a note line on the staff. */
    public static final int NOTES_IN_A_LINE = 18;

    /** The number of distinct lines of notes that exist on the staff. */
    public static final int NOTES_IN_THE_STAFF = 9;

    /** The number of beats in each measure, by default. */
    public static final int DEFAULT_TIMESIG_BEATS = 4;

    /** The MIDI control channel for modulation. */
    public static final int MODULATION = 0x1;

    /** The MIDI control channel for volume. */
    public static final int VOLUME = 0x7;

    /** The MIDI control channel for pan. */
    public static final int PAN = 0xA;

    /** The MIDI control channel for sustain pedal. */
    public static final int SUSTAIN = 0x40;

    /** The MIDI control channel for reverb. */
    public static final int REVERB = 0x5B;

    /** The MIDI control channel for chorus. */
    public static final int CHORUS = 0x5D;

    /** Array of notes that we can see on the staff. */
    public static final Note[] staffNotes = {
        Note.A2, Note.B2, Note.C3, Note.D3, Note.E3, Note.F3, Note.G3,
        Note.A3, Note.B3, Note.C4, Note.D4, Note.E4, Note.F4, Note.G4,
        Note.A4, Note.B4, Note.C5, Note.D5};
}
