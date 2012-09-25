package smp.components.staff.sequences;

/**
 * Tells where a StaffNote is located in a line of notes.
 * on the staff.
 * @author RehdBlob
 * @since 2012.08.31
 */
public enum StaffNoteIndex {

    Low_Ab  (-1),
    Low_A   (0),
    Low_As  (1),
    Low_Bb  (1),
    Low_B   (2),
    Low_Cb  (2),
    Low_Bs  (3),
    Low_C   (3),
    Low_Cs  (4),
    Low_Db  (4),
    Low_D   (5),
    Low_Ds  (6),
    Low_Eb  (7),
    Low_E   (8),
    Low_Fb  (8),
    Low_Es  (9),
    Low_F   (10),
    Low_Fs  (11),
    Low_Gb  (11),
    Low_G   (12),
    Low_Gs  (13),
    Ab      (13),
    A       (14),
    As      (15),
    Bb      (15),
    B       (16),
    Cb      (16),
    Bs      (17),
    C       (18),
    Cs      (19),
    Db      (19),
    D       (20),
    Ds      (21),
    Eb      (21),
    E       (22),
    Fb      (22),
    Es      (23),
    F       (24),
    Fs      (25),
    Gb      (25),
    G       (26),
    Gs      (27),
    High_Ab (27),
    High_A  (28),
    High_As (29),
    High_Bb (29),
    High_B  (30),
    High_Cb (30),
    High_Bs (31),
    High_C  (31),
    High_Cs (32),
    High_Db (32),
    High_D  (33),
    High_Ds (34),
    High_E  (35);

    /**
     * An integer that denotes which note corresponds to which index number.
     */
    private int coordinate;

    /**
     * Sets the coordinate of this index.
     * @param num The coordinate of this index.
     */
    private StaffNoteIndex(int num) {
        coordinate = num;
    }

    /**
     * Gives the representation of a note in integer format.
     * @return <code>coordinate</code>, the location of whatever note
     * index we are trying to look up.
     */
    public int coordinate() {
        return coordinate;
    }
}
