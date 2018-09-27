package smp.models;

import java.io.Serializable;
import java.util.ArrayList;

import smp.components.Values;

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

    /**
     * This is the list of note volumes (we will use this later as an extension
     * to the usual volume-bar-sets-the-volume-of-the-whole-line thing.
     */
    private ArrayList<Integer> volumes;

    /** This is the volume of the entire <code>StaffNoteLine</code> */
    private int volume;

    /** This ArrayList holds staff notes inside it. */
    private ArrayList<StaffNote> notes;

    /**
     * Creates a new staff note line with the specified
     * line number.
     */
    public StaffNoteLine() {
        notes = new ArrayList<StaffNote>();
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

    /** @return The list of volumes of the different notes. */
    public ArrayList<Integer> getVolumes() {
        return volumes;
    }

    /** @return The volume of this <code>StaffNoteLine</code>. */
    public int getVolume() {
        return volume;
    }

    /**
     * @param y The volume that we want to set this note line to.
     */
    public void setVolume(double y) {
        if (volume >= Values.MIN_VELOCITY &&
                volume <= Values.MAX_VELOCITY)
            volume = (int) y;
    }

    /**
     * @param vol A percentage (between 0 and 1) that we want
     * to scale this volume by.
     */
    public void setVolumePercent(double vol) {
        if (vol >= 0 && vol <= 1)
            volume = (int) (vol * Values.MAX_VELOCITY);
    }

    /**
     * @return The percent volume of this StaffNoteLine.
     */
    public double getVolumePercent() {
        return ((double) volume) / Values.MAX_VELOCITY;
    }

    @Override
    public String toString() {
        return notes.toString();
    }


}
