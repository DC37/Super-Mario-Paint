package smp.components.staff;

import java.util.ArrayList;

import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
@SuppressWarnings("unused")
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
     * The HBox that contains the ImageView objects that are meant to
     * hold measure numbers.
     */
    private HBox measureNums;

    /**
     * The HBox that holds the ImageView objects that are meant to hold the
     * note highlighters.
     */
    private HBox playBars;

    /**
     * The HBox that contains the ImageView objects that are meant to
     * hold measure lines.
     */
    private HBox mLines;

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
     * This holds the lines that contain the ImageView objects that are meant
     * to hold the lines that appear when one places notes on the higher and
     * lower portions of the staff.
     */
    private HBox expLines;

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
    private ArrayList<StaffNoteLine> noteMatrix;

    /**
     * The digits, 0-9, to be used for numbering the staff measures.
     */
    private ArrayList<Image> digits;

    /**
     * These are the lines in which instrument sprite objects appear.
     */
    private ArrayList<VBox> noteLines;

    /**
     * These are the lines in which instrument accidental flat / sharp sprite
     * objects appear.
     */
    private ArrayList<VBox> accidentalLines;

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
     */
    public StaffImages(HBox staffMLines, HBox staffPlayBars,
            HBox staffInstruments, HBox staffAccidentals, HBox staffExpLines) {
        mLines = staffMLines;
        playBars = staffPlayBars;
        instruments = staffInstruments;
        accidentals = staffAccidentals;
        expLines = staffExpLines;

        initializeMeasureNums();
        initializeStaffMeasureLines();
        initializeStaffNoteLines();
        initalizeNoteHighlights();

    }

    /**
     * Sets up the staff measure lines with their
     */
    private void initializeStaffMeasureLines() {

    }

    /**
     * Sets up the various staff note lines on the staff.
     */
    private void initializeStaffNoteLines() {

    }

    /**
     * Propagates the measure numbers across the staff.
     */
    private void initializeMeasureNums() {

    }

    /**
     * Sets up the note highlighting functionality.
     */
    private void initalizeNoteHighlights() {

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


}
