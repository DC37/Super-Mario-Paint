package gui.components.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class SMPQuestionDialog extends SMPAbstractDialog<ButtonType, Alert> {

	public SMPQuestionDialog() {
		super(new Alert(AlertType.CONFIRMATION));
	}
	
	@Override
	public void setTitle(String title) {
		dialog.setTitle(title);
	}
	
	@Override
	public void setContentText(String contentText) {
		dialog.setContentText(contentText);
	}
	
	@Override
	protected void customize() {
		super.customize();
		
		// Set the buttons to "YES" / "NO".
		DialogPane dp = dialog.getDialogPane();
		
		dp.getButtonTypes().clear();
		dp.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
	}
	
}
