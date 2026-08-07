package gui;

import java.util.function.Function;

import gui.loaders.ImageIndex;

/**
 * An enum that keeps track of the different instrument types that one can
 * click on in the instrument line.
 * @author RehdBlob
 * @author Aura Lesse Programmer
 * @since 2012.08.20
 */
public enum SMPInstrument {

	// COIN and PIRANHA channel identifiers are swapped to match soundfont conventions

	MARIO (1),
	MUSHROOM (2),
	YOSHI (3),
	STAR (4),
	FLOWER (5),
	GAMEBOY (6),
	DOG (7),
	CAT (8),
	PIG (9),
	SWAN (10),
	FACE (11),
	PLANE (12),
	BOAT (13),
	CAR (14),
	HEART (15),
	PIRANHA (17),
	COIN (16),
	SHYGUY (18),
	BOO (19),
	LUIGI (20),
	PEACH (21),
	FEATHER (22),
	BULLETBILL (23),
	GOOMBA (24),
	BOBOMB (25),
	SPINY (26),
	FRUIT (27),
	ONEUP (28),
	MOON (29),
	EGG (30),
	GNOME (31),
    ;
	
	/**
     * The channel that the instrument is to be played on. For use
     * by the MultiSynthesizer or SMPSynthesizer.
     */
    private int channel;
    
    private SMPInstrument(int channel) {
        this.channel = channel;
    }
    
    public int getChannel() {
    	return channel;
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
