package gui.loaders;

import gui.resources.SMPResourceType;

/**
 * Index values for the Hashtable in the ImageLoader class.
 * When other objects need to access their respective images,
 * use these keys to get the BufferedImage references.
 * @author RehdBlob
 * @since 2012.08.14
 */
public enum ImageIndex {

    // SPLASHSCREEN,

    /** No image */
    NONE,

    /** Blank image. */
    BLANK,
    
    /** Cursor images */
    CURSOR_0, CURSOR_1, CURSOR_2, CURSOR_3,
    
    /* INSTRUMENTS START HERE */
    
    // Normal images
    MARIO (SMPResourceType.INSTRUMENT),
    MUSHROOM (SMPResourceType.INSTRUMENT),
    YOSHI (SMPResourceType.INSTRUMENT),
    STAR (SMPResourceType.INSTRUMENT),
    FLOWER (SMPResourceType.INSTRUMENT),
    GAMEBOY (SMPResourceType.INSTRUMENT),
    DOG (SMPResourceType.INSTRUMENT),
    CAT (SMPResourceType.INSTRUMENT),
    PIG (SMPResourceType.INSTRUMENT),
    SWAN (SMPResourceType.INSTRUMENT),
    FACE (SMPResourceType.INSTRUMENT),
    PLANE (SMPResourceType.INSTRUMENT),
    BOAT (SMPResourceType.INSTRUMENT),
    CAR (SMPResourceType.INSTRUMENT),
    HEART (SMPResourceType.INSTRUMENT),
    PIRANHA (SMPResourceType.INSTRUMENT),
    COIN (SMPResourceType.INSTRUMENT),
    SHYGUY (SMPResourceType.INSTRUMENT),
    BOO (SMPResourceType.INSTRUMENT),
    LUIGI (SMPResourceType.INSTRUMENT),
    PEACH (SMPResourceType.INSTRUMENT),
    FEATHER (SMPResourceType.INSTRUMENT),
    BULLETBILL (SMPResourceType.INSTRUMENT),
    GOOMBA (SMPResourceType.INSTRUMENT),
    BOBOMB (SMPResourceType.INSTRUMENT),
    SPINY (SMPResourceType.INSTRUMENT),
    FRUIT (SMPResourceType.INSTRUMENT),
    ONEUP (SMPResourceType.INSTRUMENT),
    MOON (SMPResourceType.INSTRUMENT),
    EGG (SMPResourceType.INSTRUMENT),
    GNOME (SMPResourceType.INSTRUMENT),
    
    // Button sustained OFF
    MARIO_SM, MUSHROOM_SM, YOSHI_SM, STAR_SM, FLOWER_SM,
    GAMEBOY_SM, DOG_SM, CAT_SM, PIG_SM, SWAN_SM, FACE_SM,
    PLANE_SM, BOAT_SM, CAR_SM, HEART_SM, PIRANHA_SM, COIN_SM,
    SHYGUY_SM, BOO_SM, LUIGI_SM, PEACH_SM, FEATHER_SM, BULLETBILL_SM,
    GOOMBA_SM, BOBOMB_SM, SPINY_SM, FRUIT_SM, ONEUP_SM, MOON_SM,
    EGG_SM, GNOME_SM,
    
    // Button sustained ON (highlighted button line)
    MARIO_SMA, MUSHROOM_SMA, YOSHI_SMA, STAR_SMA, FLOWER_SMA,
    GAMEBOY_SMA, DOG_SMA, CAT_SMA, PIG_SMA, SWAN_SMA, FACE_SMA,
    PLANE_SMA, BOAT_SMA, CAR_SMA, HEART_SMA, PIRANHA_SMA, COIN_SMA,
    SHYGUY_SMA, BOO_SMA, LUIGI_SMA, PEACH_SMA, FEATHER_SMA, BULLETBILL_SMA,
    GOOMBA_SMA, BOBOMB_SMA, SPINY_SMA, FRUIT_SMA, ONEUP_SMA, MOON_SMA,
    EGG_SMA, GNOME_SMA,
    
    // Grayed out
    MARIO_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    MUSHROOM_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    YOSHI_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    STAR_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    FLOWER_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    GAMEBOY_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    DOG_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    CAT_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    PIG_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    SWAN_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    FACE_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    PLANE_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    BOAT_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    CAR_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    HEART_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    PIRANHA_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    COIN_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    SHYGUY_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    BOO_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    LUIGI_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    PEACH_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    FEATHER_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    BULLETBILL_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    GOOMBA_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    BOBOMB_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    SPINY_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    FRUIT_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    ONEUP_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    MOON_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    EGG_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    GNOME_GRAY (SMPResourceType.INSTRUMENT_GRAY),
    
    // Silhouettes
    MARIO_SIL, MUSHROOM_SIL, YOSHI_SIL, STAR_SIL, FLOWER_SIL,
    GAMEBOY_SIL, DOG_SIL, CAT_SIL, PIG_SIL, SWAN_SIL, FACE_SIL,
    PLANE_SIL, BOAT_SIL, CAR_SIL, HEART_SIL, PIRANHA_SIL, COIN_SIL,
    SHYGUY_SIL, BOO_SIL, LUIGI_SIL, PEACH_SIL, FEATHER_SIL, BULLETBILL_SIL,
    GOOMBA_SIL, BOBOMB_SIL, SPINY_SIL, FRUIT_SIL, ONEUP_SIL, MOON_SIL,
    EGG_SIL, GNOME_SIL,
    
    /* INSTRUMENTS END HERE */

    /* ACCIDENTALS START HERE */
    
    // Normal images
    SHARP, FLAT, DOUBLESHARP, DOUBLEFLAT,
    
    // Grayed out
    SHARP_GRAY, FLAT_GRAY, DOUBLESHARP_GRAY, DOUBLEFLAT_GRAY,
    
    // Silhouettes
    SHARP_SIL, FLAT_SIL, DOUBLESHARP_SIL, DOUBLEFLAT_SIL,
    
    /* ACCIDENTALS END HERE */
    
    VOL_BAR, VOL_BAR2,
    FILTER,

    /*
     * Clefs.
     */
    TREBLE_CLEF, BASS_CLEF,

    /*
     * Staff elements
     */
    /**
     * The frame that encloses the staff.
     */
    STAFF_FRAME, INST_BACKGROUND,

    /**
     * The background of the staff, which contains a treble clef.
     */
    STAFF_BG, STAFF_BG_TREBLEBASS,

    /**
     * Each one of these lines indicates a "beat"
     */
    STAFF_LINE,

    /** Each one of these lines indicates a measure. */
    STAFF_MLINE,
    
    /** Subdivisions in a measure */
    STAFF_SLINE,

    /**
     * This is the MPCv1 play bar that goes across the screen.
     */
    PLAY_BAR,

    /**
     * This is the bar that goes across the screen when one hits play.
     */
    PLAY_BAR1,

    /** This is an empty play bar. */
    PLAY_BAR_EMPTY,

    /**
     * This is a horizontal line that appears when someone tries to go
     * above or below the middle five lines of the staff.
     */
    STAFF_HLINE,
    
    /**
     * The image that is displayed behind the currently selected intrument.
     */
    SEL_INST_SM,

    /**
     * Controls elements
     */
    CONTROLS_LEFT, CONTROLS_MID, CONTROLS_RIGHT,
    SAVE, LOAD, NEW_SONG, OPTIONS,
    VOL_LABEL,
    ADD, DELETE, UP, DOWN,
    BOTTOM_BAR, MODE,

    /**
     * Button elements
     */
    STOP_PRESSED, STOP_RELEASED, STOP_LABEL,
    PLAY_PRESSED, PLAY_RELEASED, PLAY_LABEL,
    LOOP_PRESSED, LOOP_RELEASED, LOOP_LABEL,
    MUTE_PRESSED, MUTE_RELEASED, MUTE_LABEL,
    MUTE_A_PRESSED, MUTE_A_RELEASED, MUTE_A_LABEL,
    CLIPBOARD_PRESSED, CLIPBOARD_RELEASED, CLIPBOARD_LABEL,
    
    TIMESIG_4_4_PRESSED, TIMESIG_4_4_RELEASED,
    TIMESIG_3_4_PRESSED, TIMESIG_3_4_RELEASED,
    TIMESIG_6_8_PRESSED, TIMESIG_6_8_RELEASED,
    TIMESIG_CUSTOM_PRESSED, TIMESIG_CUSTOM_RELEASED,
    TIMESIG_LABEL,

    SCROLLBAR_LEFT1, SCROLLBAR_LEFT2,
    SCROLLBAR_LEFT1_PRESSED, SCROLLBAR_LEFT2_PRESSED,
    SCROLLBAR_RIGHT1, SCROLLBAR_RIGHT2,
    SCROLLBAR_RIGHT1_PRESSED, SCROLLBAR_RIGHT2_PRESSED,

    /*
     * Tempo stuff.
     */
    TEMPO_PLUS, TEMPO_MINUS, TEMPO_LABEL;
	
	/** The resource type this image belongs to. */
	private SMPResourceType type;
	
	private ImageIndex(SMPResourceType type) {
		this.type = type;
	}
	
	private ImageIndex() {
		this(SMPResourceType.UNCATEGORIZED);
	}
	
	/**
	 * Gets the {@link SMPResourceType} this image belongs to.
	 * 
	 * @return The resource type.
	 */
	public SMPResourceType getType() {
		return type;
	}

}
