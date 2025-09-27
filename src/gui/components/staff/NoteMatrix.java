package gui.components.staff;

import java.util.ArrayList;

import backend.songs.Accidental;
import backend.songs.StaffAccidental;
import backend.songs.StaffNote;
import backend.songs.StaffNoteLine;
import backend.songs.StaffSequence;
import gui.Values;
import gui.clipboard.StaffClipboard;
import gui.loaders.ImageLoader;
import javafx.geometry.Pos;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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

    private ArrayList<ArrayList<ArrayList<ImageView>>> matrix;
    private ArrayList<ArrayList<ArrayList<ImageView>>> accMatrix;

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
    
    private static Blend highlightBlend = new Blend(
            BlendMode.SRC_OVER,
            null,
            new ColorInput(
                    0,
                    0,
                    32,
                    36,
                    StaffClipboard.HIGHLIGHT_FILL
                    )
            );

    public NoteMatrix(ImageLoader i) {
        il = i;
        matrix = new ArrayList<ArrayList<ArrayList<ImageView>>>();
        accMatrix = new ArrayList<ArrayList<ArrayList<ImageView>>>();
        currentSilhouette = null;
        cacheSilhouette = null;
    }
    
    private ArrayList<ImageView> getNotes(int x, int y) {
        return matrix.get(x).get(Values.NOTES_IN_A_LINE - y - 1);
    }
    
    private ArrayList<ImageView> getAccidentals(int x, int y) {
        return accMatrix.get(x).get(Values.NOTES_IN_A_LINE - y - 1);
    }
    
    public void initializeNoteDisplay(HBox staffInstruments, HBox staffAccidentals) {
        initializeNotesMatrix(staffInstruments);
        initializeAccidentalsMatrix(staffAccidentals);
    }
    
    private void initializeNotesMatrix(HBox staffInstruments) {
        matrix.clear();
        
        for (int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++) {
            VBox vBox = new VBox();
            vBox.setSpacing(-20.0);
            vBox.setAlignment(Pos.CENTER);
            
            ArrayList<ArrayList<ImageView>> column = new ArrayList<>();
            
            for (int j = 0; j < Values.NOTES_IN_A_LINE; j++) {
                StackPane stack = new StackPane();
                stack.setDisable(true);
                stack.setPrefHeight(36.0);
                stack.setPrefWidth(64.0);
                
                ArrayList<ImageView> notes = new ArrayList<>();
                
                // +1 for silhouette
                for (int k = 0; k < Values.MAX_STACKABLE_NOTES + 1; k++) {
                    ImageView iv = new ImageView();
                    iv.setVisible(false);
                    iv.setFitWidth(32);
                    iv.setFitHeight(36);
                    
                    stack.getChildren().add(iv);
                    notes.add(iv);
                }
                
                vBox.getChildren().add(stack);
                column.add(notes);
            }
            
            staffInstruments.getChildren().add(vBox);
            matrix.add(column);
        }  
    }
    
    private void initializeAccidentalsMatrix(HBox staffAccidentals) {
        accMatrix.clear();
        
        for (int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++) {
            VBox vBox = new VBox();
            vBox.setSpacing(-20.0);
            vBox.setAlignment(Pos.CENTER);
            vBox.setPrefHeight(472.0);
            vBox.setPrefWidth(32.0);
            
            ArrayList<ArrayList<ImageView>> column = new ArrayList<>();
            
            for (int j = 0; j < Values.NOTES_IN_A_LINE; j++) {
                StackPane stack = new StackPane();
                stack.setDisable(true);
                stack.setPrefHeight(36.0);
                stack.setPrefWidth(32.0);
                
                ArrayList<ImageView> accs = new ArrayList<>();
                
                // +1 for silhouette
                for (int k = 0; k < Values.MAX_STACKABLE_NOTES + 1; k++) {
                    ImageView iv = new ImageView();
                    iv.setVisible(false);
                    iv.setFitWidth(32);
                    iv.setFitHeight(32);
                    
                    stack.getChildren().add(iv);
                    accs.add(iv);
                }
                
                vBox.getChildren().add(stack);
                column.add(accs);
            }
            
            staffAccidentals.getChildren().add(vBox);
            accMatrix.add(column);
        }     
    }

    /**
     * Clears the note display on the staff, including the silhouette.
     */
    public synchronized void clearNoteDisplay() {
        for (int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++) {
            for (int j = 0; j < Values.NOTES_IN_A_LINE; j++) {
                for (int k = 0; k < Values.MAX_STACKABLE_NOTES + 1; k++) {
                    matrix.get(i).get(j).get(k).setVisible(false);
                    accMatrix.get(i).get(j).get(k).setVisible(false);
                }
            }
        }
        
        cacheSilhouette = currentSilhouette;
        currentSilhouette = null;
    }

    /**
     * Repopulates the note display on the staff.
     */
    public void populateNoteDisplay(StaffSequence seq, int currentPosition) {
        for (int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++) {
            StaffNoteLine stl = seq.getLineSafe(currentPosition + i);
            ArrayList<StaffNote> st = stl.getNotes();
            
            for (StaffNote s : st) {
                int j = s.getPosition();
                ArrayList<ImageView> notes = getNotes(i, j);
                ArrayList<ImageView> accidentals = getAccidentals(i, j);
                
                StaffAccidental accidental = new StaffAccidental(s);
                
                int k = 0;
                while (notes.get(k).isVisible() && k < Values.MAX_STACKABLE_NOTES)
                    k++;
                if (k < Values.MAX_STACKABLE_NOTES) {
                    notes.get(k).setImage(s.getImage(il));
                    notes.get(k).setEffect(s.isSelected() ? highlightBlend : null);
                    notes.get(k).setVisible(true);
                }
                
                k = 0;
                while (accidentals.get(k).isVisible() && k < Values.MAX_STACKABLE_NOTES)
                    k++;
                if (k < Values.MAX_STACKABLE_NOTES) {
                    accidentals.get(k).setImage(accidental.getImage(il));
                    accidentals.get(k).setVisible(true);
                }
            }            
        }
    }
    
    public void resetSilhouette() {
        if (currentSilhouette == null)
            return;
        
        int line = currentSilhouetteLine;
        int pos = currentSilhouette.getPosition();
        
        ArrayList<ImageView> notes = getNotes(line, pos);
        notes.get(Values.MAX_STACKABLE_NOTES).setVisible(false);
        
        if (currentSilhouette.getAccidental() != Accidental.NATURAL) {
            ArrayList<ImageView> accs = getAccidentals(line, pos);
            accs.get(Values.MAX_STACKABLE_NOTES).setVisible(false);
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
        
        ArrayList<ImageView> notes = getNotes(line, pos);
        notes.get(Values.MAX_STACKABLE_NOTES).setImage(silhouette.getImage(il));
        notes.get(Values.MAX_STACKABLE_NOTES).setVisible(true);
        
        if (silhouette.getAccidental() != Accidental.NATURAL) {
            ArrayList<ImageView> accs = getAccidentals(line, pos);
            StaffAccidental acc = new StaffAccidental(silhouette);
            accs.get(Values.MAX_STACKABLE_NOTES).setImage(acc.getImage(il));
            accs.get(Values.MAX_STACKABLE_NOTES).setVisible(true);
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
