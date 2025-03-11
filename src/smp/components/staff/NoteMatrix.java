package smp.components.staff;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import smp.ImageLoader;
import smp.components.Values;
import smp.components.staff.sequences.Accidental;
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
    
    /**
     * A silhouette note to display where the cursor is.
     * Set to {@code null} if no silhouette is displayed.
     */
    private StaffNote currentSilhouette;
    private int currentSilhouetteLine;
    
    /**
     * Used to refresh the silhouette after the entire matrix has been cleared.
     */
    private StaffNote cacheSilhouette;

    public NoteMatrix(ImageLoader i) {
        il = i;
        matrix = new ArrayList<ArrayList<StackPane>>();
        accMatrix = new ArrayList<ArrayList<StackPane>>();
        currentSilhouette = null;
        cacheSilhouette = null;
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
    
    /**
     * Initialize some matrix in the standard way
     * @param vBoxes a HBox of VBoxes of StackPanes
     * @param matrix the matrix to fill
     */
    private void initializeMatrix(HBox vBoxes, ArrayList<ArrayList<StackPane>> matrix) {
        matrix.clear();
        
        for (Node nl : vBoxes.getChildren()) {
            ArrayList<StackPane> line = new ArrayList<>();
            
            for (Node n : ((VBox) nl).getChildren()) {
                StackPane st = (StackPane) n;
                
                st.setDisable(true);
                line.add(st);
            }
            
            matrix.add(line);
        }
    }
    
    private void initializeNotesMatrix(HBox staffInstruments) {
        initializeMatrix(staffInstruments, this.matrix);
    }
    
    private void initializeAccidentalsMatrix(HBox staffAccidentals) {
        initializeMatrix(staffAccidentals, this.accMatrix);
    }

    /**
     * Clears the note display on the staff, including the silhouette.
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
        
        cacheSilhouette = currentSilhouette;
        currentSilhouette = null;
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
            
            notes.getChildren().add(s.toImageView(il));
            accidentals.getChildren().add(accidental.toImageView(il));
        }        
    }
    
    public void resetSilhouette() {
        if (currentSilhouette == null)
            return;
        
        int line = currentSilhouetteLine;
        int pos = currentSilhouette.getPosition();
        
        StackPane notes = getNotes(line, pos);
        notes.getChildren().remove(notes.getChildren().size() - 1);
        
        if (currentSilhouette.getAccidental() != Accidental.NATURAL) {
            StackPane accs = getAccidentals(line, pos);
            accs.getChildren().remove(accs.getChildren().size() - 1);
        }
        
        currentSilhouette = null;
    }
    
    /**
     * The silhouette is processing separately from regular notes because it
     * needs to be refreshed more often.
     */
    public void updateSilhouette(int line, StaffNote note) {
        if (note == null)
            return;
        
        resetSilhouette();
        
        int pos = note.getPosition();
        StaffNote silhouette = new StaffNote(note);
        silhouette.setMuteNote(2);
        
        currentSilhouetteLine = line;
        currentSilhouette = silhouette;
        
        StackPane notes = getNotes(line, pos);
        notes.getChildren().add(notes.getChildren().size(), silhouette.toImageView(il));
        
        if (silhouette.getAccidental() != Accidental.NATURAL) {
            StackPane accs = getAccidentals(line, pos);
            StaffAccidental acc = new StaffAccidental(silhouette);
            accs.getChildren().add(accs.getChildren().size(), acc.toImageView(il));
        }
    }
    
    /**
     * Restore silhouette after the note display has been cleared.
     */
    public void refreshSilhouette() {
        if (currentSilhouette == null && cacheSilhouette != null)
            updateSilhouette(currentSilhouetteLine, cacheSilhouette);
    }
    
    public void refreshSilhouette(Accidental acc) {
        if (currentSilhouette != null && currentSilhouette.getAccidental() != acc) {
            StaffNote sil = new StaffNote(currentSilhouette.getInstrument(), currentSilhouette.getPosition(), acc);
            updateSilhouette(currentSilhouetteLine, sil);
            
        } else if (currentSilhouette == null && cacheSilhouette != null) {
            StaffNote sil = new StaffNote(cacheSilhouette.getInstrument(), cacheSilhouette.getPosition(), acc);
            updateSilhouette(currentSilhouetteLine, sil);
        }
    }

}
