package gui.loaders;

import java.util.HashMap;
import java.util.Map;

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

    /* No image */
    NONE,

    /* Blank image. */
    BLANK,
    
    /* Cursor images */
    CURSOR_0 (SMPResourceType.CURSOR),
    CURSOR_1 (SMPResourceType.CURSOR),
    CURSOR_2 (SMPResourceType.CURSOR),
    CURSOR_3 (SMPResourceType.CURSOR),
    
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
    MARIO_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    MUSHROOM_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    YOSHI_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    STAR_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    FLOWER_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    GAMEBOY_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    DOG_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    CAT_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    PIG_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    SWAN_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    FACE_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    PLANE_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    BOAT_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    CAR_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    HEART_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    PIRANHA_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    COIN_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    SHYGUY_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    BOO_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    LUIGI_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    PEACH_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    FEATHER_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    BULLETBILL_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    GOOMBA_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    BOBOMB_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    SPINY_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    FRUIT_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    ONEUP_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    MOON_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    EGG_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    GNOME_SM (SMPResourceType.INSTRUMENT_SUSTAINED_OFF),
    
    // Button sustained ON (highlighted button line)
    MARIO_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    MUSHROOM_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    YOSHI_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    STAR_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    FLOWER_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    GAMEBOY_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    DOG_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    CAT_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    PIG_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    SWAN_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    FACE_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    PLANE_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    BOAT_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    CAR_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    HEART_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    PIRANHA_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    COIN_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    SHYGUY_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    BOO_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    LUIGI_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    PEACH_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    FEATHER_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    BULLETBILL_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    GOOMBA_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    BOBOMB_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    SPINY_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    FRUIT_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    ONEUP_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    MOON_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    EGG_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    GNOME_SMA (SMPResourceType.INSTRUMENT_SUSTAINED_ON),
    
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
    MARIO_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    MUSHROOM_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    YOSHI_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    STAR_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    FLOWER_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    GAMEBOY_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    DOG_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    CAT_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    PIG_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    SWAN_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    FACE_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    PLANE_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    BOAT_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    CAR_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    HEART_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    PIRANHA_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    COIN_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    SHYGUY_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    BOO_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    LUIGI_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    PEACH_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    FEATHER_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    BULLETBILL_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    GOOMBA_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    BOBOMB_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    SPINY_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    FRUIT_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    ONEUP_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    MOON_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    EGG_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    GNOME_SIL (SMPResourceType.INSTRUMENT_SILHOUETTE),
    
    /* INSTRUMENTS END HERE */

    /* ACCIDENTALS START HERE */
    
    // Normal images
    SHARP (SMPResourceType.ACCIDENTAL),
    FLAT (SMPResourceType.ACCIDENTAL),
    DOUBLESHARP (SMPResourceType.ACCIDENTAL),
    DOUBLEFLAT (SMPResourceType.ACCIDENTAL),
    
    // Grayed out
    SHARP_GRAY (SMPResourceType.ACCIDENTAL_GRAY),
    FLAT_GRAY (SMPResourceType.ACCIDENTAL_GRAY),
    DOUBLESHARP_GRAY (SMPResourceType.ACCIDENTAL_GRAY),
    DOUBLEFLAT_GRAY (SMPResourceType.ACCIDENTAL_GRAY),
    
    // Silhouettes
    SHARP_SIL (SMPResourceType.ACCIDENTAL_SILHOUETTE),
    FLAT_SIL (SMPResourceType.ACCIDENTAL_SILHOUETTE),
    DOUBLESHARP_SIL (SMPResourceType.ACCIDENTAL_SILHOUETTE),
    DOUBLEFLAT_SIL (SMPResourceType.ACCIDENTAL_SILHOUETTE),
    
    /* ACCIDENTALS END HERE */
    
    /* STAFF ELEMENTS START HERE */
    
    // Enclosing frame
    STAFF_FRAME (SMPResourceType.STAFF),
    INST_BACKGROUND (SMPResourceType.STAFF),
    
    // Background (includes clefs)
    STAFF_BG (SMPResourceType.STAFF),
    STAFF_BG_TREBLEBASS (SMPResourceType.STAFF),
    
    // Standalone clefs
    TREBLE_CLEF (SMPResourceType.STAFF),
    BASS_CLEF (SMPResourceType.STAFF),
    
    // Lines: Beat, Measure, Measure subdivision
    STAFF_LINE (SMPResourceType.STAFF),
    STAFF_MLINE (SMPResourceType.STAFF),
    STAFF_SLINE (SMPResourceType.STAFF),
    
    // Play bars: MPCv1, during play, empty
    PLAY_BAR (SMPResourceType.STAFF),
    PLAY_BAR1 (SMPResourceType.STAFF),
    PLAY_BAR_EMPTY (SMPResourceType.STAFF), 
    
    // Horizontal line that appears above/below standard staff lines
    STAFF_HLINE (SMPResourceType.STAFF),
    
    /* STAFF ELEMENTS END HERE */
    
    /* CONTROLS START HERE */
    
    // Enclosing frame
    CONTROLS_LEFT (SMPResourceType.CONTROL),
    CONTROLS_MID (SMPResourceType.CONTROL),
    CONTROLS_RIGHT (SMPResourceType.CONTROL),
    
    // Dividers
    BOTTOM_BAR (SMPResourceType.CONTROL),
    
    // Volume bars
    VOL_BAR (SMPResourceType.CONTROL),
    VOL_BAR2 (SMPResourceType.CONTROL),
    VOL_LABEL (SMPResourceType.CONTROL),
    
    // Filter
    FILTER (SMPResourceType.CONTROL),
    
    // Selected instrument background
    SEL_INST_SM (SMPResourceType.CONTROL),
    
    // Arrangement operations
    ADD (SMPResourceType.CONTROL),
    DELETE (SMPResourceType.CONTROL),
    UP (SMPResourceType.CONTROL),
    DOWN (SMPResourceType.CONTROL),
    
    // Tempo
    TEMPO_PLUS (SMPResourceType.CONTROL),
    TEMPO_MINUS (SMPResourceType.CONTROL),
    TEMPO_LABEL (SMPResourceType.CONTROL),
    
    // Other
    MODE (SMPResourceType.CONTROL),
    
    /* CONTROLS END HERE */
    
    /* BUTTONS START HERE */
    
    STOP_PRESSED (SMPResourceType.BUTTON_PRESSED),
    STOP_RELEASED (SMPResourceType.BUTTON),
    STOP_LABEL (SMPResourceType.BUTTON_LABEL),
    
    PLAY_PRESSED (SMPResourceType.BUTTON_PRESSED),
    PLAY_RELEASED (SMPResourceType.BUTTON),
    PLAY_LABEL (SMPResourceType.BUTTON_LABEL),
    
    LOOP_PRESSED (SMPResourceType.BUTTON_PRESSED),
    LOOP_RELEASED (SMPResourceType.BUTTON),
    LOOP_LABEL (SMPResourceType.BUTTON_LABEL),
    
    MUTE_PRESSED (SMPResourceType.BUTTON_PRESSED),
    MUTE_RELEASED (SMPResourceType.BUTTON),
    MUTE_LABEL (SMPResourceType.BUTTON_LABEL),
    
    MUTE_A_PRESSED (SMPResourceType.BUTTON_PRESSED),
    MUTE_A_RELEASED (SMPResourceType.BUTTON),
    MUTE_A_LABEL (SMPResourceType.BUTTON_LABEL),
    
    CLIPBOARD_PRESSED (SMPResourceType.BUTTON_PRESSED),
    CLIPBOARD_RELEASED (SMPResourceType.BUTTON),
    CLIPBOARD_LABEL (SMPResourceType.BUTTON_LABEL),
    
    TIMESIG_4_4_PRESSED (SMPResourceType.BUTTON_PRESSED),
    TIMESIG_4_4_RELEASED (SMPResourceType.BUTTON),
    
    TIMESIG_3_4_PRESSED (SMPResourceType.BUTTON_PRESSED),
    TIMESIG_3_4_RELEASED (SMPResourceType.BUTTON),
    
    TIMESIG_6_8_PRESSED (SMPResourceType.BUTTON_PRESSED),
    TIMESIG_6_8_RELEASED (SMPResourceType.BUTTON),
    
    TIMESIG_CUSTOM_PRESSED (SMPResourceType.BUTTON_PRESSED),
    TIMESIG_CUSTOM_RELEASED (SMPResourceType.BUTTON),
    
    TIMESIG_LABEL (SMPResourceType.BUTTON_LABEL),

    // File operations
    
    SAVE_PRESSED (SMPResourceType.BUTTON_PRESSED),
    SAVE_RELEASED (SMPResourceType.BUTTON),
    
    LOAD_PRESSED (SMPResourceType.BUTTON_PRESSED),
    LOAD_RELEASED (SMPResourceType.BUTTON),
    
    NEW_PRESSED (SMPResourceType.BUTTON_PRESSED),
    NEW_RELEASED (SMPResourceType.BUTTON),
    
    OPTIONS_PRESSED (SMPResourceType.BUTTON_PRESSED),
    OPTIONS_RELEASED (SMPResourceType.BUTTON),
    
    SCROLLBAR_LEFT1 (SMPResourceType.BUTTON),
    SCROLLBAR_LEFT1_PRESSED (SMPResourceType.BUTTON_PRESSED),
    
    SCROLLBAR_LEFT2 (SMPResourceType.BUTTON),
    SCROLLBAR_LEFT2_PRESSED (SMPResourceType.BUTTON_PRESSED),
    
    SCROLLBAR_RIGHT1 (SMPResourceType.BUTTON),
    SCROLLBAR_RIGHT1_PRESSED (SMPResourceType.BUTTON_PRESSED),
    
    SCROLLBAR_RIGHT2 (SMPResourceType.BUTTON),
    SCROLLBAR_RIGHT2_PRESSED (SMPResourceType.BUTTON_PRESSED),
    ;

    /* BUTTONS END HERE */
	
	private static final Map<String, ImageIndex> ITEM_LOOKUP_BY_NAME = new HashMap<>();
	private static final Map<String, ImageIndex> INSTRUMENT_SUSTAINED_OFF_LOOKUP_BY_NAME = new HashMap<>();
	private static final Map<String, ImageIndex> INSTRUMENT_SUSTAINED_ON_LOOKUP_BY_NAME = new HashMap<>();
	private static final Map<String, ImageIndex> ITEM_GRAY_LOOKUP_BY_NAME = new HashMap<>();
	private static final Map<String, ImageIndex> ITEM_SILHOUETTE_LOOKUP_BY_NAME = new HashMap<>();
	
	static {
		for (ImageIndex imgIdx : ImageIndex.values()) {
			Map<String, ImageIndex> mapToUse;
			String keyToUse = imgIdx.toString();
			
			switch (imgIdx.getType()) {
			case INSTRUMENT, ACCIDENTAL:
				mapToUse = ITEM_LOOKUP_BY_NAME;
				break;
			case INSTRUMENT_SUSTAINED_OFF:
				mapToUse = INSTRUMENT_SUSTAINED_OFF_LOOKUP_BY_NAME;
				keyToUse = keyToUse.replace("_SM", "");
				break;
			case INSTRUMENT_SUSTAINED_ON:
				mapToUse = INSTRUMENT_SUSTAINED_ON_LOOKUP_BY_NAME;
				keyToUse = keyToUse.replace("_SMA", "");
				break;
			case INSTRUMENT_GRAY, ACCIDENTAL_GRAY:
				mapToUse = ITEM_GRAY_LOOKUP_BY_NAME;
				keyToUse = keyToUse.replace("_GRAY", "");
				break;
			case INSTRUMENT_SILHOUETTE, ACCIDENTAL_SILHOUETTE:
				mapToUse = ITEM_SILHOUETTE_LOOKUP_BY_NAME;
				keyToUse = keyToUse.replace("_SIL", "");
				break;
			default:
				continue;
			}
			
			mapToUse.put(keyToUse, imgIdx);
		}
	}
    
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
	
	public static ImageIndex ofItem(String name) {
		return ITEM_LOOKUP_BY_NAME.getOrDefault(name, NONE);
	}
	
	public static ImageIndex ofInstrumentSustainedOff(String name) {
		return INSTRUMENT_SUSTAINED_OFF_LOOKUP_BY_NAME.getOrDefault(name, NONE);
	}
	
	public static ImageIndex ofInstrumentSustainedOn(String name) {
		return INSTRUMENT_SUSTAINED_ON_LOOKUP_BY_NAME.getOrDefault(name, NONE);
	}
	
	public static ImageIndex ofItemGray(String name) {
		return ITEM_GRAY_LOOKUP_BY_NAME.getOrDefault(name, NONE);
	}
	
	public static ImageIndex ofItemSilhouette(String name) {
		return ITEM_SILHOUETTE_LOOKUP_BY_NAME.getOrDefault(name, NONE);
	}

}
