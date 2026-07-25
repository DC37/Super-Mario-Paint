package backend.songs;

import gui.loaders.ImageIndex;

/**
 * Every note alteration that we allow.
 */
public enum Accidental {
    
    DOUBLE_FLAT(-2, ImageIndex.DOUBLEFLAT, ImageIndex.DOUBLEFLAT_GRAY, ImageIndex.DOUBLEFLAT_SIL),
    FLAT(-1, ImageIndex.FLAT, ImageIndex.FLAT_GRAY, ImageIndex.FLAT_SIL),
    NATURAL(0, ImageIndex.BLANK, ImageIndex.BLANK, ImageIndex.BLANK),
    SHARP(1, ImageIndex.SHARP, ImageIndex.SHARP_GRAY, ImageIndex.SHARP_SIL),
    DOUBLE_SHARP(2, ImageIndex.DOUBLESHARP, ImageIndex.DOUBLESHARP_GRAY, ImageIndex.DOUBLESHARP_SIL);
    
    private int offset;
    private ImageIndex imageIndex;
    private ImageIndex imgIdxGray;
    private ImageIndex imgIdxSilhouette;
    
    private Accidental(int value, ImageIndex imageIndex,
    		ImageIndex imgIdxGray, ImageIndex imgIdxSilhouette) {
    	
        this.offset = value;
        this.imageIndex = imageIndex;
        this.imgIdxGray = imgIdxGray;
        this.imgIdxSilhouette = imgIdxSilhouette;
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
        return this.offset;
    }
    
    /**
     * Index of the default image associated with this accidental
     * @return Image index of this accidental
     */
    public ImageIndex getImageIndex() {
        return imageIndex;
    }
    
    /**
     * Index of the gray image associated with this accidental
     * @return Image index of this accidental
     */
    public ImageIndex getImgIdxGray() {
    	return imgIdxGray;
    }
    
    /**
     * Index of the silhouette image associated with this accidental
     * @return Image index of this accidental
     */
    public ImageIndex getImgIdxSilhouette() {
    	return imgIdxSilhouette;
    }

}
