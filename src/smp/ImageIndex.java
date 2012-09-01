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
	
	/*
	 * Instruments grayed out.
	 */
	MARIO_GRAY, MUSHROOM_GRAY, YOSHI_GRAY, STAR_GRAY, FLOWER_GRAY, 
	GAMEBOY_GRAY, DOG_GRAY, CAT_GRAY, PIG_GRAY, SWAN_GRAY, FACE_GRAY,
	PLANE_GRAY, BOAT_GRAY, CAR_GRAY, HEART_GRAY, PIRANHA_GRAY, COIN_GRAY, 
	SHYGUY_GRAY, BOO_GRAY, LUIGI_GRAY,
	
	SHARP, FLAT,
	
	/*
	 * Silhouettes of images.
	 */
	MARIO_SIL, MUSHROOM_SIL, YOSHI_SIL, STAR_SIL, FLOWER_SIL, 
	GAMEBOY_SIL, DOG_SIL, CAT_SIL, PIG_SIL, SWAN_SIL, FACE_SIL,
	PLANE_SIL, BOAT_SIL, CAR_SIL, HEART_SIL, PIRANHA_SIL, COIN_SIL, 
	SHYGUY_SIL, BOO_SIL, LUIGI_SIL,
	
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
