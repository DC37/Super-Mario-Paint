package gui.components.dialogs;

import gui.Values;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DialogUtils {

	private DialogUtils() {}
	
	public static void showInfo(String title, String body) {
		Alert dialog = new Alert(AlertType.INFORMATION);
		
		dialog.setTitle(title);
		dialog.setContentText(body);
		
		dialog.showAndWait();
	}
	
	public static void showInfo(String body) {
		showInfo(Values.PROGRAM_NAME, body);
	}
	
}
