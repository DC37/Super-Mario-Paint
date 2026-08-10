package backend.songs;

import java.util.HashMap;
import java.util.Map;
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
    
    private static final Map<String, Accidental> LOOKUP_BY_TOKEN = new HashMap<>();
    
    static {
    	for (Accidental acc : Accidental.values()) {
    		LOOKUP_BY_TOKEN.put(acc.getToken(), acc);
    	}
    }
    
    private Accidental(int value, String token) {
        this.offset = value;
        this.token = token;
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
    
    /**
     * Get the {@code Accidental} that corresponds to the
     * specified token.
     *
     * @param token The token to look for.
     * @return The accidental corresponding to that token,
     *         or {@code NATURAL} if not found.
     */
    public static Accidental ofToken(String token) {
    	return LOOKUP_BY_TOKEN.getOrDefault(token, NATURAL);
    }

}
