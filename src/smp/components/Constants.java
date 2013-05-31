package smp.components;

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
     * The smallest value that a note velocity can be;
     * a note will basically be silent if played at this.
     */
    public static final int MIN_VELOCITY = 0;

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

}
