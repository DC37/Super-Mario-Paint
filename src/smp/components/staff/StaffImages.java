package smp.components.staff;

import java.util.ArrayList;

import smp.components.staff.sequences.StaffNote;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

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
     * The HBox that contains the ImageView objects that are meant to
     * hold measure numbers.
     */
    private HBox measureNums;

    /**
     * The HBox that contains the ImageView objects that are meant to
     * hold measure lines.
     */
    private HBox staffMLines;

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
    private ArrayList<ArrayList<ImageView>> noteImageMatrix;

    /**
     * Holds whatever notes that the user decides to put on the staff.
     */
    private ArrayList<ArrayList<StaffNote>> noteMatrix;

    /**
     * Instantiates this wrapper class with the correct HBox objects
     * such that it can begin keeping track of whatever's happening
     * on the staff, at least on the measure lines side.
     */
    public StaffImages() {

    }

    /**
     * Initializes the note matrix with ImageViews as notes.
     */
    private void initializeNoteMatrix() {
        noteImageMatrix = new ArrayList<ArrayList<ImageView>>();
        noteMatrix = new ArrayList<ArrayList<StaffNote>>();
    }

    /**
     * Initializes the staff measure lines with ImageViews as display
     * elements.
     */
    private void initializeMeasureLines() {

    }

}
