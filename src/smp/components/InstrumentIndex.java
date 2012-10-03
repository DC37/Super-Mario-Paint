package smp.components;

/**
 * An enum that keeps track of the different instrument types that one can
 * click on in the instrument line.
 * @author RehdBlob
 * @since 2012.08.20
 */
public enum InstrumentIndex {

    /*
     * The instruments of Super Mario Paint
     */

    MARIO    (1),
    MUSHROOM (2),
    YOSHI    (3),
    STAR     (4),
    FLOWER   (5),
    GAMEBOY  (6),
    DOG      (7),
    CAT      (8),
    PIG      (9),
    SWAN     (10),
    FACE     (11),
    PLANE    (12),
    BOAT     (13),
    CAR      (14),
    HEART    (15),
    PIRANHA  (16),
    COIN     (17),
    SHYGUY   (18),
    BOO      (19),
    LUIGI    (20);

    /**
     * The channel that the instrument is to be played on. For use
     * by the MultiSynthesizer or SMPSynthesizer.
     */
    private int channel;

    /**
     * Initializes the InstrumentIndex with its intended channel.
     * @param n The integer representing its channel.
     */
    private InstrumentIndex(int n) {
        channel = n;
    }

    /**
     * Gives the channel that this Instrument should be located at.
     * @return An integer that represents what channel the instrument
     * is located at.
     */
    public int getChannel() {
        return channel;
    }

}
