package smp.components.staff;

import java.util.ArrayList;

import smp.components.Constants;
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

    }

    /**
     * These are the numbers above each successive measure.
     */
    private void initializeStaffMeasureNums(HBox mNums) {
        ArrayList<HBox> measureNumBoxes = new ArrayList<HBox>();
        for(Node num : mNums.getChildren())
            measureNumBoxes.add((HBox) num);
        theStaff.getStaffBackend().setMeasureNumBoxes(measureNumBoxes);
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

        for (int i = 0; i < noteLines.size(); i++) {
            VBox verticalHolder = noteLines.get(i);
            VBox accVerticalHolder = accidentalLines.get(i);

            ObservableList<Node> lineOfNotes = verticalHolder.getChildren();
            ObservableList<Node> lineOfAcc = accVerticalHolder.getChildren();

            ArrayList<StackPane> notes = new ArrayList<StackPane>();
            ArrayList<StackPane> accs = new ArrayList<StackPane>();

            for (int pos = 1; pos <= Constants.NOTES_IN_A_LINE; pos++) {
                StackPane note = (StackPane) lineOfNotes.get(pos - 1);
                StackPane acc = (StackPane) lineOfAcc.get(pos - 1);
                notes.add(note);
                accs.add(acc);
                addListeners(note, acc, Constants.NOTES_IN_A_LINE - pos);
            }

            staffMatrix.addLine(notes);
            staffMatrix.addAccLine(accs);
        }
    }

    /**
     * Adds the requisite listeners to the StackPanes of the instrument
     * note matrix.
     * @param s The StackPane that will be holding some instrument.
     * @param acc The StackPane that will be holding some sort of accidental.
     * @param pos The position that this StackPane will be associated with.
     */
    private void addListeners(final StackPane s, final StackPane acc,
            final int pos) {
        s.addEventHandler(MouseEvent.ANY,
                new StaffInstrumentEventHandler(s, acc, pos));
    }

    /**
     * These are the lines that divide up the staff.
     * @param staffMLines The measure lines that divide the staff.
     */
    private void initializeStaffMeasureLines(HBox mLines) {
        ArrayList<ImageView> measureLines = new ArrayList<ImageView>();
        for (Node n : mLines.getChildren())
            measureLines.add((ImageView) n);
    }

    /**
     * Sets up the note highlighting functionality.
     * @param staffPlayBars The bars that move to highlight different
     * notes.
     */
    private void initalizeStaffPlayBars(HBox playBars) {
        ArrayList<ImageView> staffPlayBars = new ArrayList<ImageView>();
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
     * Shifts the images on this staff image holder left by 1 step.
     * Does nothing if the left-hand position is already 0.
     */
    public void left() {

    }

    /**
     * Shifts the images on this staff image holder right by 1 step.
     * Does nothing if the right-hand position is already at the maximum,
     * unless the Advanced Mario Sequencer-style unlimited lines feature
     * has been enabled.
     */
    public void right() {

    }

    /**
     * Gets the current position that the staff is set to. This returns a line
     * number generally between 0 and 383 for 96-measure / 128-measure 4/4 and
     * 3/4 time, respectively.
     */
    private int getMeasurePosition() {
        return StateMachine.getMeasureLineNum();
    }

    /**
     * Sets the parent staff object to the specified object.
     * @param s The pointer to the parent staff object.
     */
    public void setStaff(Staff s) {
        theStaff = s;
    }

}
