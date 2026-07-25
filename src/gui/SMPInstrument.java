package gui;

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

	MARIO (1, ImageIndex.MARIO, ImageIndex.MARIO_SM, ImageIndex.MARIO_SMA, ImageIndex.MARIO_GRAY, ImageIndex.MARIO_SIL),
	MUSHROOM (2, ImageIndex.MUSHROOM, ImageIndex.MUSHROOM_SM, ImageIndex.MUSHROOM_SMA, ImageIndex.MUSHROOM_GRAY, ImageIndex.MUSHROOM_SIL),
	YOSHI (3, ImageIndex.YOSHI, ImageIndex.YOSHI_SM, ImageIndex.YOSHI_SMA, ImageIndex.YOSHI_GRAY, ImageIndex.YOSHI_SIL),
	STAR (4, ImageIndex.STAR, ImageIndex.STAR_SM, ImageIndex.STAR_SMA, ImageIndex.STAR_GRAY, ImageIndex.STAR_SIL),
	FLOWER (5, ImageIndex.FLOWER, ImageIndex.FLOWER_SM, ImageIndex.FLOWER_SMA, ImageIndex.FLOWER_GRAY, ImageIndex.FLOWER_SIL),
	GAMEBOY (6, ImageIndex.GAMEBOY, ImageIndex.GAMEBOY_SM, ImageIndex.GAMEBOY_SMA, ImageIndex.GAMEBOY_GRAY, ImageIndex.GAMEBOY_SIL),
	DOG (7, ImageIndex.DOG, ImageIndex.DOG_SM, ImageIndex.DOG_SMA, ImageIndex.DOG_GRAY, ImageIndex.DOG_SIL),
	CAT (8, ImageIndex.CAT, ImageIndex.CAT_SM, ImageIndex.CAT_SMA, ImageIndex.CAT_GRAY, ImageIndex.CAT_SIL),
	PIG (9, ImageIndex.PIG, ImageIndex.PIG_SM, ImageIndex.PIG_SMA, ImageIndex.PIG_GRAY, ImageIndex.PIG_SIL),
	SWAN (10, ImageIndex.SWAN, ImageIndex.SWAN_SM, ImageIndex.SWAN_SMA, ImageIndex.SWAN_GRAY, ImageIndex.SWAN_SIL),
	FACE (11, ImageIndex.FACE, ImageIndex.FACE_SM, ImageIndex.FACE_SMA, ImageIndex.FACE_GRAY, ImageIndex.FACE_SIL),
	PLANE (12, ImageIndex.PLANE, ImageIndex.PLANE_SM, ImageIndex.PLANE_SMA, ImageIndex.PLANE_GRAY, ImageIndex.PLANE_SIL),
	BOAT (13, ImageIndex.BOAT, ImageIndex.BOAT_SM, ImageIndex.BOAT_SMA, ImageIndex.BOAT_GRAY, ImageIndex.BOAT_SIL),
	CAR (14, ImageIndex.CAR, ImageIndex.CAR_SM, ImageIndex.CAR_SMA, ImageIndex.CAR_GRAY, ImageIndex.CAR_SIL),
	HEART (15, ImageIndex.HEART, ImageIndex.HEART_SM, ImageIndex.HEART_SMA, ImageIndex.HEART_GRAY, ImageIndex.HEART_SIL),
	PIRANHA (17, ImageIndex.PIRANHA, ImageIndex.PIRANHA_SM, ImageIndex.PIRANHA_SMA, ImageIndex.PIRANHA_GRAY, ImageIndex.PIRANHA_SIL),
	COIN (16, ImageIndex.COIN, ImageIndex.COIN_SM, ImageIndex.COIN_SMA, ImageIndex.COIN_GRAY, ImageIndex.COIN_SIL),
	SHYGUY (18, ImageIndex.SHYGUY, ImageIndex.SHYGUY_SM, ImageIndex.SHYGUY_SMA, ImageIndex.SHYGUY_GRAY, ImageIndex.SHYGUY_SIL),
	BOO (19, ImageIndex.BOO, ImageIndex.BOO_SM, ImageIndex.BOO_SMA, ImageIndex.BOO_GRAY, ImageIndex.BOO_SIL),
	LUIGI (20, ImageIndex.LUIGI, ImageIndex.LUIGI_SM, ImageIndex.LUIGI_SMA, ImageIndex.LUIGI_GRAY, ImageIndex.LUIGI_SIL),
	PEACH (21, ImageIndex.PEACH, ImageIndex.PEACH_SM, ImageIndex.PEACH_SMA, ImageIndex.PEACH_GRAY, ImageIndex.PEACH_SIL),
	FEATHER (22, ImageIndex.FEATHER, ImageIndex.FEATHER_SM, ImageIndex.FEATHER_SMA, ImageIndex.FEATHER_GRAY, ImageIndex.FEATHER_SIL),
	BULLETBILL (23, ImageIndex.BULLETBILL, ImageIndex.BULLETBILL_SM, ImageIndex.BULLETBILL_SMA, ImageIndex.BULLETBILL_GRAY, ImageIndex.BULLETBILL_SIL),
	GOOMBA (24, ImageIndex.GOOMBA, ImageIndex.GOOMBA_SM, ImageIndex.GOOMBA_SMA, ImageIndex.GOOMBA_GRAY, ImageIndex.GOOMBA_SIL),
	BOBOMB (25, ImageIndex.BOBOMB, ImageIndex.BOBOMB_SM, ImageIndex.BOBOMB_SMA, ImageIndex.BOBOMB_GRAY, ImageIndex.BOBOMB_SIL),
	SPINY (26, ImageIndex.SPINY, ImageIndex.SPINY_SM, ImageIndex.SPINY_SMA, ImageIndex.SPINY_GRAY, ImageIndex.SPINY_SIL),
	FRUIT (27, ImageIndex.FRUIT, ImageIndex.FRUIT_SM, ImageIndex.FRUIT_SMA, ImageIndex.FRUIT_GRAY, ImageIndex.FRUIT_SIL),
	ONEUP (28, ImageIndex.ONEUP, ImageIndex.ONEUP_SM, ImageIndex.ONEUP_SMA, ImageIndex.ONEUP_GRAY, ImageIndex.ONEUP_SIL),
	MOON (29, ImageIndex.MOON, ImageIndex.MOON_SM, ImageIndex.MOON_SMA, ImageIndex.MOON_GRAY, ImageIndex.MOON_SIL),
	EGG (30, ImageIndex.EGG, ImageIndex.EGG_SM, ImageIndex.EGG_SMA, ImageIndex.EGG_GRAY, ImageIndex.EGG_SIL),
	GNOME (31, ImageIndex.GNOME, ImageIndex.GNOME_SM, ImageIndex.GNOME_SMA, ImageIndex.GNOME_GRAY, ImageIndex.GNOME_SIL),
    ;
	
	/**
     * The channel that the instrument is to be played on. For use
     * by the MultiSynthesizer or SMPSynthesizer.
     */
    private int channel;
	
	/** Image to use for notes on the staff */
    private ImageIndex imageIndex;
    
    /** Image to use for instrument buttons (sustained off) */
    private ImageIndex imgIdxSustainOff;
    
    /** Image to use for instrument buttons (sustained on) */
    private ImageIndex imgIdxSustainOn;
    
    /** Image to use when grayed out */
    private ImageIndex imgIdxGray;
    
    /** Image to use for silhouettes */
    private ImageIndex imgIdxSilhouette;
    
    private SMPInstrument(int channel, ImageIndex imageIndex,
    		ImageIndex imgIdxSustainOff, ImageIndex imgIdxSustainOn,
    		ImageIndex imgIdxGray, ImageIndex imgIdxSilhouette) {
    	
        this.channel = channel;
        this.imageIndex = imageIndex;
        this.imgIdxSustainOff = imgIdxSustainOff;
        this.imgIdxSustainOn = imgIdxSustainOn;
        this.imgIdxGray = imgIdxGray;
        this.imgIdxSilhouette = imgIdxSilhouette;
    }
    
    public int getChannel() {
    	return channel;
    }
    
    public ImageIndex getImageIndex() {
    	return imageIndex;
    }
	
    public ImageIndex getImgIdxSustainOff() {
    	return imgIdxSustainOff;
    }
    
    public ImageIndex getImgIdxSustainOn() {
    	return imgIdxSustainOn;
    }
    
    public ImageIndex getImgIdxGray() {
    	return imgIdxGray;
    }
    
    public ImageIndex getImgIdxSilhouette() {
    	return imgIdxSilhouette;
    }
    
}
