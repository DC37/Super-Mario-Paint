package gui.components.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class SMPInfoDialog extends SMPAbstractDialog<ButtonType, Alert> {

	public SMPInfoDialog(AlertType type) {
		super(new Alert(type), () -> type != AlertType.CONFIRMATION);
	}
	
	@Override
	public void setTitle(String title) {
		dialog.setTitle(title);
	}
	
	@Override
	public void setContentText(String contentText) {
		dialog.setContentText(contentText);
	}
	
}
