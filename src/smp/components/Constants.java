package smp.components;

/**
 * A constants file for holding things like default
 * window size, default modes, etc.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class Constants {

    public static final int DEFAULT_WIDTH = 805;

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

}
