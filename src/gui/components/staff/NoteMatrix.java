package gui.components.staff;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import backend.songs.Accidental;
import backend.songs.StaffAccidental;
import backend.songs.StaffNote;
import backend.songs.StaffNoteLine;
import backend.songs.StaffSequence;
import gui.clipboard.StaffClipboard;
import gui.components.staff.StaffDisplayManager.StaffNoteCoordinate;
import gui.loaders.ImageLoader;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;

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
    
    final private StaffDisplayManager disp;

    final private List<ImageView> matrix;
    final private List<ImageView> accMatrix;
    
    final private List<ImageView> silMatrix;
    final private List<ImageView> accSilMatrix;
    
    /** Pointer to the image loader object. */
    final private transient ImageLoader il;
    
    /**
     * A silhouette note to display where the cursor is.
     * Set to {@code null} if no silhouette is displayed.
     */
    private StaffNote currentSilhouette;
    private int currentSilhouetteColumn;
    
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

    public NoteMatrix(ImageLoader il, StaffDisplayManager disp) {
        this.il = il;
        this.disp = disp;
        
        int notesCapacity = disp.width * disp.height * disp.depth;
        int silCapacity = disp.width * disp.height;
        
        matrix = new Vector<ImageView>(notesCapacity);
        accMatrix = new Vector<ImageView>(notesCapacity);
        silMatrix = new Vector<ImageView>(silCapacity);
        accSilMatrix = new Vector<ImageView>(silCapacity);
        
        ((Vector<?>) matrix).setSize(notesCapacity);
        ((Vector<?>) accMatrix).setSize(notesCapacity);
        ((Vector<?>) silMatrix).setSize(silCapacity);
        ((Vector<?>) accSilMatrix).setSize(silCapacity);
        
        currentSilhouette = null;
    }
    
    public void initializeNoteDisplay(Map<StaffNoteCoordinate, ImageView> map, Map<StaffNoteCoordinate, ImageView> mapAcc) {        
        for (Map.Entry<StaffNoteCoordinate, ImageView> entry : map.entrySet()) {
        	StaffNoteCoordinate k = entry.getKey();
        	ImageView iv = entry.getValue();
        	
        	if (k.dep == -1) {
        		silMatrix.set(k.lin(), iv);
        	} else {
        		matrix.set(k.lin(), iv);
        	}
        }
        
        for (Map.Entry<StaffNoteCoordinate, ImageView> entry : mapAcc.entrySet()) {
        	StaffNoteCoordinate k = entry.getKey();
        	ImageView iv = entry.getValue();
        	
        	if (k.dep == -1) {
        		accSilMatrix.set(k.lin(), iv);
        	} else {
        		accMatrix.set(k.lin(), iv);
        	}
        }
    }

    /**
     * Clears the note display on the staff, excluding silhouettes.
     */
    public synchronized void clearNoteDisplay() {
        for (ImageView iv : matrix)
            iv.setVisible(false);
        
        for (ImageView iv : accMatrix)
            iv.setVisible(false);
    }

    /**
     * Repopulates the note display on the staff.
     */
    public void populateNoteDisplay(StaffSequence seq, int currentPosition) {
        // No order is assumed in StaffNoteLine so we keep track of how many
        // stacked notes we already treated for each row
        int[] stackedAmounts = new int[disp.height];
        int[] accStackedAmounts = new int[disp.height];
        
        for (int col = 0; col < disp.width; col++) {
            for (int row = 0; row < disp.height; row++) {
                stackedAmounts[row] = 0;
                accStackedAmounts[row] = 0;
            }
            
            StaffNoteLine stl = seq.getLineSafe(currentPosition + col);
            List<StaffNote> st = stl.getNotes();
            
            for (StaffNote s : st) {
                int row = s.getPosition();                
                StaffAccidental accidental = new StaffAccidental(s);
                
                int d = stackedAmounts[row];
                if (d < disp.depth) {
                    stackedAmounts[row] = d + 1;
                    ImageView iv = matrix.get(disp.new StaffNoteCoordinate(col, row, d).lin());
                    iv.setImage(il.getSpriteFX(s.getImageIndex()));
                    iv.setEffect(s.isSelected() ? highlightBlend : null);
                    iv.setVisible(true);
                }
                
                d = accStackedAmounts[row];
                if (d < disp.depth) {
                    accStackedAmounts[row] = d + 1;
                    ImageView iv = accMatrix.get(disp.new StaffNoteCoordinate(col, row, d).lin());
                    iv.setImage(il.getSpriteFX(accidental.getImageIndex()));
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
        
        silMatrix.get(disp.new StaffNoteCoordinate(col, row, -1).lin()).setVisible(false);
        
        if (currentSilhouette.getAccidental() != Accidental.NATURAL) {
            accSilMatrix.get(disp.new StaffNoteCoordinate(col, row, -1).lin()).setVisible(false);
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
        
        ImageView iv = silMatrix.get(disp.new StaffNoteCoordinate(col, row, -1).lin());
        iv.setImage(il.getSpriteFX(silhouette.getImageIndex()));
        iv.setVisible(true);
        
        if (silhouette.getAccidental() != Accidental.NATURAL) {
            ImageView acciv = accSilMatrix.get(disp.new StaffNoteCoordinate(col, row, -1).lin());
            StaffAccidental acc = new StaffAccidental(silhouette);
            acciv.setImage(il.getSpriteFX(acc.getImageIndex()));
            acciv.setVisible(true);
        }
    }
    
    /**
     * Refresh the current silhouette with a possibly different accidental
     */
    public void refreshSilhouette(Accidental acc) {
        if (currentSilhouette != null && currentSilhouette.getAccidental() != acc) {
            StaffNote sil = new StaffNote(currentSilhouette.getInstrument(), currentSilhouette.getPosition(), acc);
            updateSilhouette(currentSilhouetteColumn, sil);
        }
    }

}
