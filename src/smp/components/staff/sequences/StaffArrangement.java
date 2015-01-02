package smp.components.staff.sequences;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;

/**
 * This is an arrangement on the staff, a collection of
 * <code>StaffSequence</code>s and their filepaths.
 *
 * @author RehdBlob
 * @since 2014.12.31
 *
 */
public class StaffArrangement implements Serializable {

    /**
     * At the moment, we're using object serialization to save things.
     */
    private static final long serialVersionUID = 1L;

    /** This is the list of <code>StaffSequence</code> objects. */
    private transient ArrayList<StaffSequence> theSequences;

    /** This is the list of <code>StaffSequence</code> file locations. */
    private ArrayList<File> theSequenceFiles;

    /** This is the list of sequence / song names. */
    private ArrayList<String> theSequenceNames;

    /** Default constructor. Creates two empty lists. */
    public StaffArrangement() {
        theSequences = new ArrayList<StaffSequence>();
        theSequenceFiles = new ArrayList<File>();
        theSequenceNames = new ArrayList<String>();
    }

    /**
     * Adds a sequence to this arrangement.
     *
     * @param s
     *            The sequence to add.
     * @param f
     *            The file associated with the sequence.
     */
    public void add(StaffSequence s, File f) {
        theSequences.add(s);
        theSequenceFiles.add(f);
    }

    /**
     * Adds a sequence to this arrangement at index <code>i</code>.
     *
     * @param i
     *            The index to add at.
     * @param s
     *            The <code>StaffSequence</code> that we are adding.
     * @param f
     *            The file associated with the sequence.
     */
    public void add(int i, StaffSequence s, File f)
            throws IndexOutOfBoundsException {
        theSequences.add(i, s);
        theSequenceFiles.add(i, f);
    }

    /**
     * Sets a sequence at index <code>i</code> to the specified index.
     *
     * @param s
     *            The sequence to set.
     * @param f
     *            The file associated with the sequence.
     * @param i
     *            The index at which to set the material at.
     */
    public void set(StaffSequence s, File f, int i)
            throws IndexOutOfBoundsException {
        theSequences.set(i, s);
        theSequenceFiles.set(i, f);
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
    public ArrayList<StaffSequence> getTheSequences() {
        return theSequences;
    }

    /**
     * @param t
     *            The list of sequence files to set.
     */
    public void setTheSequences(ArrayList<StaffSequence> t) {
        theSequences = t;
    }

    /**
     * @return The list of file locations for the sequence files.
     */
    public ArrayList<File> getTheSequenceFiles() {
        return theSequenceFiles;
    }

    /**
     * @param tsf
     *            The list of file locations to set.
     */
    public void setTheSequenceFiles(ArrayList<File> tsf) {
        theSequenceFiles = tsf;
    }

    /**
     * @return the The names of the files in this arrangement.
     */
    public ArrayList<String> getTheSequenceNames() {
        return theSequenceNames;
    }

    /**
     * @param list
     *            The filenames we want to set.
     */
    public void setTheSequenceNames(List<String> list) {
        theSequenceNames.addAll(list);
    }

}
