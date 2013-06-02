package smp.fx;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import smp.ImageIndex;
import smp.ImageLoader;
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
     * These are the lines that appear when one places notes on the higher and
     * lower portions of the staff.
     */
    @FXML
    private static HBox staffExpLines;

    /**
     * The staff measure lines.
     */
    @FXML
    private static HBox staffMeasureLines;

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
        staff = new Staff(staffMeasureLines, staffPlayBars, staffInstruments,
                staffAccidentals, staffExpLines);
        staff.draw();

        // Initialize play button
        // Controls.setPlayButton();

        // Initialize stop button
        // Controls.setStopButton();

        // Initialize loop button
        // Controls.setLoopButton();
    }


}
