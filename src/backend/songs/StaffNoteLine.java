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
    final private List<StaffNote> notes;

    /**
     * Create an empty line of notes at the default volume.
     */
    public StaffNoteLine() {
    	this(Values.DEFAULT_VELOCITY);
    }
    
    /**
     * Create an empty line of notes at a specified volume.
     * @param volume The volume
     */
    public StaffNoteLine(int volume) {
        this.notes = new ArrayList<StaffNote>();
        this.volume = volume;
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
    public void setVolume(int y) {
        if (volume >= Values.MIN_VELOCITY &&
                volume <= Values.MAX_VELOCITY)
            volume = y;
    }

    @Override
    public String toString() {
        return notes.toString();
    }


}
