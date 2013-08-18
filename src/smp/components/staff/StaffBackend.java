package smp.components.staff;

import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This class will hold the note matrix behind the staff. It will
 * also take care of the MIDI sequencing, etc.
 * @author RehdBlob
 * @since 2013.08.16
 */
public class StaffBackend {

    /**
     * These are the lines in which instrument accidental flat / sharp sprite
     * objects appear.
     */
    private static ArrayList<VBox> accidentalLines;

    /** @param lines The list of flats / sharps / etc sprite VBoxes. */
    public void setAccidentalLines(ArrayList<VBox> lines) {
        accidentalLines = lines;
    }

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
     * These are the lines in which instrument sprite objects appear.
     */
    private ArrayList<VBox> noteLines;

}
