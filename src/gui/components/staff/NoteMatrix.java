package gui.components.staff;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import backend.songs.Accidental;
import backend.songs.MuteModifier;
import backend.songs.Note;
import backend.songs.NoteLine;
import backend.songs.Song;
import gui.components.staff.StaffDisplayManager.StaffNoteCoordinate;
import gui.loaders.ImageIndex;
import javafx.scene.image.Image;
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
class NoteMatrix {
    
    private final StaffDisplayManager disp;

    private final List<ImageView> matrix;
    private final List<ImageView> accMatrix;
    
    private final List<ImageView> silMatrix;
    private final List<ImageView> accSilMatrix;
    
    /** Pointer to the image loader object. */
    private final transient Map<ImageIndex, Image> imagesHolder;
    
    /**
     * A silhouette note to display where the cursor is.
     * Set to {@code null} if no silhouette is displayed.
     */
    private Note currentSilhouette;
    private int currentSilhouetteColumn;

    public NoteMatrix(Map<ImageIndex, Image> imagesHolder, StaffDisplayManager disp) {
        this.imagesHolder = imagesHolder;
        this.disp = disp;
        
        int notesCapacity = disp.width * disp.height * disp.depth;
        int silCapacity = disp.width * disp.height;
        
        matrix = new Vector<>(notesCapacity);
        accMatrix = new Vector<>(notesCapacity);
        silMatrix = new Vector<>(silCapacity);
        accSilMatrix = new Vector<>(silCapacity);
        
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
    public void populateNoteDisplay(Song seq, int currentPosition) {
        // No order is assumed in StaffNoteLine so we keep track of how many
        // stacked notes we already treated for each row
        int[] stackedAmounts = new int[disp.height];
        int[] accStackedAmounts = new int[disp.height];
        
        for (int col = 0; col < disp.width; col++) {
            for (int row = 0; row < disp.height; row++) {
                stackedAmounts[row] = 0;
                accStackedAmounts[row] = 0;
            }
            
            NoteLine stl = seq.getLine(currentPosition + col);
            List<Note> st = stl.getNotes();
            
            for (Note s : st) {
                int row = s.getVerticalPosition();
                
                int d = stackedAmounts[row];
                if (d < disp.depth) {
                    stackedAmounts[row] = d + 1;
                    ImageView iv = matrix.get(disp.new StaffNoteCoordinate(col, row, d).lin());
                    iv.setImage(imagesHolder.get(noteImageIndex(s)));
                    iv.setEffect(s.isSelected() ? StaffDisplayManager.highlightBlend : null);
                    iv.setVisible(true);
                }
                
                d = accStackedAmounts[row];
                if (d < disp.depth) {
                    accStackedAmounts[row] = d + 1;
                    ImageView iv = accMatrix.get(disp.new StaffNoteCoordinate(col, row, d).lin());
                    iv.setImage(imagesHolder.get(accImageIndex(s)));
                    iv.setVisible(true);
                }
            }
        }
    }
    
    public void resetSilhouette() {
        if (currentSilhouette == null)
            return;
        
        int col = currentSilhouetteColumn;
        int row = currentSilhouette.getVerticalPosition();
        
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
    public void updateSilhouette(int col, Note note) {
        if (note == null)
            return;
        
        resetSilhouette();
        
        int row = note.getVerticalPosition();
        Note silhouette = new Note(
                note.getInstrument(),
                note.getVerticalPosition(),
                note.getAccidental(),
                MuteModifier.MUTE_THIS_INST);
        
        currentSilhouetteColumn = col;
        currentSilhouette = silhouette;
        
        ImageView iv = silMatrix.get(disp.new StaffNoteCoordinate(col, row, -1).lin());
        iv.setImage(imagesHolder.get(noteImageIndex(silhouette)));
        iv.setVisible(true);
        
        if (silhouette.getAccidental() != Accidental.NATURAL) {
            ImageView acciv = accSilMatrix.get(disp.new StaffNoteCoordinate(col, row, -1).lin());
            acciv.setImage(imagesHolder.get(accImageIndex(silhouette)));
            acciv.setVisible(true);
        }
    }
    
    /**
     * Refresh the current silhouette with a possibly different accidental
     */
    public void refreshSilhouette(Accidental acc) {
        if (currentSilhouette != null && currentSilhouette.getAccidental() != acc) {
            Note sil = new Note(currentSilhouette.getInstrument(), currentSilhouette.getVerticalPosition(), acc);
            updateSilhouette(currentSilhouetteColumn, sil);
        }
    }
    
    public ImageIndex noteImageIndex(Note note) {
        switch (note.getMuteModifier()) {
        case REGULAR:
            return note.getInstrument().imageIndex();
        case MUTE_THIS_PITCH:
            return note.getInstrument().imageIndex().alt();
        case MUTE_THIS_INST:
            return note.getInstrument().imageIndex().silhouette();
        default:
            throw new IllegalArgumentException("Unrecognized mute modifier: " + note.getMuteModifier());   
        }
    }
    
    public ImageIndex accImageIndex(Note theNote) {
        switch (theNote.getMuteModifier()) {
        case REGULAR:
            return theNote.getAccidental().imageIndex();
        case MUTE_THIS_PITCH:
            return theNote.getAccidental().imageIndex().alt();
        case MUTE_THIS_INST:
            return theNote.getAccidental().imageIndex().silhouette();
        default:
            throw new IllegalArgumentException("Unrecognized mute modifier: " + theNote.getMuteModifier());
        }
    }

}
