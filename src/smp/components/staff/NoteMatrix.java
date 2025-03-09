package smp.components.staff;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    
    public StackPane getNotes(int x, int y) {
        return matrix.get(x).get(Values.NOTES_IN_A_LINE - y - 1);
    }
    
    public StackPane getAccidentals(int x, int y) {
        return accMatrix.get(x).get(Values.NOTES_IN_A_LINE - y - 1);
    }
    
    public void initializeNoteDisplay(HBox staffInstruments, HBox staffAccidentals) {
        initializeNotesMatrix(staffInstruments);
        initializeAccidentalsMatrix(staffAccidentals);
    }
    
    private void initializeNotesMatrix(HBox staffInstruments) {
        ArrayList<VBox> noteLines = new ArrayList<VBox>();
        for (Node n : staffInstruments.getChildren())
            noteLines.add((VBox) n);

        for (int line = 0; line < noteLines.size(); line++) {
            VBox verticalHolder = noteLines.get(line);

            ObservableList<Node> lineOfNotes = verticalHolder.getChildren();

            ArrayList<StackPane> notes = new ArrayList<StackPane>();

            for (int pos = 1; pos <= Values.NOTES_IN_A_LINE; pos++) {
                StackPane note = (StackPane) lineOfNotes.get(pos - 1);
                
                // Don't register events for stability
                note.setDisable(true);
                notes.add(note);
            }

            matrix.add(notes);
        }
    }
    
    private void initializeAccidentalsMatrix(HBox staffAccidentals) {
        ArrayList<VBox> accidentalLines = new ArrayList<VBox>();
        for (Node n : staffAccidentals.getChildren())
            accidentalLines.add((VBox) n);

        for (int line = 0; line < accidentalLines.size(); line++) {
            VBox accVerticalHolder = accidentalLines.get(line);

            ObservableList<Node> lineOfAcc = accVerticalHolder.getChildren();

            ArrayList<StackPane> accs = new ArrayList<StackPane>();

            for (int pos = 1; pos <= Values.NOTES_IN_A_LINE; pos++) {
                StackPane acc = (StackPane) lineOfAcc.get(pos - 1);
                
                // Don't register events for stability
                acc.setDisable(true);
                accs.add(acc);
            }

            accMatrix.add(accs);
        }
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
            StackPane notes = getNotes(index, s.getPosition());
            StackPane accidentals = getAccidentals(index, s.getPosition());
            
            StaffAccidental accidental = new StaffAccidental(s);
            accidental.setImage(il.getSpriteFX(Staff.switchAcc(s
                    .getAccidental())));

            if (s.muteNoteVal() == 0) {
                s.setImage(il.getSpriteFX(s.getInstrument().imageIndex()));
            } else if (s.muteNoteVal() == 1) {
                s.setImage(il.getSpriteFX(s.getInstrument().imageIndex().alt()));
            } else {
                s.setImage(il.getSpriteFX(s.getInstrument().imageIndex()
                        .silhouette()));
            }

            s.setVisible(true);
            
            notes.getChildren().add(s.toImageView());
            accidentals.getChildren().add(accidental.toImageView());
        }
    }

}
