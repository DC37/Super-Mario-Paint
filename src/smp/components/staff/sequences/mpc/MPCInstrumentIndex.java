package smp.components.staff.sequences.mpc;

import smp.components.topPanel.instrumentLine.InstrumentIndex;

/**
 * Links an <code>InstrumentIndex</code> to a character that
 * appears in an Mario Paint Composer text file.
 * @author RehdBlob
 * @since 2012.09.11
 */
public enum MPCInstrumentIndex {
    a (InstrumentIndex.MARIO),
    b (InstrumentIndex.MUSHROOM),
    c (InstrumentIndex.YOSHI),
    d (InstrumentIndex.STAR),
    e (InstrumentIndex.FLOWER),
    f (InstrumentIndex.GAMEBOY),
    g (InstrumentIndex.DOG),
    h (InstrumentIndex.CAT),
    i (InstrumentIndex.PIG),
    j (InstrumentIndex.SWAN),
    k (InstrumentIndex.FACE),
    l (InstrumentIndex.PLANE),
    m (InstrumentIndex.BOAT),
    n (InstrumentIndex.CAR),
    o (InstrumentIndex.HEART),
    p (InstrumentIndex.PIRANHA),
    q (InstrumentIndex.SHYGUY),
    r (InstrumentIndex.COIN),
    s (InstrumentIndex.BOO);

    /**
     * The <code>InstrumentIndex</code> that the MPCInstrumentIndex
     * should be linked to.
     */
    private InstrumentIndex ind;

    /**
     * Makes a new MPCInstrumentIndex with an InstrumentIndex.
     * @param i The InstrumentIndex that is to be linked to the letter.
     */
    private MPCInstrumentIndex(InstrumentIndex i) {
        ind = i;
    }

    /**
     * @return The InstrumentIndex that this MPCInstrumentIndex happens
     * to be linked to.
     */
    public InstrumentIndex getInstrument() {
        return ind;
    }

    /**
     * @param c Some <b>char</b> in a Mario Paint Composer song.
     * @return The <code>InstrumentIndex</code> of the
     */
    public static InstrumentIndex valueOf(char c) {
        String str = "" + c;
        return MPCInstrumentIndex.valueOf(str).getInstrument();
    }


}
