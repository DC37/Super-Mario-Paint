package backend.songs;

/**
 * <p>Value-based class wrapping an integer to represent a pitch. The integer
 * corresponds to the MIDI value for that pitch.
 */
public class Pitch {
	
	/**
	 * Lowest pitch we can play; this is A0 on a piano keyboard.
	 */
	final public static Pitch LOWEST = new Pitch(21);
	
	/**
	 * A default pitch; this is A3 on a piano keyboard.
	 */
	final public static Pitch DEFAULT = new Pitch(57);
	
	/**
	 * Highest pitch we can play; this is B#7 on a piano keyboard.
	 */
	final public static Pitch HIGHEST = new Pitch(108);
	
	/**
	 * The integer identifying this pitch in MIDI format.
	 */
	final private int value;

	/**
	 * Base constructor for the Pitch class.
	 * @param value MIDI value for this pitch
	 * @throws IllegalArgumentException if the value is not in the accepted
	 * 		range
	 */
    private Pitch(int value) {
    	if (value < LOWEST.value || value > HIGHEST.value) {
    		throw new IllegalArgumentException("Illegal value for Pitch: " + value);
    	}
    	
        this.value = value;
    }
    
    /**
     * Create a pitch from a given MIDI value.
     * @param value An integer
     * @return Pitch whose value is the argument
	 * @throws IllegalArgumentException if the value is not in the accepted
	 * 		range
     */
    public static Pitch valueOf(int value) {
    	return new Pitch(value);
    }

    /**
     * Get the MIDI value of this pitch.
     * @return The MIDI value of this pitch
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Test equality between this pitch and another.
     * @param oth A pitch
     * @return true iff the two pitches have the same value
     */
    public boolean equals(Pitch oth) {
    	if (!(oth instanceof Pitch)) {
    		return false;
    	}
    	
    	return this.value == ((Pitch) oth).value;
    }

}
