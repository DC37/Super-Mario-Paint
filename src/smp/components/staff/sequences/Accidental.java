package smp.components.staff.sequences;

import smp.ImageIndex;

/**
 * Every note alteration that we allow.
 * @author rozlynd
 * @since 2025.03.09
 */
public enum Accidental {
    
    DOUBLE_FLAT(-2, ImageIndex.DOUBLEFLAT),
    FLAT(-1, ImageIndex.FLAT),
    NATURAL(0, ImageIndex.BLANK),
    SHARP(1, ImageIndex.SHARP),
    DOUBLE_SHARP(2, ImageIndex.DOUBLESHARP);
    
    /**
     * Given some {@link Note} value, applying this offset will result in the
     * value of the altered note.
     */
    private int offset;
    private ImageIndex imageIndex;
    
    private Accidental(int value, ImageIndex imageIndex) {
        this.offset = value;
        this.imageIndex = imageIndex;
    }
    
    public static Accidental valueOf(int v) {
        switch (v) {
        case -2:
            return DOUBLE_FLAT;
        case -1:
            return FLAT;
        case 1:
            return SHARP;
        case 2:
            return DOUBLE_SHARP;
        default:
            return NATURAL;
        }
    }
    
    public int getOffset() {
        return this.offset;
    }
    
    public ImageIndex imageIndex() {
        return this.imageIndex;
    }

}
