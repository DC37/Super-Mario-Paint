package gui.components.dialogs;

import java.util.Optional;

import gui.Values;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

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
	
	public static boolean showQuestion(String title, String body) {
		Alert dialog = new Alert(AlertType.CONFIRMATION);
		
		dialog.setTitle(title);
		dialog.setContentText(body);
		
		Optional<ButtonType> result = dialog.showAndWait();
		
		if (result.isEmpty())
			return false;
		
		return result.get().getButtonData().isDefaultButton();
	}
	
	public static boolean showQuestion(String body) {
		return showQuestion(Values.PROGRAM_NAME, body);
	}
	
}
