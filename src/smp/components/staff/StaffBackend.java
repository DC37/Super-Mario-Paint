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



    /**
     * The HBox that contains the ImageView objects that are meant to
     * hold measure lines.
     */
    private HBox mLines;



    /**
     * These are the lines in which instrument sprite objects appear.
     */
    private ArrayList<VBox> noteLines;

    /**
     * This holds the lines that contain the ImageView objects that are meant
     * to hold the lines that appear when one places notes on the higher and
     * lower portions of the staff.
     */
    private HBox[] expLines;


    /** @param lines The list of flats / sharps / etc sprite VBoxes. */
    public void setAccidentalLines(ArrayList<VBox> lines) {
        accidentalLines = lines;
    }

    /**
     * @return The list of <code>HBox</code> objects that holds the
     * measure numbers.
     * 
     */
    public ArrayList<HBox> getMeasureNumBoxes() {
        return measureNumBoxes;
    }

    /**
     * @param mNum This holds the measure numbers.
     */
    public void setMeasureNumBoxes(ArrayList<HBox> mNum) {
        measureNumBoxes = mNum;
    }

}
