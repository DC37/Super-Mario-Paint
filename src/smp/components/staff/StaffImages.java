package smp.components.staff;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.Values;
import smp.components.staff.sequences.StaffNoteLine;
import smp.fx.SMPFXController;

/**
 * Wrapper class for all of the images that appear on the Staff of Super Mario
 * Paint. Contains the sprite holders for the images that represent notes, the
 * ImageView holders for each of the measure lines, the lines that allow one to
 * display notes higher than a G or lower than a D on the staff, and the measure
 * numbers.
 *
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
     * The ArrayList that holds the Text objects that will hold the measure line
     * numbers.
     */
    private ArrayList<Text> measureNums;

    /** This is the FXML controller class. */
    private SMPFXController controller;

    /** This is the ImageLoader class. */
    private ImageLoader il;

    /** The ledger lines. */
    private HBox[] staffLLines;

    /** The ledger lines at the high C of the staff. */
    private ArrayList<Node> highC;

    /** The ledger lines at the high A of the staff. */
    private ArrayList<Node> highA;

    /** The ledger lines at the low C of the staff. */
    private ArrayList<Node> lowC;

    /** The ledger lines at the low A of the staff. */
    private ArrayList<Node> lowA;

    /**
     * This is the list of lists of the ledger lines, so that we can quickly
     * hide everything using a loop, if necessary.
     */
    private ArrayList<ArrayList<Node>> theLLines = new ArrayList<ArrayList<Node>>();

    /**
     * The parent staff object.
     */
    private Staff theStaff;

    /**
     * Constructor that also sets up the staff ledger lines.
     */
    public StaffImages(ImageLoader i) {
        il = i;
    }

    /**
     * Instantiates this wrapper class with the correct HBox objects such that
     * it can begin keeping track of whatever's happening on the staff, at least
     * on the measure lines side.
     */
    public void initialize() {

        initializeStaffMeasureLines(controller.getStaffMeasureLines());
        initializeStaffMeasureNums(controller.getStaffMeasureNums());
        initializeStaffLedgerLines();
        initializeStaffInstruments(controller.getStaffInstruments(),
                controller.getStaffAccidentals());
        initializeVolumeBars(controller.getVolumeBars());
        initializeVolumeBarLinks();
    }
    /**
     * Sets up the links between the volume bars display and StaffNoteLines.
     */
    private void initializeVolumeBarLinks() {
        for (int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++) {
            StaffVolumeEventHandler sveh = theStaff.getNoteMatrix()
                    .getVolHandler(i);
            StaffNoteLine stl = theStaff.getSequence().getLineSafe(i);
            sveh.setStaffNoteLine(stl);
        }
    }

    /**
     * Initializes the volume bars in the program.
     *
     * @param volumeBars
     *            This is the HBox that holds all of the volume bar
     *            <code>StackPane</code> objects.
     */
    private void initializeVolumeBars(HBox volumeBars) {
        ArrayList<StackPane> vol = new ArrayList<StackPane>();
        for (Node v : volumeBars.getChildren()) {
            StackPane volBar = (StackPane) v;
            vol.add(volBar);
            StaffVolumeEventHandler sveh = new StaffVolumeEventHandler(volBar,
                    il, controller.getModifySongManager());
            volBar.addEventHandler(Event.ANY, sveh);
            theStaff.getNoteMatrix().addVolHandler(sveh);
        }
        theStaff.getNoteMatrix().setVolumeBars(vol);

    }

    /**
     * These are the numbers above each successive measure.
     */
    private void initializeStaffMeasureNums(HBox mNums) {
        ArrayList<HBox> measureNumBoxes = new ArrayList<HBox>();
        measureNums = new ArrayList<Text>();
        for (Node num : mNums.getChildren())
            measureNumBoxes.add((HBox) num);
        int counter = 1;
        for (int i = 0; i < measureNumBoxes.size(); i++) {
            HBox theBox = measureNumBoxes.get(i);
            Text t = new Text();
            theBox.getChildren().add(t);
            measureNums.add(t);
            if (i % Values.TIMESIG_BEATS == 0) {
                t.setText(String.valueOf(counter));
                counter++;
            } else
                continue;
        }
    }

    /**
     * Sets up the various note lines of the staff. These are the notes that can
     * appear on the staff. This method also sets up sharps, flats, etc.
     *
     * @param accidentals
     *            The HBox that holds the framework for the sharps / flats.
     * @param instruments
     *            The HBox that holds the framework for the instruments.
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

            for (int pos = 1; pos <= Values.NOTES_IN_A_LINE; pos++) {
                StackPane note = (StackPane) lineOfNotes.get(pos - 1);
                StackPane acc = (StackPane) lineOfAcc.get(pos - 1);
                notes.add(note);
                accs.add(acc);
            }

            staffMatrix.addLine(notes);
            staffMatrix.addAccLine(accs);
        }
    }

    /**
     * These are the lines that divide up the staff.
     *
     * @param staffMLines
     *            The measure lines that divide the staff.
     */
    private void initializeStaffMeasureLines(HBox mLines) {
        measureLines = new ArrayList<ImageView>();
        for (Node n : mLines.getChildren())
            measureLines.add((ImageView) n);
        for (int i = 0; i < measureLines.size(); i++) {
            if (i % Values.TIMESIG_BEATS == 0)
                measureLines.get(i).setImage(
                        il.getSpriteFX(ImageIndex.STAFF_MLINE));
            else
                measureLines.get(i).setImage(
                        il.getSpriteFX(ImageIndex.STAFF_LINE));
        }
    }

    /**
     * Redraws the staff measure lines and numbers.
     *
     * @param currLine
     *            The current line that we are on.
     */
    public void updateStaffMeasureLines(int currLine) {
        int counter = 0;
        for (int i = 0; i < measureLines.size(); i++) {
            ImageView currImage = measureLines.get(i);
            Text currText = measureNums.get(i);
            if ((currLine + i) % Values.TIMESIG_BEATS == 0) {
                currImage.setImage(il.getSpriteFX(ImageIndex.STAFF_MLINE));
                currText.setText(String.valueOf((int) (Math.ceil(currLine
                        / (double) Values.TIMESIG_BEATS) + 1 + counter)));
                counter++;
            } else {
                currImage.setImage(il.getSpriteFX(ImageIndex.STAFF_LINE));
                currText.setText("");
            }
        }
    }

    /**
     * Sets up the staff expansion lines, which are to hold notes that are
     * higher than or lower than the regular lines of the staff.
     *
     * @param staffLLines
     *            An array of ledger lines. This method expects that there will
     *            be four of these, two of which indicate the lines above the
     *            staff and the other of which indicating the lines below the
     *            staff.
     */
    private void initializeStaffLedgerLines() {
        highC = new ArrayList<Node>();
        highA = new ArrayList<Node>();
        lowC = new ArrayList<Node>();
        lowA = new ArrayList<Node>();
        highC.addAll(staffLLines[0].getChildren());
        highA.addAll(staffLLines[1].getChildren());
        lowC.addAll(staffLLines[2].getChildren());
        lowA.addAll(staffLLines[3].getChildren());
        theLLines.add(highC);
        theLLines.add(highA);
        theLLines.add(lowC);
        theLLines.add(lowA);
        hideAllLedgerLines();
    }

    /**
     * Hides all of the ledger lines.
     */
    public void hideAllLedgerLines() {

        for (ArrayList<Node> n : theLLines)
            for (Node nd : n)
                nd.setVisible(false);

    }

    /**
     * Sets the parent staff object to the specified object.
     *
     * @param s
     *            The pointer to the parent staff object.
     */
    public void setStaff(Staff s) {
        theStaff = s;
    }

    /**
     * Sets the controller class.
     *
     * @param ct
     *            The FXML controller class.
     */
    public void setController(SMPFXController ct) {
        controller = ct;
    }

    /**
     * @param l
     *            The array of <code>HBox</code>es that contains the rectangles
     *            for the ledger lines.
     */
    public void setLedgerLines(HBox[] l) {
        staffLLines = l;
    }

    /**
     * @return The array of <code>HBox</code>es that contains the rectangles for
     *         the ledger lines.
     */
    public HBox[] getLedgerLines() {
        return staffLLines;
    }

    /** @return The ledger lines at position high C. */
    public ArrayList<Node> highC() {
        return highC;
    }

    /** @return The ledger lines at position high A. */
    public ArrayList<Node> highA() {
        return highA;
    }

    /** @return The ledger lines at position low C. */
    public ArrayList<Node> lowC() {
        return lowC;
    }

    /** @return The ledger lines at position low C. */
    public ArrayList<Node> lowA() {
        return lowA;
    }

}
