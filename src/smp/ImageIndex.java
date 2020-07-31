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

    /** No image */
    NONE,

    /** Blank image. */
    BLANK (NONE, NONE),

    /*
     * Instruments grayed out.
     */
    MARIO_GRAY, MUSHROOM_GRAY, YOSHI_GRAY, STAR_GRAY, FLOWER_GRAY,
    GAMEBOY_GRAY, DOG_GRAY, CAT_GRAY, PIG_GRAY, SWAN_GRAY, FACE_GRAY,
    PLANE_GRAY, BOAT_GRAY, CAR_GRAY, HEART_GRAY, PIRANHA_GRAY, COIN_GRAY,
    SHYGUY_GRAY, BOO_GRAY, LUIGI_GRAY, PEACH_GRAY, FEATHER_GRAY, BULLETBILL_GRAY,
    GOOMBA_GRAY, BOBOMB_GRAY, SPINY_GRAY, FRUIT_GRAY, ONEUP_GRAY, MOON_GRAY,
    EGG_GRAY, GNOME_GRAY,
    

    SHARP_GRAY, FLAT_GRAY, DOUBLESHARP_GRAY, DOUBLEFLAT_GRAY,

    /* Silhouettes of images. */
    MARIO_SIL, MUSHROOM_SIL, YOSHI_SIL, STAR_SIL, FLOWER_SIL,
    GAMEBOY_SIL, DOG_SIL, CAT_SIL, PIG_SIL, SWAN_SIL, FACE_SIL,
    PLANE_SIL, BOAT_SIL, CAR_SIL, HEART_SIL, PIRANHA_SIL, COIN_SIL,
    SHYGUY_SIL, BOO_SIL, LUIGI_SIL, PEACH_SIL, FEATHER_SIL, BULLETBILL_SIL,
    GOOMBA_SIL, BOBOMB_SIL, SPINY_SIL, FRUIT_SIL, ONEUP_SIL, MOON_SIL,
    EGG_SIL, GNOME_SIL,

    /* The highlighted button line image of the instruments. */
    MARIO_SMA, MUSHROOM_SMA, YOSHI_SMA, STAR_SMA, FLOWER_SMA,
    GAMEBOY_SMA, DOG_SMA, CAT_SMA, PIG_SMA, SWAN_SMA, FACE_SMA,
    PLANE_SMA, BOAT_SMA, CAR_SMA, HEART_SMA, PIRANHA_SMA, COIN_SMA,
    SHYGUY_SMA, BOO_SMA, LUIGI_SMA, PEACH_SMA, FEATHER_SMA, BULLETBILL_SMA,
    GOOMBA_SMA, BOBOMB_SMA, SPINY_SMA, FRUIT_SMA, ONEUP_SMA, MOON_SMA,
    EGG_SMA, GNOME_SMA,

    SHARP_SIL, FLAT_SIL, DOUBLESHARP_SIL, DOUBLEFLAT_SIL,
    VOL_BAR, VOL_BAR2,

    /*
     * Instruments.
     */
    MARIO   (MARIO_GRAY, MARIO_SIL),
    MUSHROOM(MUSHROOM_GRAY, MUSHROOM_SIL),
    YOSHI   (YOSHI_GRAY, YOSHI_SIL),
    STAR    (STAR_GRAY, STAR_SIL),
    FLOWER  (FLOWER_GRAY, FLOWER_SIL),
    GAMEBOY (GAMEBOY_GRAY, GAMEBOY_SIL),
    DOG     (DOG_GRAY, DOG_SIL),
    CAT     (CAT_GRAY, CAT_SIL),
    PIG     (PIG_GRAY, PIG_SIL),
    SWAN    (SWAN_GRAY, SWAN_SIL),
    FACE    (FACE_GRAY, FACE_SIL),
    PLANE   (PLANE_GRAY, PLANE_SIL),
    BOAT    (BOAT_GRAY, BOAT_SIL),
    CAR     (CAR_GRAY, CAR_SIL),
    HEART   (HEART_GRAY, HEART_SIL),
    PIRANHA (PIRANHA_GRAY, PIRANHA_SIL),
    COIN    (COIN_GRAY, COIN_SIL),
    SHYGUY  (SHYGUY_GRAY, SHYGUY_SIL),
    BOO     (BOO_GRAY, BOO_SIL),
    LUIGI   (LUIGI_GRAY, LUIGI_SIL),
    PEACH	(PEACH_GRAY, PEACH_SIL),
    FEATHER	(FEATHER_GRAY, FEATHER_SIL),
    BULLETBILL	(BULLETBILL_GRAY, BULLETBILL_SIL),
    GOOMBA	(GOOMBA_GRAY, GOOMBA_SIL),
    BOBOMB	(BOBOMB_GRAY, BOBOMB_SIL),
    SPINY	(SPINY_GRAY, SPINY_SIL),
    FRUIT	(FRUIT_GRAY, FRUIT_SIL),
    ONEUP	(ONEUP_GRAY, ONEUP_SIL),
    MOON	(MOON_GRAY, MOON_SIL),
    EGG		(EGG_GRAY, EGG_SIL),
    GNOME	(GNOME_GRAY, GNOME_SIL),
    SHARP   (SHARP_GRAY, SHARP_SIL),
    FLAT    (FLAT_GRAY, FLAT_SIL),
    DOUBLESHARP (DOUBLESHARP_GRAY, DOUBLESHARP_SIL),
    DOUBLEFLAT  (DOUBLEFLAT_GRAY, DOUBLEFLAT_SIL),

    MARIO_SM   (MARIO_SMA, NONE),
    MUSHROOM_SM(MUSHROOM_SMA, NONE),
    YOSHI_SM   (YOSHI_SMA, NONE),
    STAR_SM    (STAR_SMA, NONE),
    FLOWER_SM  (FLOWER_SMA, NONE),
    GAMEBOY_SM (GAMEBOY_SMA, NONE),
    DOG_SM     (DOG_SMA, NONE),
    CAT_SM     (CAT_SMA, NONE),
    PIG_SM     (PIG_SMA, NONE),
    SWAN_SM    (SWAN_SMA, NONE),
    FACE_SM    (FACE_SMA, NONE),
    PLANE_SM   (PLANE_SMA, NONE),
    BOAT_SM    (BOAT_SMA, NONE),
    CAR_SM     (CAR_SMA, NONE),
    HEART_SM   (HEART_SMA, NONE),
    PIRANHA_SM (PIRANHA_SMA, NONE),
    COIN_SM    (COIN_SMA, NONE),
    SHYGUY_SM  (SHYGUY_SMA, NONE),
    BOO_SM     (BOO_SMA, NONE),
    LUIGI_SM   (LUIGI_SMA, NONE),
    PEACH_SM   (PEACH_SMA, NONE),
    FEATHER_SM (FEATHER_SMA, NONE),
    BULLETBILL_SM(BULLETBILL_SMA, NONE),
    GOOMBA_SM  (GOOMBA_SMA, NONE),
    BOBOMB_SM (BOBOMB_SMA, NONE),
    SPINY_SM    (SPINY_SMA, NONE),
    FRUIT_SM  (FRUIT_SMA, NONE),
    ONEUP_SM     (ONEUP_SMA, NONE),
    MOON_SM   (MOON_SMA, NONE),
    EGG_SM   (EGG_SMA, NONE),
    GNOME_SM (GNOME_SMA, NONE),
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
    STAFF_FRAME,

    /**
     * The background of the staff, which contains a treble clef.
     */
    STAFF_BG,

    /**
     * Each one of these lines indicates a "beat"
     */
    STAFF_LINE,

    /** Each one of these lines indicates a measure. */
    STAFF_MLINE,

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
     * Controls elements
     */
    CONTROLS_LEFT, CONTROLS_MID, CONTROLS_RIGHT,

    /**
     * Button elements
     */
    STOP_PRESSED, STOP_RELEASED, STOP_LABEL,
    PLAY_PRESSED, PLAY_RELEASED, PLAY_LABEL,
    LOOP_PRESSED, LOOP_RELEASED, LOOP_LABEL,
    MUTE_PRESSED, MUTE_RELEASED, MUTE_LABEL,
    MUTE_A_PRESSED, MUTE_A_RELEASED, MUTE_A_LABEL,
    CLIPBOARD_PRESSED, CLIPBOARD_RELEASED, CLIPBOARD_LABEL,

    SCROLLBAR_LEFT1, SCROLLBAR_LEFT2,
    SCROLLBAR_LEFT1_PRESSED, SCROLLBAR_LEFT2_PRESSED,
    SCROLLBAR_RIGHT1, SCROLLBAR_RIGHT2,
    SCROLLBAR_RIGHT1_PRESSED, SCROLLBAR_RIGHT2_PRESSED,

    /*
     * Tempo stuff.
     */
    TEMPO_PLUS, TEMPO_MINUS, TEMPO_LABEL,

    /** Digits for the ImageViews of the measure lines. */
    ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, ELEVEN, TWELVE;


    /** The alt version of this sprite. It can be null. */
    private ImageIndex alternate = null;

    /** The silhouette version of this sprite. It can be null. */
    private ImageIndex silhouette = null;

    /**
     * @param alt The alternate version of this sprite.
     * @param sil The silhouette version of this sprite.
     */
    private ImageIndex(ImageIndex alt, ImageIndex sil) {
        alternate = alt;
        silhouette = sil;
    }

    /**
     * Default constructor.
     */
    private ImageIndex() {

    }

    /** @return The <code>ImageIndex</code> of the alternate figure. */
    public ImageIndex alt() {
        return alternate;
    }

    /** @return The <code>ImageIndex</code> of the silhouette figure. */
    public ImageIndex silhouette() {
        return silhouette;
    }

}
