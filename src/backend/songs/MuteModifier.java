package backend.songs;

/**
 * Modifiers for notes: some notes on the staff are mute notes, whose purpose
 * is not to play sounds but to cancel previous notes.
 */
public enum MuteModifier {
    
    /**
     * Not a mute note; just a regular note.
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

	/**
	 * Retrieves the {@link MuteModifier} that corresponds to the
	 * given mute instrument/pitch flags.
	 * 
	 * <p>Specifically:
	 * 
	 * <ul>
	 * <li>If {@code muteInstrument} is true, regardless of the value of
	 *     {@code mutePitch}, it returns {@link MUTE_THIS_INST}.</li>
	 * <li>If {@code muteInstrument} is false and {@code mutePitch} is true,
	 *     it returns {@link MUTE_THIS_PITCH}.</li>
	 * <li>If both {@code muteInstrument} and {@code mutePitch} are false,
	 *     it returns {@link GENERAL}.</li>
	 * </ul>
	 * 
	 * @param muteInstrument Whether the instrument should be muted at every pitch
	 * @param mutePitch Whether the instrument should be muted at the specific pitch
	 * @return A {@link MuteModifier} that corresponds to the selection
	 */
	public static MuteModifier givenFlags(boolean muteInstrument, boolean mutePitch) {
		if (muteInstrument) return MUTE_THIS_INST;
		if (mutePitch) return MUTE_THIS_PITCH;
		return REGULAR;
	}
	
}
