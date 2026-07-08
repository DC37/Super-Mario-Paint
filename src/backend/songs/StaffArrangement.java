package backend.songs;

import java.io.File;
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

    /** This is the list of <code>StaffSequence</code> file locations. */
    private final List<File> theSequenceFiles;

    /** This is the list of sequence / song names. */
    private final List<String> theSequenceNames;

    /** Default constructor. Creates two empty lists. */
    public StaffArrangement() {
        theSequences = new ArrayList<>();
        theSequenceFiles = new ArrayList<>();
        theSequenceNames = new ArrayList<>();
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
     * Removes the sequence at index i of the list. This returns and object
     * array of size 2 with the first element as the <code>StaffSequence</code>
     * that has been removed and the second element as the File location of the
     * StaffSequence that has been removed.
     *
     * @param i
     *            The index to remove from.
     * @return The removed StaffSequence and file.
     */
    public Object[] remove(int i) {
        Object[] ray = { null, null };
        try {
            theSequences.get(i);
            ray[0] = theSequences.remove(i);
            ray[1] = theSequenceFiles.remove(i);
            return ray;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * @return The list of sequence files.
     */
    public List<StaffSequence> getTheSequences() {
        return theSequences;
    }

    /**
     * @return The list of file locations for the sequence files.
     */
    public List<File> getTheSequenceFiles() {
        return theSequenceFiles;
    }

    /**
     * @return the The names of the files in this arrangement.
     */
    public List<String> getTheSequenceNames() {
        return theSequenceNames;
    }
}
