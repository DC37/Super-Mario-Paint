package smp.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A line of notes on the staff. This can include
 * notes, bookmarks, etc.
 * @author RehdBlob
 * @since 2012.09.19
 */
public class StaffNoteLine implements Serializable {

    /**
     * Generated serial ID
     */
    private static final long serialVersionUID = 3876410979457142750L;

    /** This is the volume of the entire <code>StaffNoteLine</code> */
    private int volume;

    /** This ArrayList holds staff notes inside it. */
    private ArrayList<StaffNote> notes = new ArrayList<StaffNote>();

    public StaffNoteLine() {
    }

    /**
     * Adds a note to the staff note line.
     * @param n The note to add to this StaffNoteLine.
     */
    public void add(StaffNote n) {
        notes.add(n);
    }

    /**
     * Deletes a note from the staff.
     * @param n The note to delete.
     * @return True if we successfully removed the note.
     */
    public boolean remove(StaffNote n) {
        return notes.remove(n);
    }

    /**
     * Deletes a note from the staff.
     * @param index The index which we are deleting from.
     * @return The deleted element.
     */
    public StaffNote remove(int index) {
        return notes.remove(index);
    }

    /** @return Whether this StaffNoteLine contains the staff note already. */
    public boolean contains(StaffNote theNote) {
        return notes.contains(theNote);
    }

    /** @return The number of notes that are in this StaffNoteLine. */
    public int size() {
        return notes.size();
    }

    /** @return Whether the StaffNoteLine has any notes or not. */
    public boolean isEmpty() {
        return notes.isEmpty();
    }

    /**
     * @return The list of notes that this <code>StaffNoteLine</code> contains.
     */
    public ArrayList<StaffNote> getNotes() {
        return notes;
    }

    /** @return The volume of this <code>StaffNoteLine</code>. */
    public int getVolume() {
        return volume;
    }

	/**
	 * @param y The volume that we want to set this note line to.
	 */
	public void setVolume(int y) {
		volume = (int) y;
	}

    @Override
    public String toString() {
        return notes.toString();
    }


}
