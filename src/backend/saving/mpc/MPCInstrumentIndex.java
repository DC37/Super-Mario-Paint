package backend.saving.mpc;

import java.util.HashMap;
import java.util.Map;

import gui.SMPInstrument;

/**
 * Links an <code>InstrumentIndex</code> to a character that
 * appears in an Mario Paint Composer text file.
 * @author RehdBlob
 * @author Aura Lesse Programmer
 * @since 2012.09.11
 */
public enum MPCInstrumentIndex {
    UNKNOWN('?', null),
    A ('a', SMPInstrument.MARIO),
    B ('b', SMPInstrument.MUSHROOM),
    C ('c', SMPInstrument.YOSHI),
    D ('d', SMPInstrument.STAR),
    E ('e', SMPInstrument.FLOWER),
    F ('f', SMPInstrument.GAMEBOY),
    G ('g', SMPInstrument.DOG),
    H ('h', SMPInstrument.CAT),
    I ('i', SMPInstrument.PIG),
    J ('j', SMPInstrument.SWAN),
    K ('k', SMPInstrument.FACE),
    L ('l', SMPInstrument.PLANE),
    M ('m', SMPInstrument.BOAT),
    N ('n', SMPInstrument.CAR),
    O ('o', SMPInstrument.HEART),
    P ('p', SMPInstrument.COIN),
    Q ('q', SMPInstrument.PIRANHA),
    R ('r', SMPInstrument.SHYGUY),
    S ('s', SMPInstrument.BOO);
    
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
     * The <code>SMPInstrument</code> that the MPCInstrumentIndex
     * should be linked to.
     */
    private SMPInstrument ind;

    /**
     * Makes a new MPCInstrumentIndex with a code and an InstrumentIndex.
     * @param c The code character that represents the instrument.
     * @param i The InstrumentIndex that is to be linked to the letter.
     */
    private MPCInstrumentIndex(char c, SMPInstrument i) {
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
    public SMPInstrument getInstrument() {
        return ind;
    }

    /**
     * @param c Some <b>char</b> in a Mario Paint Composer song.
     * @return The <code>InstrumentIndex</code> of the letter.
     */
    public static SMPInstrument valueOf(char c) {
        return CODE_MAPPINGS.getOrDefault(c, UNKNOWN).getInstrument();
    }
    
}
