package gui.components.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;

public class SMPTextInputDialog extends SMPAbstractDialog<String, TextInputDialog> {

	private Label deputyContentText = new Label();
	
	public SMPTextInputDialog() {
		super(new TextInputDialog(""));
	}
	
	@Override
	public void setTitle(String title) {
		dialog.setTitle(title);
	}
	
	@Override
	public void setContentText(String contentText) {
		deputyContentText.setText(contentText);
	}
	
	public void setPlaceholderText(String placeholderText) {
		if (placeholderText != null)
			dialog.getEditor().setPromptText(placeholderText);
	}
	
	@Override
	protected void customize() {
		super.customize();
		
		// Set the original content text to null as it won't be used.
		dialog.setContentText(null);
		
		// Use a VBox to put the label above the TextField.
		VBox content = new VBox(10, deputyContentText, dialog.getEditor());
		content.setPadding(new Insets(10));
		
		dialog.getDialogPane().setContent(content);
	}
	
}
