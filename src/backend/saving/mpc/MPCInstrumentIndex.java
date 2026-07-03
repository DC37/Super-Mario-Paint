package backend.saving.mpc;

import java.util.HashMap;
import java.util.Map;

import gui.InstrumentIndex;

/**
 * Links an <code>InstrumentIndex</code> to a character that
 * appears in an Mario Paint Composer text file.
 * @author RehdBlob
 * @since 2012.09.11
 */
public enum MPCInstrumentIndex {
    UNKNOWN('?', null),
    A ('a', InstrumentIndex.MARIO),
    B ('b', InstrumentIndex.MUSHROOM),
    C ('c', InstrumentIndex.YOSHI),
    D ('d', InstrumentIndex.STAR),
    E ('e', InstrumentIndex.FLOWER),
    F ('f', InstrumentIndex.GAMEBOY),
    G ('g', InstrumentIndex.DOG),
    H ('h', InstrumentIndex.CAT),
    I ('i', InstrumentIndex.PIG),
    J ('j', InstrumentIndex.SWAN),
    K ('k', InstrumentIndex.FACE),
    L ('l', InstrumentIndex.PLANE),
    M ('m', InstrumentIndex.BOAT),
    N ('n', InstrumentIndex.CAR),
    O ('o', InstrumentIndex.HEART),
    P ('p', InstrumentIndex.COIN),
    Q ('q', InstrumentIndex.PIRANHA),
    R ('r', InstrumentIndex.SHYGUY),
    S ('s', InstrumentIndex.BOO);
    
    /**
     * A map that transforms character codes to <code>MPCInstrumentIndex</code>es.
     */
    private static final Map<Character, MPCInstrumentIndex> CODE_MAPPINGS = new HashMap<>();
    
    static {
        for (MPCInstrumentIndex ind: values()) {
            CODE_MAPPINGS.put(ind.getCode(), ind);
        }
    }

    /**
     * The code character that represents the MPCInstrumentIndex
     * in a Mario Paint Composer song.
     */
    private char code;
    
    /**
     * The <code>InstrumentIndex</code> that the MPCInstrumentIndex
     * should be linked to.
     */
    private InstrumentIndex ind;

    /**
     * Makes a new MPCInstrumentIndex with a code and an InstrumentIndex.
     * @param c The code character that represents the instrument.
     * @param i The InstrumentIndex that is to be linked to the letter.
     */
    private MPCInstrumentIndex(char c, InstrumentIndex i) {
        code = c;
        ind = i;
    }

    /**
     * @return The code character that this MPCInstrumentIndex uses
     * inside a Mario Paint Composer song.
     */
    public char getCode() {
        return code;
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
     * @return The <code>InstrumentIndex</code> of the letter.
     */
    public static InstrumentIndex valueOf(char c) {
        return CODE_MAPPINGS.getOrDefault(c, UNKNOWN).getInstrument();
    }
    
}
