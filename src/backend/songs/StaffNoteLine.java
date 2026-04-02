package backend.songs;

import java.util.ArrayList;
import java.util.List;

import gui.Values;

/**
 * <p>A vertical line of notes on the staff. These notes all share the same
 * volume value.
 * 
 * <p>This is mostly a wrapper around a list of {@link StaffNote}s. The method
 * {@link getNotes} provides a reference to that modifiable list. The order of
 * notes in the list determines how they are ultimately displayed; the last
 * element will end up at the front. Typically we want that note to be the most
 * recently-placed.
 */
public class StaffNoteLine {

	/**
	 * The volume of this line.
	 */
    private int volume;

    /**
     * The notes on this line.
     */
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
     * Get the reference to the list of notes held by this line.
     * @return The list of notes on this line
     */
    public List<StaffNote> getNotes() {
        return notes;
    }

    /**
     * Get the volume of this line.
     * @return The volume of this line
     */
    public int getVolume() {
        return volume;
    }

    /**
     * Set the volume of this line. The value must be in the range specified by
     * {@link Values#MIN_VELOCITY} and {@link Values#MAX_VELOCITY}. If the
     * parameter is not in the range then the method has no effect.
     * @param volume The volume to set
     */
    public void setVolume(int volume) {
        if (volume >= Values.MIN_VELOCITY && volume <= Values.MAX_VELOCITY)
            this.volume = volume;
    }

    @Override
    public String toString() {
        return notes.toString();
    }

}
