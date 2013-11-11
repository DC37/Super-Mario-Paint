package smp.components.staff.sequences;

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
     * The line number of this StaffNoteLine. For a standard 96-measure 4/4
     * song, this number is between 0 and 383.
     */
    private int lineNum;

    /**
     * This is the list of note volumes (we will use this later as an extension
     * to the usual volume-bar-sets-the-volume-of-the-whole-line thing.
     */
    private ArrayList<Integer> volumes;

    /** This is the volume of the entire <code>StaffNoteLine</code> */
    private int volume;

    /** This ArrayList holds staff notes inside it. */
    private ArrayList<StaffNote> notes;

    /** This ArrayList holds staff events in it. */
    private ArrayList<StaffEvent> marks;

    /**
     * Creates a new staff note line with the specified
     * line number.
     * @param num The line number identifier of this staff note line.
     * We will index starting from zero.
     */
    public StaffNoteLine(int num) {
        lineNum = num;
        notes = new ArrayList<StaffNote>();
        marks = new ArrayList<StaffEvent>();
    }

    /**
     * Adds a note to the staff note line.
     * @param n The note to add to this StaffNoteLine.
     */
    public void add(StaffNote n) {
        notes.add(n);
    }

    /**
     * Adds an event to this staff note line.
     * @param e The event that we are trying to add.
     */
    public void addEvent(StaffEvent e) {
        marks.add(e);
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


    /**
     * Deletes an event from this staff note line.
     * @param e The event that we are trying to remove.
     * @return True if we successfully removed the event.
     */
    public boolean removeEvent(StaffEvent e) {
        return marks.remove(e);
    }

    /**
     * @return The line number that this staff event is located at
     */
    public int getLineNum() {
        return lineNum;
    }

    /**
     * Sets the line number to whatever we feed this method.
     * @param num The line number that we want this staff note line to
     * occur at.
     */
    public void setLineNum(int num) {
        lineNum = num;
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
     * @param vol The volume that we want to set this note line to.
     */
    public void setVolume(int vol) {
        if (volume >= Values.MIN_VELOCITY &&
                volume <= Values.MAX_VELOCITY)
            volume = vol;
    }

    @Override
    public String toString() {
        return "Line: " + lineNum + "\nNotes: " + notes.toString();
    }


}
