package backend.songs;

import gui.loaders.ImageIndex;

/**
 * Every note alteration that we allow.
 */
public enum Accidental {
    
    DOUBLE_FLAT(-2, ImageIndex.DOUBLEFLAT),
    FLAT(-1, ImageIndex.FLAT),
    NATURAL(0, ImageIndex.BLANK),
    SHARP(1, ImageIndex.SHARP),
    DOUBLE_SHARP(2, ImageIndex.DOUBLESHARP);
	
    private int offset;
    private ImageIndex imageIndex;
    
    private Accidental(int value, ImageIndex imageIndex) {
        this.offset = value;
        this.imageIndex = imageIndex;
    }
    
    /**
     * Get an {@code Accidental} from an offset value
     * @param v Integer between {@code -2} and {@code 2}
     * @return Accidental whose offset value is {@code v}
     * @throws IllegalArgumentException if {@code v} is not in the specified
     * 		range
     */
    public static Accidental valueOf(int v) {
        switch (v) {
        case -2:
            return DOUBLE_FLAT;
        case -1:
            return FLAT;
        case 0:
        	return NATURAL;
        case 1:
            return SHARP;
        case 2:
            return DOUBLE_SHARP;
        default:
            throw new IllegalArgumentException("Cannot create Accidental from value " + v);
        }
    }
    
    /**
     * Given some {@link Pitch} value, applying this offset will result in the
     * value of the altered note.
     * @return The pitch offset
     */
    public int getOffset() {
        return this.offset;
    }
    
    /**
     * Index of the image associated with this accidental
     * @return Image index of this accidental
     */
    public ImageIndex imageIndex() {
        return this.imageIndex;
    }

}
