package gui.components.dialogs;

import java.util.Optional;

import gui.Values;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class DialogUtils {

	private DialogUtils() {}
	
	private static void show(AlertType type, String title, String body) {
		SMPInfoDialog dialog = new SMPInfoDialog(type);
		
		dialog.setTitle(title);
		dialog.setContentText(body);
		
		dialog.showAndWait();
	}
	
	public static void showInfo(String title, String body) {
		show(AlertType.INFORMATION, title, body);
	}
	
	public static void showInfo(String body) {
		showInfo(Values.PROGRAM_NAME, body);
	}
	
	public static void showWarning(String title, String body) {
		show(AlertType.WARNING, title, body);
	}
	
	public static void showWarning(String body) {
		showWarning(Values.PROGRAM_NAME, body);
	}
	
	public static void showError(String title, String body) {
		show(AlertType.ERROR, title, body);
	}
	
	public static void showError(String body) {
		showError(Values.PROGRAM_NAME, body);
	}
	
	public static boolean showQuestion(String title, String body) {
		SMPQuestionDialog dialog = new SMPQuestionDialog();
		
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
	
	public static String requestInput(String title, String body, String placeholder) {
		SMPTextInputDialog dialog = new SMPTextInputDialog();
		
		dialog.setTitle(title);
		dialog.setContentText(body);
		dialog.setPlaceholderText(placeholder);
		
		return dialog.showAndWait().orElse("");
	}
	
	public static String requestInput(String title, String body) {
		return requestInput(title, body, null);
	}
	
	public static String requestInput(String body) {
		return requestInput(Values.PROGRAM_NAME, body);
	}
	
}
