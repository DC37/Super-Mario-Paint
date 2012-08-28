package smp;

/**
 * Index values for the Hashtable in the ImageLoader class.
 * When other objects need to access their respective images,
 * use these keys to get the BufferedImage references.
 * @author RehdBlob
 * @since 2012.08.14
 */
public enum ImageIndex {

	// SPLASHSCREEN,
	
	/*
	 * Instruments.
	 */
	MARIO, MUSHROOM, YOSHI, STAR, FLOWER, GAMEBOY, DOG, CAT, PIG, SWAN,
	FACE, PLANE, BOAT, CAR, HEART, PIRANHA, COIN, SHYGUY, BOO, LUIGI,
	
	SHARP, FLAT,
	
	/*
	 * Clefs.
	 */
	TREBLE_CLEF, BASS_CLEF,
	
	/*
	 * Staff elements
	 */
	STAFF_FRAME, STAFF_BG, STAFF_LINE, STAFF_MLINE,
	PLAY_BAR1,
	
	/*
	 * Controls elements
	 */
	CONTROLS_LEFT, CONTROLS_MID, CONTROLS_RIGHT,
	
	/*
	 * Button elements
	 */
	STOP_PRESSED, STOP_RELEASED, STOP_LABEL,
	PLAY_PRESSED, PLAY_RELEASED, PLAY_LABEL,
	LOOP_PRESSED, LOOP_RELEASED, LOOP_LABEL,
	
	/*
	 * Tempo stuff.
	 */
	TEMPO_PLUS, TEMPO_MINUS, TEMPO_LABEL,
	
	INST_LINE;
	
}
