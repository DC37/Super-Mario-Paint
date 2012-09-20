package smp.components.staff.sequences;

/**
 * Tells where a StaffNote is located in a line of notes.
 * on the staff.
 * @author RehdBlob
 * @since 2012.08.31
 */
public enum StaffNoteIndex {

    Low_A (0), A(12), High_A(24), High_B(26), High_C(27), High_D(29);

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
