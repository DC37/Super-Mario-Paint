package backend.songs;

import java.util.function.Function;

import gui.loaders.ImageIndex;

/**
 * Every note alteration that we allow.
 */
public enum Accidental {
    
    DOUBLE_FLAT(-2, "B"),
    FLAT(-1, "b"),
    NATURAL(0, ""),
    SHARP(1, "#"),
    DOUBLE_SHARP(2, "X");
    
    private int offset;
    private String token;
    
    private Accidental(int value, String token) {
        this.offset = value;
        this.token = token;
    }
    
    /**
     * Get an {@code Accidental} from an offset value
     * @param v Integer between {@code -2} and {@code 2}
     * @return Accidental whose offset value is {@code v}
     * @throws IllegalArgumentException if {@code v} is not in the specified
     *      range
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
        return offset;
    }
    
    /**
     * Get the representation of this {@link Accidental} as viewed when written in a file.
     * @return The representation as a {@link String}.
     */
    public String getToken() {
    	return token;
    }
    
    private ImageIndex getImageIndexAs(
    		Function<String, ImageIndex> fnGetImgIdx) {
    	
    	if ("NATURAL".equals(toString()))
    		return ImageIndex.BLANK;
    	
    	return fnGetImgIdx.apply(toString());
    }
    
    /**
     * Index of the default image associated with this accidental
     * @return Image index of this accidental
     */
    public ImageIndex getImageIndex() {
    	return getImageIndexAs(ImageIndex::ofItem);
    }
    
    /**
     * Index of the gray image associated with this accidental
     * @return Image index of this accidental
     */
    public ImageIndex getImgIdxGray() {
    	return getImageIndexAs(ImageIndex::ofItemGray);
    }
    
    /**
     * Index of the silhouette image associated with this accidental
     * @return Image index of this accidental
     */
    public ImageIndex getImgIdxSilhouette() {
    	return getImageIndexAs(ImageIndex::ofItemSilhouette);
    }

}
