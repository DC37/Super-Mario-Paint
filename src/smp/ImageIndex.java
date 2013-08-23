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
     * Instruments grayed out.
     */
    MARIO_GRAY, MUSHROOM_GRAY, YOSHI_GRAY, STAR_GRAY, FLOWER_GRAY,
    GAMEBOY_GRAY, DOG_GRAY, CAT_GRAY, PIG_GRAY, SWAN_GRAY, FACE_GRAY,
    PLANE_GRAY, BOAT_GRAY, CAR_GRAY, HEART_GRAY, PIRANHA_GRAY, COIN_GRAY,
    SHYGUY_GRAY, BOO_GRAY, LUIGI_GRAY,

    SHARP_GRAY, FLAT_GRAY, DOUBLESHARP_GRAY, DOUBLEFLAT_GRAY,

    /*
     * Silhouettes of images.
     */
    MARIO_SIL, MUSHROOM_SIL, YOSHI_SIL, STAR_SIL, FLOWER_SIL,
    GAMEBOY_SIL, DOG_SIL, CAT_SIL, PIG_SIL, SWAN_SIL, FACE_SIL,
    PLANE_SIL, BOAT_SIL, CAR_SIL, HEART_SIL, PIRANHA_SIL, COIN_SIL,
    SHYGUY_SIL, BOO_SIL, LUIGI_SIL,

    SHARP_SIL, FLAT_SIL, DOUBLESHARP_SIL, DOUBLEFLAT_SIL,

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
    SHARP   (SHARP_GRAY, SHARP_SIL),
    FLAT    (FLAT_GRAY, FLAT_SIL),
    DOUBLESHARP (DOUBLESHARP_GRAY, DOUBLESHARP_SIL),
    DOUBLEFLAT  (DOUBLEFLAT_GRAY, DOUBLEFLAT_SIL),

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
     * This is the bar that goes across the screen when one hits play.
     */
    PLAY_BAR1,

    /**
     * This is a horizontal line that appears when someone tries to go
     * above or below the middle five lines of the staff.
     */
    STAFF_HLINE,

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

    /** Digits for the ImageViews of the measure lines. */
    ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,

    /** Alphabet for font. */
    A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V,
    W, X, Y, Z,
    a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v,
    w, x, y, z,

    /** No image */
    NONE,

    /** Blank image. */
    BLANK (NONE, NONE);

    /** The gray version of this sprite. It can be null. */
    private ImageIndex gray = null;

    /** The silhouette version of this sprite. It can be null. */
    private ImageIndex silhouette = null;

    /**
     * @param gray The gray version of this sprite.
     * @param sil The silhouette version of this sprite.
     */
    private ImageIndex(ImageIndex gr, ImageIndex sil) {
        gray = gr;
        silhouette = sil;
    }

    /**
     * Default constructor.
     */
    private ImageIndex() {

    }

    /** @return The <code>ImageIndex</code> of the gray figure. */
    public ImageIndex gray() {
        return gray;
    }

    /** @return The <code>ImageIndex</code> of the silhouette figure. */
    public ImageIndex silhouette() {
        return silhouette;
    }

}
