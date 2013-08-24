package smp.fx;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.controls.Controls;
import smp.components.staff.Staff;
import smp.components.topPanel.ButtonLine;


/**
 * The Controller class for most of the program. This will
 * handle the events that happen on the screen.
 * @author RehdBlob
 * @since 2012.08.16
 */
public class SMPFXController implements Initializable {

    /**
     * The image that shows the selected instrument.
     */
    @FXML
    private static ImageView selectedInst;

    /**
     * Instrument line.
     */
    @FXML
    private static HBox instLine;

    /**
     * The line of buttons that corresponds with the line of images
     * at the top of the frame.
     */
    private static ButtonLine instBLine;

    /**
     * The staff that notes, measure lines, and sprites will be placed
     * on.
     */
    private static Staff staff;

    /**
     * The controls line that includes the play button, stop button,
     * loop button, etc.
     */
    @FXML
    private static StackPane controls;


    /**
     * The controls line object that holds the FXML controls object.
     */
    private static Controls controlPanel;

    /**
     * Lines that appear when a note is placed above the standard
     * staff lines. High C lines.
     */
    @FXML
    private static HBox staffExtLinesHighC;

    /**
     * Lines that appear when a note is placed above the standard
     * staff lines. High A lines.
     */
    @FXML
    private static HBox staffExtLinesHighA;

    /**
     * Lines that appear when a note is placed above the standard
     * staff lines. Low C lines.
     */
    @FXML
    private static HBox staffExtLinesLowC;

    /** The staff measure lines. */
    @FXML
    private static HBox staffMeasureLines;

    /** The staff measure numbers. */
    @FXML
    private static HBox staffMeasureNumbers;

    /**
     * The staff layer that displays the bar that plays notes.
     */
    @FXML
    private static HBox staffPlayBars;

    /**
     * The staff layer that displays the instruments that have been placed
     * on the staff. Note: Images should be spaced 16 px.
     */
    @FXML
    private static HBox staffInstruments;

    /**
     * The staff layer that displays the instrument accidentals that have
     * been placed on the staff.
     */
    @FXML
    private static HBox staffAccidentals;

    /** The scrollbar that moves the staff. */
    @FXML
    private static Slider scrollbar;

    /**
     * The left arrow that you can click to make the staff go to the left.
     */
    @FXML
    private static ImageView leftArrow;

    /**
     * The right arrow that you can click to make the staff go to the right.
     */
    @FXML
    private static ImageView rightArrow;

    /**
     * The left arrow that you can click to make the staff go to the left quickly.
     */
    @FXML
    private static ImageView leftFastArrow;

    /**
     * The left arrow that you can click to make the staff go to the right quickly.
     */
    @FXML
    private static ImageView rightFastArrow;

    /**
     * Initializes the Controller class for Super Mario Paint
     * @param fileLocation The location that the files are located, passed
     * by the FXMLLoader class.
     * @param resources A ResourceBundle.
     */
    @Override
    public void initialize(URL fileLocation, ResourceBundle resources) {
        // Currently does nothing.
    }

    /**
     * Sets up event handlers for the different parts of the Super Mario
     * Paint GUI.
     */
    public static void initializeHandlers() {
        /* Initialize the selected instrument handlers in the ButtonLine. */
        instBLine = new ButtonLine(instLine, selectedInst);

        selectedInst.setImage(ImageLoader.getSpriteFX(ImageIndex.MARIO));

        HBox[] staffExtLines = {staffExtLinesHighC, staffExtLinesHighA,
                staffExtLinesLowC};

        staff = new Staff(staffExtLines);

        controlPanel = new Controls(staff);

        // At this point, everything should be initialized and ready to
        // respond to user input.
    }

    /**
     * @return The <code>HBox</code> that holds the staff measure lines.
     */
    public static HBox getStaffMeasureLines() {
        return staffMeasureLines;
    }

    /**
     * @return The <code>HBox</code> that holds the staff play bars.
     */
    public static HBox getStaffPlayBars() {
        return staffPlayBars;
    }

    /**
     * @return The <code>HBox</code> that holds the staff measure numbers.
     */
    public static HBox getStaffMeasureNums() {
        return staffMeasureNumbers;
    }

    /**
     * @return The <code>HBox</code> that holds the staff flats / sharps / etc.
     */
    public static HBox getStaffAccidentals() {
        return staffAccidentals;
    }

    /**
     * @return The <code>HBox</code> that holds the staff instruments.
     */
    public static HBox getStaffInstruments() {
        return staffInstruments;
    }

    /**
     * @return The <code>Slider</code> that we will use to navigate the staff.
     */
    public static Slider getScrollbar() {
        return scrollbar;
    }

    /**
     * @return The <code>ImageView</code> that holds the fast left
     * navigation arrow of the staff.
     */
    public static ImageView getLeftFastArrow() {
        return leftFastArrow;
    }

    /**
     * @return The <code>ImageView</code> that holds the right navigation arrow
     * of the staff.
     */
    public static ImageView getRightArrow() {
        return rightArrow;
    }


    /**
     * @return The <code>ImageView</code> that holds the left navigation arrow
     * of the staff.
     */
    public static ImageView getRightFastArrow() {
        return rightFastArrow;
    }


    /**
     * @return The <code>ImageView</code> that holds the fast right
     * navigation arrow of the staff.
     */
    public static ImageView getLeftArrow() {
        return leftArrow;
    }


}
