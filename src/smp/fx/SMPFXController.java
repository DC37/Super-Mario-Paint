package smp.fx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import smp.components.topPanel.SelectedInstrument;

/**
 * The Controller class for most of the program. This will
 * handle the events that happen on the screen.
 * @author RehdBlob
 * @since 2012.08.16
 */
public class SMPFXController implements Initializable {
	
	/**
	 * Top pane, box at the left.
	 */
	@FXML
	private static StackPane selectedInst;
	
	/**
	 * The picture that displays the currently-selected instrument.
	 */
	private static SelectedInstrument selectedInstPic;
	
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
	
	/**
	 * Basically allows me to move some code out of the controller class
	 * and into other classes.
	 */
	public static void castCustom() {
		selectedInstPic = (SelectedInstrument) selectedInst.getChildren().get(0);
	}
	
	/**
	 * Sets up event handlers for the different parts of the Super Mario Paint GUI.
	 */
	public static void initializeHandlers() {
	}


}
