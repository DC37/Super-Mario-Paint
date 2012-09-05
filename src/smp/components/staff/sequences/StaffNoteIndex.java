package smp.components.staff.sequences;

/**
 * Tells where a StaffNote is located in a line of notes.
 * on the staff.
 * @author RehdBlob
 * @since 2012.08.31
 */
public enum StaffNoteIndex {

    Low_A (0), A(12), High_A(24);

    private int coordinate;

    private StaffNoteIndex(int num) {
        coordinate = num;
    }

    public int coordinate() {
        return coordinate;
    }
}
