package smp.components.staff;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import smp.ImageLoader;
import smp.components.Values;
import smp.components.staff.sequences.StaffAccidental;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.staff.sequences.StaffSequence;

/**
 * This is the matrix of notes behind the staff. We can change the size of this
 * dynamically, or by setting it manually. The reason we even have this class is
 * to make it really easy to just refer to some note on the staff via a
 * coordinate system. The schema for addressing notes is: <br>
 * <b>First number</b>: The vertical line that we are addressing; 0 is the
 * left-most line. <br>
 * <b>Second number</b>: The note number that we are addressing. 0 is the lowest
 * note. In regular SMP, this is the low A note. Random fun fact: some of this
 * was coded at 33000 feet.
 *
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

    /** This is the matrix of flats / sharps / etc. */
    private ArrayList<ArrayList<StackPane>> accMatrix;

    /** Pointer to the image loader object. */
    private transient ImageLoader il;

    public NoteMatrix(ImageLoader i) {
        il = i;
        matrix = new ArrayList<ArrayList<StackPane>>();
        accMatrix = new ArrayList<ArrayList<StackPane>>();
    }

    /**
     * Gets you an object based on the coordinate that you give this method.
     * This method should help a lot when working on those portions of code that
     * ask the entire staff to update its images. Bypassing the individual
     * StackPane object links should be a lot easier with this here.
     *
     * @param x
     *            The note line number.
     * @param y
     *            The note number.
     * @return Index 0 is the <code>StackPane</code> of the note that is located
     *         at the location. Index 1 is the <code>StackPane</code> of the
     *         flat / sharp / etc box that it is associated with.
     */
    public StackPane[] getNote(int x, int y) {
        StackPane note;
        StackPane acc;
        note = matrix.get(x).get(Values.NOTES_IN_A_LINE - y - 1);
        acc = accMatrix.get(x).get(Values.NOTES_IN_A_LINE - y - 1);
        return new StackPane[] { note, acc };
    }

    /**
     * Adds a new line of notes into the note matrix.
     *
     * @param newLine
     *            The line that we want to add into the note matrix.
     */
    public void addLine(ArrayList<StackPane> newLine) {
        matrix.add(newLine);
    }

    /**
     * Adds a new line of accidentals into the note matrix.
     *
     * @param newLine
     *            The line that we want to add into the accidental matrix.
     */
    public void addAccLine(ArrayList<StackPane> newLine) {
        accMatrix.add(newLine);
    }

    /**
     * Clears the note display on the staff.
     *
     * @param index
     *            The index that we are clearing.
     */
    public synchronized void clearNoteDisplay(int index) {
        ArrayList<StackPane> nt = matrix.get(index);
        ArrayList<StackPane> ac = accMatrix.get(index);
        for (int i = 0; i < Values.NOTES_IN_A_LINE; i++) {
            ObservableList<Node> ntList = nt.get(i).getChildren();
            ObservableList<Node> acList = ac.get(i).getChildren();
            ntList.clear();
            acList.clear();
        }
    }

    /**
     * Repopulates the note display on the staff.
     */
    public void populateNoteDisplay(StaffSequence seq, int currentPosition, int index) {
        StaffNoteLine stl = seq.getLineSafe(currentPosition + index);
        ArrayList<StaffNote> st = stl.getNotes();
        
        for (StaffNote s : st) {
            StackPane[] noteAndAcc = getNote(index, s.getPosition());
            noteAndAcc[0].getChildren().add(s);
            StaffAccidental accidental = new StaffAccidental(s);
            accidental.setImage(il.getSpriteFX(Staff.switchAcc(s
                    .getAccidental())));
            noteAndAcc[1].getChildren().add(accidental);

            if (s.muteNoteVal() == 0) {
                s.setImage(il.getSpriteFX(s.getInstrument().imageIndex()));
            } else if (s.muteNoteVal() == 1) {
                s.setImage(il.getSpriteFX(s.getInstrument().imageIndex().alt()));
            } else {
                s.setImage(il.getSpriteFX(s.getInstrument().imageIndex()
                        .silhouette()));
            }

            s.setVisible(true);
        }
    }

}
