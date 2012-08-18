package smp.fx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * The Controller class for most of the program. This will
 * handle the events that happen on the screen.
 * @author RehdBlob
 * @since 2012.08.16
 */
public class SMPFXController implements Initializable {
	
	/**
	 * Top pane, box at the right.
	 */
	@FXML
	private static HBox topBoxRight;
	
	/**
	 * Top pane, box in the top-mid-center. 
	 */
	@FXML
	private static HBox topBoxTop;
	
	/**
	 * Top pane, box in the bottom-mid-center.
	 */
	@FXML
	private static HBox instLine;
	
	/**
	 * Top pane, box at the left.
	 */
	@FXML
	private static StackPane selectedInst;
	
	/**
	 * Middle pane, stackpane holding the staff.
	 */
	@FXML
	private static StackPane staffPane;
	
	/**
	 * Middle pane, staff image.
	 */
	@FXML
	private static ImageView staff;
	
	/**
	 * Middle pane, volume control stackPane.
	 */
	@FXML
	private static StackPane volPane;
	
	@FXML
	private static StackPane controls;
	
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
