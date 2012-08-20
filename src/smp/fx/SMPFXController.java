package smp.fx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * The Controller class for most of the program. This will
 * handle the events that happen on the screen.
 * @author RehdBlob
 * @since 2012.08.16
 */
public class SMPFXController implements Initializable {
	
	/**
	 * Top pane, box in the bottom-mid-center.
	 */
	@FXML
	private static HBox instLine;
	
	@FXML
	private static ImageView instLineImg;
	
	/**
	 * Top pane, box at the left.
	 */
	@FXML
	private static StackPane selectedInst;
	
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

	}
	
	public static void check() {
	}
	


}
