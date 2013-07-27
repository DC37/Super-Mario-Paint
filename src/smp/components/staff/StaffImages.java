package smp.components.staff;

import java.util.ArrayList;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.SoundfontLoader;
import smp.components.Constants;
import smp.components.InstrumentIndex;
import smp.components.staff.sequences.Note;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.topPanel.ButtonLine;
import smp.stateMachine.StateMachine;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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

    /** Array of notes that we can see on the staff. */
    private Note[] staffNotes = {
            Note.A2, Note.B2, Note.C3, Note.D3, Note.E3, Note.F3, Note.G3,
            Note.A3, Note.B3, Note.C4, Note.D4, Note.E4, Note.F4, Note.G4,
            Note.A4, Note.B4, Note.C5, Note.D5};

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
     * The HBox that contains the ImageView objects that are meant to
     * hold measure numbers.
     */
    private HBox measureNums;

    /**
     * The list of HBoxes that the measureNums are supposed to actually
     * hold.
     */
    private ArrayList<HBox> measureNumBoxes;

    /**
     * The HBox that holds the ImageView objects that are meant to hold the
     * note highlighters.
     */
    private HBox playBars;

    /** These are the bars that highlight notes. */
    private ArrayList<ImageView> staffPlayBars;

    /**
     * The HBox that contains the ImageView objects that are meant to
     * hold measure lines.
     */
    private HBox mLines;

    /**
     * The ArrayList that holds the ImageView objects that the measure lines
     * object holds.
     */
    private ArrayList<ImageView> measureLines;

    /**
     * The HBox that contains the ImageView objects that are meant to hold
     * note images on the staff.
     */
    private HBox instruments;

    /**
     * The HBox that contains the ImageView objects that are meant to hold
     * the accidental (flat / sharp) images on the staff.
     */
    private HBox accidentals;

    /**
     * These are the lines in which instrument accidental flat / sharp sprite
     * objects appear.
     */
    private ArrayList<VBox> accidentalLines;

    /**
     * This holds the lines that contain the ImageView objects that are meant
     * to hold the lines that appear when one places notes on the higher and
     * lower portions of the staff.
     */
    private HBox[] expLines;

    /**
     * The Image for a key measure line.
     */
    private Image keyLine;

    /**
     * The Image for a regular measure line.
     */
    private Image regularLine;

    /**
     * A matrix of notes, which will be displayed on the screen
     * at any time.
     */
    private ArrayList<ArrayList<StackPane>> noteMatrix;

    /**
     * The StaffNoteLines that will be associated with the note matrix
     * of images above.
     */
    private ArrayList<StaffNoteLine> staffNoteLines;

    /**
     * The digits, 0-9, to be used for numbering the staff measures.
     */
    private ArrayList<Image> digits;

    /**
     * These are the lines in which instrument sprite objects appear.
     */
    private ArrayList<VBox> noteLines;



    /**
     * Instantiates this wrapper class with the correct HBox objects
     * such that it can begin keeping track of whatever's happening
     * on the staff, at least on the measure lines side.
     * @param staffPlayBars The bars that show when a line of notes is played.
     * @param staffMLines These are the lines where notes are placed.
     * @param staffInstruments The instrument image boxes that hold the
     * instruments that should appear on the staff.
     * @param staffAccidentals These are the sharps and flats that you can
     * place.
     * @param staffExpLines These are the lines that appear under notes for the
     * lower and upper portions of the staff.
     * @param staffMeasureNumbers These are the numbers above each measure
     * line.
     */
    public StaffImages(HBox staffMLines, HBox staffPlayBars,
            HBox staffInstruments, HBox staffAccidentals, HBox[] staffExpLines,
            HBox staffMeasureNumbers) {
        mLines      = staffMLines;
        playBars    = staffPlayBars;
        instruments = staffInstruments;
        accidentals = staffAccidentals;
        expLines    = staffExpLines;
        measureNums = staffMeasureNumbers;

        initializeStaffMeasureLines();
        initalizeStaffPlayBars();
        initializeStaffMeasureNums();
        initializeStaffInstruments();
        initializeStaffAccidentals();

    }

    /**
     * These are the numbers above each successive measure.
     */
    private void initializeStaffMeasureNums() {
        measureNumBoxes = new ArrayList<HBox>();
        for(Node num : measureNums.getChildren())
            measureNumBoxes.add((HBox) num);
    }

    /**
     * Sets up the various note lines of the staff. These
     * are the notes that can appear on the staff.
     */
    private void initializeStaffInstruments() {
        noteLines = new ArrayList<VBox>();
        for (Node n : instruments.getChildren())
            noteLines.add((VBox) n);
        for (VBox v : noteLines) {
            int pos = 1;
            ArrayList<StackPane> a = new ArrayList<StackPane>();
            for (Node s : v.getChildren()) {
                addListeners((StackPane) s, Constants.NOTES_IN_A_LINE - pos);
                a.add((StackPane) s);
                pos++;
            }
            //noteMatrix.add(a);
        }
    }

    /**
     * Adds the requisite listeners to the StackPanes of the instrument
     * note matrix.
     * @param s The StackPane that will be holding some instrument.
     * @param pos The position that this StackPane will be associated with.
     */
    private void addListeners(final StackPane s, final int pos) {
        s.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {

            /** The position of this note. */
            private int position = pos;

            @Override
            public void handle(MouseEvent event) {
                ImageView theImage = new ImageView();
                InstrumentIndex theInd =
                        ButtonLine.getSelectedInstrument();
                theImage.setImage(
                        ImageLoader.getSpriteFX(
                                theInd.imageIndex()));
                s.getChildren().add(theImage);
                playSound(theInd, position);
            }

        });

        s.addEventHandler(MouseEvent.ANY,
                new EventHandler<MouseEvent>() {

            /** The silhouette image of the instrument. */
            ImageView theImage = new ImageView();

            @Override
            public void handle(MouseEvent event) {
                if (event.equals(Event.MOUSE_ENTERED))
                    ImageView theImage = new ImageView();
                InstrumentIndex theInd =
                        ButtonLine.getSelectedInstrument();
                theImage.setImage(
                        ImageLoader.getSpriteFX(
                                theInd.imageIndex().silhouette()));
                s.getChildren().add(theImage);
            }
        });


    }

    /**
     * These are the lines that divide up the staff.
     */
    private void initializeStaffMeasureLines() {
        measureLines = new ArrayList<ImageView>();
        for (Node n : mLines.getChildren())
            measureLines.add((ImageView) n);
    }


    /** These are the sharps, flats, etc */
    private void initializeStaffAccidentals() {
        accidentalLines = new ArrayList<VBox>();
        for (Node n : accidentals.getChildren())
            accidentalLines.add((VBox) n);
    }

    /**
     * Sets up the note highlighting functionality.
     */
    private void initalizeStaffPlayBars() {
        staffPlayBars = new ArrayList<ImageView>();
        for (Node n : playBars.getChildren())
            staffPlayBars.add((ImageView) n);
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
     * Plays a sound given an index and a position.
     * @param theInd The index at which this instrument is located at.
     * @param pos The position at which this note is located at.
     */
    private void playSound(InstrumentIndex theInd, int pos) {
        int acc;
        if (StateMachine.isAltPressed() || StateMachine.isCtrlPressed())
            acc = -1;
        else if (StateMachine.isShiftPressed())
            acc = 1;
        else
            acc = 0;
        SoundfontLoader.playSound(staffNotes[pos].getKeyNum(), theInd, acc);
    }
}
