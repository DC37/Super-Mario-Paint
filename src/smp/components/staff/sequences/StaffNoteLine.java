package smp.components.staff.sequences;

import java.util.ArrayList;

/**
 * A line of notes on the staff. This can include
 * notes, bookmarks, etc.
 * @author RehdBlob
 *
 */
public class StaffNoteLine {

    /**
     * The line number of this StaffNoteLine. For a standard 96-measure 4/4
     * song, this number is between 0 and 383.
     */
    private int lineNum;

    /**
     * This ArrayList holds staff notes inside it.
     */
    private ArrayList<StaffNote> notes;

    /**
     * This ArrayList holds staff events in it.
     */
    private ArrayList<StaffEvent> marks;

    /**
     * Creates a new staff note line.
     */
    public StaffNoteLine() {

    }

    /**
     * Adds a note to the staff.
     * @param n The note to add to this StaffNoteLine.
     */
    public void add(StaffNote n) {
        notes.add(n);
    }

    /**
     * Deletes the note at the index provided.
     * @param index The index to delete a note from.
     */
    private void delete(int index) {
        notes.remove(index);
    }

    /**
     * Deletes a note from the staff.
     * @param n The note to delete.
     */
    public void delete(StaffNote n) {
        if (!(notes.indexOf(n) >= 0))
            return;
        delete(notes.indexOf(n));
    }

}
