package backend.songs;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>An arrangement is a list of {@link StaffSequence}s.
 */
public class StaffArrangement {
	
	/** The name of this arrangement. */
	private String name;

    /** The sequences in this arrangement. */
    private final List<StaffSequence> sequences;

    /** Default constructor. Makes an empty arrangement. */
    public StaffArrangement() {
        sequences = new ArrayList<>();
    }
    
    /**
     * Get the name of this arrangement.
     */
    public String getName() {
    	return this.name;
    }
    
    /**
     * Set the name of this arrangement.
     * @param name A name
     */
    public void setName(String name) {
    	this.name = name;
    }

    /**
     * Get the sequences in this arrangement.
     */
    public List<StaffSequence> getSequences() {
        return sequences;
    }
}
