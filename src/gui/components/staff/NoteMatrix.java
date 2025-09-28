package gui.components.staff;

import java.util.ArrayList;

import backend.songs.Accidental;
import backend.songs.StaffAccidental;
import backend.songs.StaffNote;
import backend.songs.StaffNoteLine;
import backend.songs.StaffSequence;
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
    
    final private int width;
    final private int height;
    final private int depth;

    private ArrayList<ImageView> matrix;
    private ArrayList<ImageView> accMatrix;
    
    private int map(int col, int row, int d) {
        return ((depth + 1) * ((height * col) + row)) + d;
    }
    
    /** Pointer to the image loader object. */
    private transient ImageLoader il;
    
    /**
     * A silhouette note to display where the cursor is.
     * Set to {@code null} if no silhouette is displayed.
     */
    private StaffNote currentSilhouette;
    private int currentSilhouetteColumn;
    
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

    public NoteMatrix(ImageLoader i, int width, int height, int depth) {
        il = i;
        this.width = width;
        this.height = height;
        this.depth = depth;
        matrix = new ArrayList<ImageView>();
        accMatrix = new ArrayList<ImageView>();
        currentSilhouette = null;
        cacheSilhouette = null;
    }
    
    public void initializeNoteDisplay(HBox staffInstruments, HBox staffAccidentals) {
        initializeNotesMatrix(staffInstruments);
        initializeAccidentalsMatrix(staffAccidentals);
    }
    
    private void initializeNotesMatrix(HBox staffInstruments) {
        matrix.clear();
        
        for (int col = 0; col < width; col++) {
            VBox vBox = new VBox();
            vBox.setSpacing(-20.0);
            vBox.setAlignment(Pos.CENTER);
                        
            for (int row = 0; row < height; row++) {
                StackPane stack = new StackPane();
                stack.setDisable(true);
                stack.setPrefHeight(36.0);
                stack.setPrefWidth(64.0);
                                
                // +1 for silhouette
                for (int d = 0; d < depth + 1; d++) {
                    ImageView iv = new ImageView();
                    iv.setVisible(false);
                    iv.setFitWidth(32);
                    iv.setFitHeight(36);
                    
                    stack.getChildren().add(iv);
                    matrix.add(iv);
                }
                
                vBox.getChildren().add(0, stack);
            }
            
            staffInstruments.getChildren().add(vBox);
        }  
    }
    
    private void initializeAccidentalsMatrix(HBox staffAccidentals) {
        accMatrix.clear();
        
        for (int col = 0; col < width; col++) {
            VBox vBox = new VBox();
            vBox.setSpacing(-20.0);
            vBox.setAlignment(Pos.CENTER);
            vBox.setPrefHeight(472.0);
            vBox.setPrefWidth(32.0);
                        
            for (int row = 0; row < height; row++) {
                StackPane stack = new StackPane();
                stack.setDisable(true);
                stack.setPrefHeight(36.0);
                stack.setPrefWidth(32.0);
                                
                // +1 for silhouette
                for (int d = 0; d < depth + 1; d++) {
                    ImageView iv = new ImageView();
                    iv.setVisible(false);
                    iv.setFitWidth(32);
                    iv.setFitHeight(32);
                    
                    stack.getChildren().add(iv);
                    accMatrix.add(iv);
                }
                
                vBox.getChildren().add(0, stack);
            }
            
            staffAccidentals.getChildren().add(vBox);
        }     
    }

    /**
     * Clears the note display on the staff, including the silhouette.
     */
    public synchronized void clearNoteDisplay() {
        for (ImageView iv : matrix)
            iv.setVisible(false);
        
        for (ImageView iv : accMatrix)
            iv.setVisible(false);
        
        cacheSilhouette = currentSilhouette;
        currentSilhouette = null;
    }

    /**
     * Repopulates the note display on the staff.
     */
    public void populateNoteDisplay(StaffSequence seq, int currentPosition) {
        for (int col = 0; col < width; col++) {
            StaffNoteLine stl = seq.getLineSafe(currentPosition + col);
            ArrayList<StaffNote> st = stl.getNotes();
            
            for (StaffNote s : st) {
                int row = s.getPosition();                
                StaffAccidental accidental = new StaffAccidental(s);
                
                int d = 0;
                while (matrix.get(map(col, row, d)).isVisible() && d < depth)
                    d++;
                if (d < depth) {
                    ImageView iv = matrix.get(map(col, row, d));
                    iv.setImage(s.getImage(il));
                    iv.setEffect(s.isSelected() ? highlightBlend : null);
                    iv.setVisible(true);
                }
                
                d = 0;
                while (accMatrix.get(map(col, row, d)).isVisible() && d < depth)
                    d++;
                if (d < depth) {
                    ImageView iv = accMatrix.get(map(col, row, d));
                    iv.setImage(accidental.getImage(il));
                    iv.setVisible(true);
                }
            }            
        }
    }
    
    public void resetSilhouette() {
        if (currentSilhouette == null)
            return;
        
        int col = currentSilhouetteColumn;
        int row = currentSilhouette.getPosition();
        
        matrix.get(map(col, row, depth)).setVisible(false);
        
        if (currentSilhouette.getAccidental() != Accidental.NATURAL) {
            accMatrix.get(map(col, row, depth)).setVisible(false);
        }
        
        currentSilhouette = null;
    }
    
    /**
     * The silhouette is processing separately from regular notes because it
     * needs to be refreshed more often.
     */
    public void updateSilhouette(int col, StaffNote note) {
        if (note == null)
            return;
        
        resetSilhouette();
        
        int row = note.getPosition();
        StaffNote silhouette = new StaffNote(note);
        silhouette.setMuteNote(2);
        
        currentSilhouetteColumn = col;
        currentSilhouette = silhouette;
        
        ImageView iv = matrix.get(map(col, row, depth));
        iv.setImage(silhouette.getImage(il));
        iv.setVisible(true);
        
        if (silhouette.getAccidental() != Accidental.NATURAL) {
            ImageView acciv = accMatrix.get(map(col, row, depth));
            StaffAccidental acc = new StaffAccidental(silhouette);
            acciv.setImage(acc.getImage(il));
            acciv.setVisible(true);
        }
    }
    
    /**
     * Restore silhouette after the note display has been cleared.
     */
    public void refreshSilhouette() {
        if (currentSilhouette == null && cacheSilhouette != null)
            updateSilhouette(currentSilhouetteColumn, cacheSilhouette);
    }
    
    public void refreshSilhouette(Accidental acc) {
        if (currentSilhouette != null && currentSilhouette.getAccidental() != acc) {
            StaffNote sil = new StaffNote(currentSilhouette.getInstrument(), currentSilhouette.getPosition(), acc);
            updateSilhouette(currentSilhouetteColumn, sil);
            
        } else if (currentSilhouette == null && cacheSilhouette != null) {
            StaffNote sil = new StaffNote(cacheSilhouette.getInstrument(), cacheSilhouette.getPosition(), acc);
            updateSilhouette(currentSilhouetteColumn, sil);
        }
    }

}
