package backend.songs;

import java.util.HashMap;
import java.util.Map;

/**
 * Modifiers for notes: some notes on the staff are mute notes, whose purpose
 * is not to play sounds but to cancel previous notes.
 */
public enum MuteModifier {
    
    /** Not a mute note; just a regular note. */
    REGULAR(""),
    
    /** This note will cancel sounds of that instrument and pitch. */
    MUTE_THIS_PITCH("m1"),
    
    /** This note will cancel sounds of that instrument (every pitch). */
    MUTE_THIS_INST("m2");
	
	/** Token that represent the mute modifier in a file. */
	private String token;
	
	private static final Map<Integer, MuteModifier> LOOKUP_BY_TOKEN = new HashMap<>();
	
	static {
		for (MuteModifier mm : MuteModifier.values()) {
			char t = mm.getToken().isEmpty() ? '0' : mm.getToken().charAt(1);
			int type = Integer.parseInt(String.format("%c", t));
			
			LOOKUP_BY_TOKEN.put(type, mm);
		}
	}
	
	/**
	 * Creates a {@link MuteModifier} with the given token.
	 * 
	 * @param token The token to assign. 
	 */
	private MuteModifier(String token) {
		this.token = token;
	}
	
	/**
	 * Gets the token that represents this {@link MuteModifier} in a file.
	 * 
	 * @return The token as a {@link String}.
	 */
	public String getToken() {
		return token;
	}

	/**
     * Get the {@code MuteModifier} that corresponds to
     * the specified type.
     *
     * @param type The numeric type to look for.
     * @return The modifier corresponding to that type,
     *         or {@code REGULAR} if not found.
     */
	public static MuteModifier ofType(int type) {
		return LOOKUP_BY_TOKEN.getOrDefault(type, REGULAR);
	}
	
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
