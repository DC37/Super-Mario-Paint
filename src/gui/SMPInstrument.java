package gui;

import java.util.function.Function;

import backend.BackendUtils;
import gui.loaders.ImageIndex;

/**
 * An enum that keeps track of the different instrument types that one can
 * click on in the instrument line.
 * @author RehdBlob
 * @author Aura Lesse Programmer
 * @since 2012.08.20
 */
public enum SMPInstrument {

	// ATTENTION: DO NOT ALTER THE ORDER OF THESE INSTRUMENTS! 

	MARIO,
	MUSHROOM,
	YOSHI,
	STAR,
	FLOWER,
	GAMEBOY,
	DOG,
	CAT,
	PIG,
	SWAN,
	FACE,
	PLANE,
	BOAT,
	CAR,
	HEART,
	PIRANHA,
	COIN,
	SHYGUY,
	BOO,
	LUIGI,
	PEACH,
	FEATHER,
	BULLETBILL,
	GOOMBA,
	BOBOMB,
	SPINY,
	FRUIT,
	ONEUP,
	MOON,
	EGG,
	GNOME,
    ;
    
	/**
	 * Get the physical position of the instrument in the
	 * enumeration of available instruments.
	 * 
	 * @note COIN and PIRANHA channel identifiers are NOT
	 *       swapped in this method; use {@link #getIndex()}
	 *       if you need the swap.
	 * 
	 * @see #getIndex()
	 * 
	 * @return The physical position of the instrument in
	 *         the enumeration.
	 */
	public int getEnumIndex() {
		return ordinal();
	}
	
	/**
	 * Get the instrument index, to be used in note extensions.
	 * 
	 * @note COIN and PIRANHA indexes are swapped to match
     *       soundfont conventions; if you don't need this,
     *       please use {@link #getEnumIndex()}.
     *       
     * @see #getEnumIndex()
	 * 
	 * @return The index number; first index is number zero.
	 */
	public int getIndex() {
		return BackendUtils.swapCoinPiranhaInstrumentIdxs(getEnumIndex());
	}
	
    /**
     * Get the channel that the instrument is to be played on. For
     * use by the MultiSynthesizer or SMPSynthesizer.
     * 
     * @note This method has the COIN and PIRANHA channel identifiers
     *       swapped to match soundfont conventions. 
     * 
     * @see #getIndex()
     * 
     * @returns The channel number; first channel is #1.
     */
    public int getChannel() {
    	// Add one to the index to get the channel number.
    	return getIndex() + 1;
    }
    
    private ImageIndex getImageIndexAs(Function<String, ImageIndex> fnGetImgIdx) {
    	return fnGetImgIdx.apply(toString());
    }
    
    /** Get the image to use for notes on the staff */
    public ImageIndex getImageIndex() {
    	return getImageIndexAs(ImageIndex::ofItem);
    }
	
    /** Get the image to use for instrument buttons (sustained off) */
    public ImageIndex getImgIdxSustainOff() {
    	return getImageIndexAs(ImageIndex::ofInstrumentSustainedOff);
    }
    
    /** Get the image to use for instrument buttons (sustained on) */
    public ImageIndex getImgIdxSustainOn() {
    	return getImageIndexAs(ImageIndex::ofInstrumentSustainedOn);
    }
    
    /** Get the image to use when grayed out */
    public ImageIndex getImgIdxGray() {
    	return getImageIndexAs(ImageIndex::ofItemGray);
    }
    
    /** Get the image to use for silhouettes */
    public ImageIndex getImgIdxSilhouette() {
    	return getImageIndexAs(ImageIndex::ofItemSilhouette);
    }
    
}
