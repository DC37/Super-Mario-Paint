package backend.songs;

/**
 * Modifiers for notes: some notes on the staff are mute notes, whose purpose
 * is not to play sounds but to cancel previous notes.
 */
public enum MuteModifier {
	
	/**
	 * Note a mute note; just a regular note.
	 */
	REGULAR,
	
	/**
	 * This note will cancel sounds of that instrument and pitch.
	 */
	MUTE_THIS_PITCH,
	
	/**
	 * This note will cancel sounds of that instrument (every pitch).
	 */
	MUTE_THIS_INST;
	
	// Remove when the whole code uses mute modifiers rather than int
	static MuteModifier fromInt(int v) {
		switch (v) {
		case 0:
			return REGULAR;
		case 1:
			return MUTE_THIS_PITCH;
		case 2:
			return MUTE_THIS_INST;
		default:
			throw new IllegalArgumentException("No mute modifier associated to value " + v);
		}
	}

}
