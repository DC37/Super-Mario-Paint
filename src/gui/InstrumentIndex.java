package gui;

import gui.loaders.ImageIndex;

/**
 * An enum that keeps track of the different instrument types that one can
 * click on in the instrument line.
 * @author RehdBlob
 * @since 2012.08.20
 */
public enum InstrumentIndex {

    MARIO       (1,  ImageIndex.MARIO),
    MUSHROOM    (2,  ImageIndex.MUSHROOM),
    YOSHI       (3,  ImageIndex.YOSHI),
    STAR        (4,  ImageIndex.STAR),
    FLOWER      (5,  ImageIndex.FLOWER),
    GAMEBOY     (6,  ImageIndex.GAMEBOY),
    DOG         (7,  ImageIndex.DOG),
    CAT         (8,  ImageIndex.CAT),
    PIG         (9,  ImageIndex.PIG),
    SWAN        (10, ImageIndex.SWAN),
    FACE        (11, ImageIndex.FACE),
    PLANE       (12, ImageIndex.PLANE),
    BOAT        (13, ImageIndex.BOAT),
    CAR         (14, ImageIndex.CAR),
    HEART       (15, ImageIndex.HEART),
    COIN        (16, ImageIndex.COIN),
    PIRANHA     (17, ImageIndex.PIRANHA),
    SHYGUY      (18, ImageIndex.SHYGUY),
    BOO         (19, ImageIndex.BOO),
    LUIGI       (20, ImageIndex.LUIGI),
    PEACH       (21, ImageIndex.PEACH),
    FEATHER     (22, ImageIndex.FEATHER),
    BULLETBILL  (23, ImageIndex.BULLETBILL),
    GOOMBA      (24, ImageIndex.GOOMBA),
    BOBOMB      (25, ImageIndex.BOBOMB),
    SPINY       (26, ImageIndex.SPINY),
    FRUIT       (27, ImageIndex.FRUIT),
    ONEUP       (28, ImageIndex.ONEUP),
    MOON        (29, ImageIndex.MOON),
    EGG         (30, ImageIndex.EGG),
    GNOME       (31, ImageIndex.GNOME),
    ;

    /**
     * The channel that the instrument is to be played on. For use
     * by the MultiSynthesizer or SMPSynthesizer.
     */
    private int channel;

    /** The <code>ImageIndex</code> that this enum constant is linked to. */
    private ImageIndex theIndex;

    /**
     * Initializes the InstrumentIndex with its intended channel.
     * @param n The integer representing its channel.
     */
    private InstrumentIndex(int n, ImageIndex i) {
        channel = n;
        theIndex = i;
    }

    /**
     * Gives the channel that this Instrument should be located at.
     * @return An integer that represents what channel the instrument
     * is located at. The channel numbering starts at 1.
     */
    public int getChannel() {
        return channel;
    }

    /**
     * @return the <code>ImageIndex</code> that this instrument is linekd to./
     */
    public ImageIndex imageIndex() {
        return theIndex;
    }

}
