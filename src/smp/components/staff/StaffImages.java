package smp.components.staff;

import java.util.ArrayList;

import smp.ImageIndex;
import smp.ImageLoader;
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
     * The ArrayList that holds the ImageView objects that the measure lines
     * object holds.
     */
    private ArrayList<ImageView> measureLines;


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

        for (int line = 0; line < noteLines.size(); line++) {
            VBox verticalHolder = noteLines.get(line);
            VBox accVerticalHolder = accidentalLines.get(line);

            ObservableList<Node> lineOfNotes = verticalHolder.getChildren();
            ObservableList<Node> lineOfAcc = accVerticalHolder.getChildren();

            ArrayList<StackPane> notes = new ArrayList<StackPane>();
            ArrayList<StackPane> accs = new ArrayList<StackPane>();

            for (int pos = 1; pos <= Constants.NOTES_IN_A_LINE; pos++) {
                StackPane note = (StackPane) lineOfNotes.get(pos - 1);
                StackPane acc = (StackPane) lineOfAcc.get(pos - 1);
                notes.add(note);
                accs.add(acc);
                addListeners(note, acc, Constants.NOTES_IN_A_LINE - pos, line);
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
     * @param line The line that this StackPane is on.
     */
    private void addListeners(final StackPane s, final StackPane acc,
            final int pos, final int line) {
        s.addEventHandler(MouseEvent.ANY,
                new StaffInstrumentEventHandler(s, acc, pos, line,
                        theStaff));
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
            if (i % Constants.DEFAULT_TIMESIG_BEATS == 0)
                measureLines.get(i).setImage(ImageLoader.getSpriteFX(
                        ImageIndex.STAFF_MLINE));
            else
                measureLines.get(i).setImage(ImageLoader.getSpriteFX(
                        ImageIndex.STAFF_LINE));
        }
    }

    /** Redraws the staff measure lines. */
    public void updateStaffMeasureLines() {
        int currLine = StateMachine.getMeasureLineNum();
        for (int i = 0; i < measureLines.size(); i++) {
            if ((currLine + i) % Constants.DEFAULT_TIMESIG_BEATS == 0)
                measureLines.get(i).setImage(ImageLoader.getSpriteFX(
                        ImageIndex.STAFF_MLINE));
            else
                measureLines.get(i).setImage(ImageLoader.getSpriteFX(
                        ImageIndex.STAFF_LINE));
        }
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
