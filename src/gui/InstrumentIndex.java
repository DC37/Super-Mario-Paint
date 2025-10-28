package gui;

import gui.loaders.ImageIndex;

/**
 * An enum that keeps track of the different instrument types that one can
 * click on in the instrument line.
 * @author RehdBlob
 * @since 2012.08.20
 */
public enum InstrumentIndex {

    MARIO       (1,  ImageIndex.MARIO,		ImageIndex.MARIO_SM, 		ImageIndex.MARIO_SMA),
    MUSHROOM    (2,  ImageIndex.MUSHROOM,	ImageIndex.MUSHROOM_SM, 	ImageIndex.MUSHROOM_SMA),
    YOSHI       (3,  ImageIndex.YOSHI,		ImageIndex.YOSHI_SM,		ImageIndex.YOSHI_SMA),
    STAR        (4,  ImageIndex.STAR,		ImageIndex.STAR_SM,			ImageIndex.STAR_SMA),
    FLOWER      (5,  ImageIndex.FLOWER,		ImageIndex.FLOWER_SM,		ImageIndex.FLOWER_SMA),
    GAMEBOY     (6,  ImageIndex.GAMEBOY,	ImageIndex.GAMEBOY_SM,		ImageIndex.GAMEBOY_SMA),
    DOG         (7,  ImageIndex.DOG,		ImageIndex.DOG_SM,			ImageIndex.DOG_SMA),
    CAT         (8,  ImageIndex.CAT,		ImageIndex.CAT_SM,			ImageIndex.CAT_SMA),
    PIG         (9,  ImageIndex.PIG,		ImageIndex.PIG_SM,			ImageIndex.PIG_SMA),
    SWAN        (10, ImageIndex.SWAN,		ImageIndex.SWAN_SM,			ImageIndex.SWAN_SMA),
    FACE        (11, ImageIndex.FACE,		ImageIndex.FACE_SM,			ImageIndex.FACE_SMA),
    PLANE       (12, ImageIndex.PLANE,		ImageIndex.PLANE_SM,		ImageIndex.PLANE_SMA),
    BOAT        (13, ImageIndex.BOAT,		ImageIndex.BOAT_SM,			ImageIndex.BOAT_SMA),
    CAR         (14, ImageIndex.CAR,		ImageIndex.CAR_SM,			ImageIndex.CAR_SMA),
    HEART       (15, ImageIndex.HEART,		ImageIndex.HEART_SM,		ImageIndex.HEART_SMA),
    COIN        (16, ImageIndex.COIN,		ImageIndex.COIN_SM,			ImageIndex.COIN_SMA),
    PIRANHA     (17, ImageIndex.PIRANHA,	ImageIndex.PIRANHA_SM,		ImageIndex.PIRANHA_SMA),
    SHYGUY      (18, ImageIndex.SHYGUY,		ImageIndex.SHYGUY_SM,		ImageIndex.SHYGUY_SMA),
    BOO         (19, ImageIndex.BOO,		ImageIndex.BOO_SM,			ImageIndex.BOO_SMA),
    LUIGI       (20, ImageIndex.LUIGI,		ImageIndex.LUIGI_SM,		ImageIndex.LUIGI_SMA),
    PEACH       (21, ImageIndex.PEACH,		ImageIndex.PEACH_SM,		ImageIndex.PEACH_SMA),
    FEATHER     (22, ImageIndex.FEATHER,	ImageIndex.FEATHER_SM,		ImageIndex.FEATHER_SMA),
    BULLETBILL  (23, ImageIndex.BULLETBILL,	ImageIndex.BULLETBILL_SM,	ImageIndex.BULLETBILL_SMA),
    GOOMBA      (24, ImageIndex.GOOMBA,		ImageIndex.GOOMBA_SM,		ImageIndex.GOOMBA_SMA),
    BOBOMB      (25, ImageIndex.BOBOMB,		ImageIndex.BOBOMB_SM,		ImageIndex.BOBOMB_SMA),
    SPINY       (26, ImageIndex.SPINY,		ImageIndex.SPINY_SM,		ImageIndex.SPINY_SMA),
    FRUIT       (27, ImageIndex.FRUIT,		ImageIndex.FRUIT_SM,		ImageIndex.FRUIT_SMA),
    ONEUP       (28, ImageIndex.ONEUP,		ImageIndex.ONEUP_SM,		ImageIndex.ONEUP_SMA),
    MOON        (29, ImageIndex.MOON,		ImageIndex.MOON_SM,			ImageIndex.MOON_SMA),
    EGG         (30, ImageIndex.EGG,		ImageIndex.EGG_SM,			ImageIndex.EGG_SMA),
    GNOME       (31, ImageIndex.GNOME,		ImageIndex.GNOME_SM,		ImageIndex.GNOME_SMA),
    ;

    /**
     * The channel that the instrument is to be played on. For use
     * by the MultiSynthesizer or SMPSynthesizer.
     */
    private int channel;

    /** Image to use for notes on the staff */
    private ImageIndex image;
    
    /** Image to use for instrument buttons (sustained off) */
    private ImageIndex image_sm;
    
    /** Image to use for instrument buttons (sustained on) */
    private ImageIndex image_sma;

    private InstrumentIndex(int channel, ImageIndex image, ImageIndex image_sm, ImageIndex image_sma) {
        this.channel = channel;
        this.image = image;
        this.image_sm = image_sm;
        this.image_sma = image_sma;
    }

    public int getChannel() {
        return channel;
    }
    
    public ImageIndex imageIndex() {
        return image;
    }
    
    public ImageIndex smImageIndex() {
    	return image_sm;
    }
    
    public ImageIndex smaImageIndex() {
    	return image_sma;
    }

}
