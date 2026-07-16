package backend.songs;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an arrangement on the staff, a collection of
 * <code>StaffSequence</code>s and their filepaths.
 *
 * @author RehdBlob
 * @since 2014.12.31
 *
 */
public class StaffArrangement {
	
	/** The name of this arrangement */
	private String name;

    /** This is the list of <code>StaffSequence</code> objects. */
    private final List<StaffSequence> theSequences;

    /** Default constructor. Creates two empty lists. */
    public StaffArrangement() {
        theSequences = new ArrayList<>();
    }
    
    /**
     * Get the name of this arrangement.
     * @return
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
     * @return The list of sequence files.
     */
    public List<StaffSequence> getSequences() {
        return theSequences;
    }
}
