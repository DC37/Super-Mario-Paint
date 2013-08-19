package smp.components.staff;

import java.util.ArrayList;

import javafx.scene.layout.StackPane;

/**
 * This is the matrix of notes behind the staff. We can change the
 * size of this dynamically, or by setting it manually. The reason we
 * even have this class is to make it really easy to just refer to some
 * note on the staff via a coordinate system.
 * The schema for addressing notes is: <br>
 * <b>First number</b>: The vertical line that we are addressing;
 * 0 is the left-most line. <br>
 * <b>Second number</b>: The note number that we are addressing.
 * 0 is the lowest note. In regular SMP, this is the low A note.
 * Random fun fact: some of this was coded at 33000 feet.
 * @author RehdBlob
 * @since 2013.08.18
 *
 */
public class NoteMatrix {

    /**
     * The list of lists that holds the different <code>StackPane</code>
     * objects.
     */
    private ArrayList<ArrayList<StackPane>> matrix;

    /**
     * This is the matrix of flats / sharps / etc.
     */
    private ArrayList<ArrayList<StackPane>> accMatrix;

    /** The number of lines in this note matrix. */
    private int numberOfLines;

    /** The number of notes that are in each note line. */
    private int numberOfNotes;

    /**
     * @param x The number of note lines on the current staff.
     * @param y The number of addressable notes in a line.
     */
    public NoteMatrix(int x, int y) {
        numberOfLines = x;
        numberOfNotes = y;
        matrix = new ArrayList<ArrayList<StackPane>>();
        accMatrix = new ArrayList<ArrayList<StackPane>>();
    }

    /**
     * Gets you a StackPane object based on the coordinate that
     * you give this method.
     * @param x The note line number.
     * @param y The note number.
     * @return Index 0 is the <code>StackPane</code> of the note that
     * is located at the location. Index 1 is the <code>StackPane</code>
     * of the flat / sharp / etc box that it is associated with.
     */
    public StackPane[] getNote(int x, int y) {
        return null;
        // TODO Fix
    }

    /** @return The number of lines in the note matrix. */
    public int numberOfLines() {
        return numberOfLines;
    }

    /** @return The number of notes in a line. */
    public int numberOfNotes() {
        return numberOfNotes;
    }

    /**
     * Adds a new line of notes into the note matrix.
     * @param newLine The line that we want to add into the note matrix.
     */
    public void addLine(ArrayList<StackPane> newLine) {
        matrix.add(newLine);
    }

    /**
     * Adds a new line of accidentals into the note matrix.
     * @param newLine The line that we want to add into the accidental
     * matrix.
     */
    public void addAccLine(ArrayList<StackPane> newLine) {
        accMatrix.add(newLine);
    }

    /**
     * Removes the line of notes at index <code>index</code>.
     * @param index The index of the line of notes that we want to remove.
     */
    public void removeLine(int index) {
        matrix.remove(index);
        accMatrix.remove(index);
    }

}
