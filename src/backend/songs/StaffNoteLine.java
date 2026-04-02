package backend.songs;

import java.util.ArrayList;
import java.util.List;

import gui.Values;

/**
 * A line of notes on the staff. This can include
 * notes, bookmarks, etc.
 * @author RehdBlob
 * @since 2012.09.19
 */
public class StaffNoteLine {

    /** This is the volume of the entire <code>StaffNoteLine</code> */
    private int volume;

    /** This List holds staff notes inside it. */
    private List<StaffNote> notes;

    /**
     * Creates a new staff note line with the specified
     * line number.
     */
    public StaffNoteLine() {
        notes = new ArrayList<StaffNote>();
    }

    /**
     * @return The list of notes that this <code>StaffNoteLine</code> contains.
     */
    public List<StaffNote> getNotes() {
        return notes;
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
