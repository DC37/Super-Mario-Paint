package smp.components.staff;

import java.util.ArrayList;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.Values;
import smp.fx.SMPFXController;
import smp.stateMachine.StateMachine;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Wrapper class for all of the images that appear on the Staff of
 * Super Mario Paint. Contains the sprite holders for the images that
 * represent notes, the ImageView holders for each of the measure lines,
 * the lines that allow one to display notes higher than a G or lower than
 * a D on the staff, and the measure numbers.
 * @author RehdBlob
 * @since 2012.09.17
 */
public class StaffImages {

    /**
     * The ArrayList that holds the ImageView objects that the measure lines
     * object holds.
     */
    private ArrayList<ImageView> measureLines;


    /** These are the bars that highlight notes. */
    private ArrayList<ImageView> staffPlayBars;


    /**
     * The line that denotes where the staffImages should begin searching
     * for images to draw.
     */
    private int beginLine;

    /**
     * The line after which StaffImages should stop looking for images to draw.
     */
    private int endLine;

    /**
     * The digits, 0-9, to be used for numbering the staff measures.
     */
    private ArrayList<Image> digits;

    /**
     * The parent staff object.
     */
    private Staff theStaff;

    /**
     * Constructor that also sets up the staff expansion lines.
     * @param staffExtLines These
     */
    public StaffImages(HBox[] staffExtLines) {
        initializeStaffExpansionLines(staffExtLines);
    }

    /**
     * Instantiates this wrapper class with the correct HBox objects
     * such that it can begin keeping track of whatever's happening
     * on the staff, at least on the measure lines side.
     */
    public void initialize() {

        initializeStaffMeasureLines(SMPFXController.getStaffMeasureLines());
        initalizeStaffPlayBars(SMPFXController.getStaffPlayBars());
        initializeStaffMeasureNums(SMPFXController.getStaffMeasureNums());
        initializeStaffInstruments(SMPFXController.getStaffInstruments(),
                SMPFXController.getStaffAccidentals());
        initializeVolumeBars(SMPFXController.getVolumeBars());

    }

    /**
     * Initializes the volume bars in the program.
     * @param volumeBars This is the HBox that holds all of the volume
     * bar <code>StackPane</code> objects.
     */
    private void initializeVolumeBars(HBox volumeBars) {
        ArrayList<StackPane> vol = new ArrayList<StackPane>();
        for (Node v : volumeBars.getChildren())
            vol.add((StackPane) v);
        theStaff.getNoteMatrix().setVolumeBars(vol);
        for (StackPane s : vol)
            setupVolumeBehavior(s);

    }

    /**
     * Makes the <code>StackPane</code> actually behave like a volume bar.
     * @param s A <code>StackPane</code> object that holds an
     * <code>ImageView</code> object that will display a volume bar.
     */
    private void setupVolumeBehavior(StackPane s) {

    }

    /**
     * These are the numbers above each successive measure.
     */
    private void initializeStaffMeasureNums(HBox mNums) {
        ArrayList<HBox> measureNumBoxes = new ArrayList<HBox>();
        for(Node num : mNums.getChildren())
            measureNumBoxes.add((HBox) num);
    }

    /**
     * Sets up the various note lines of the staff. These
     * are the notes that can appear on the staff. This method
     * also sets up sharps, flats, etc.
     * @param accidentals The HBox that holds the framework for the sharps /
     * flats.
     * @param instruments The HBox that holds the framework for the instruments.
     */
    private void initializeStaffInstruments(HBox instruments, HBox accidentals) {
        NoteMatrix staffMatrix = theStaff.getNoteMatrix();

        ArrayList<VBox> accidentalLines = new ArrayList<VBox>();
        for (Node n : accidentals.getChildren())
            accidentalLines.add((VBox) n);

        ArrayList<VBox> noteLines = new ArrayList<VBox>();
        for (Node n : instruments.getChildren())
            noteLines.add((VBox) n);

        for (int line = 0; line < noteLines.size(); line++) {
            VBox verticalHolder = noteLines.get(line);
            VBox accVerticalHolder = accidentalLines.get(line);

            ObservableList<Node> lineOfNotes = verticalHolder.getChildren();
            ObservableList<Node> lineOfAcc = accVerticalHolder.getChildren();

            ArrayList<StackPane> notes = new ArrayList<StackPane>();
            ArrayList<StackPane> accs = new ArrayList<StackPane>();
            ArrayList<NotePane> notePanes = new ArrayList<NotePane>();

            for (int pos = 1; pos <= Values.NOTES_IN_A_LINE; pos++) {
                StackPane note = (StackPane) lineOfNotes.get(pos - 1);
                StackPane acc = (StackPane) lineOfAcc.get(pos - 1);
                notes.add(note);
                accs.add(acc);
                StaffInstrumentEventHandler hd =
                        new StaffInstrumentEventHandler(note, acc,
                                Values.NOTES_IN_A_LINE - pos, line, theStaff);
                note.addEventHandler(MouseEvent.ANY, hd);
                notePanes.add(new NotePane(note, hd));
            }

            staffMatrix.addNotePane(notePanes);
            staffMatrix.addLine(notes);
            staffMatrix.addAccLine(accs);
        }
    }

    /**
     * These are the lines that divide up the staff.
     * @param staffMLines The measure lines that divide the staff.
     */
    private void initializeStaffMeasureLines(HBox mLines) {
        measureLines = new ArrayList<ImageView>();
        for (Node n : mLines.getChildren())
            measureLines.add((ImageView) n);
        for (int i = 0; i < measureLines.size(); i++) {
            if (i % Values.DEFAULT_TIMESIG_BEATS == 0)
                measureLines.get(i).setImage(ImageLoader.getSpriteFX(
                        ImageIndex.STAFF_MLINE));
            else
                measureLines.get(i).setImage(ImageLoader.getSpriteFX(
                        ImageIndex.STAFF_LINE));
        }
    }

    /**
     * Redraws the staff measure lines.
     * @param currLine The current line that we are on.
     */
    public void updateStaffMeasureLines(int currLine) {
        for (int i = 0; i < measureLines.size(); i++) {
            ImageView currImage = measureLines.get(i);
            if ((currLine + i) % Values.DEFAULT_TIMESIG_BEATS == 0)
                currImage.setImage(ImageLoader.getSpriteFX(
                        ImageIndex.STAFF_MLINE));
            else
                currImage.setImage(ImageLoader.getSpriteFX(
                        ImageIndex.STAFF_LINE));
        }
    }

    /**
     * Sets up the note highlighting functionality.
     * @param staffPlayBars The bars that move to highlight different
     * notes.
     */
    private void initalizeStaffPlayBars(HBox playBars) {
        staffPlayBars = new ArrayList<ImageView>();
        for (Node n : playBars.getChildren())
            staffPlayBars.add((ImageView) n);
    }

    /**
     * Sets up the staff expansion lines, which are to hold notes that are
     * higher than or lower than the regular lines of the staff.
     * @param staffExpLines An array of expansion lines. This method
     * expects that there will be two of these, one of which indicates
     * the lines above the staff and the other of which indicates
     * the lines below the staff.
     */
    private void initializeStaffExpansionLines(HBox[] staffExpLines) {
        // TODO Auto-generated method stub

    }

    /**
     * Draws the pictures on the staff.
     */
    void draw() {

    }

    /**
     * Refreshes the staff images that are currently displaying by
     * forcing everything to redraw itself.
     */
    public void redraw() {

    }

    /**
     * Sets the parent staff object to the specified object.
     * @param s The pointer to the parent staff object.
     */
    public void setStaff(Staff s) {
        theStaff = s;
    }

    /**
     * @return The list of <code>ImageView</code> objects that
     * holds the bars that will highlight the notes that we are playing
     * on the staff.
     */
    public ArrayList<ImageView> getPlayBars() {
        return staffPlayBars;
    }


}
