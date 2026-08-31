package gui.components.dialogs;

import java.util.Optional;
import java.util.function.BooleanSupplier;

import gui.components.SMPIconService;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

public abstract class SMPAbstractDialog<R, D extends Dialog<R>> {

	protected final D dialog;
	
	protected SMPAbstractDialog(D dialog, BooleanSupplier fnValidateParams) {
		if (fnValidateParams != null && !fnValidateParams.getAsBoolean())
			throw new IllegalArgumentException("Dialog cannot be constructed with the provided parameters!");
		
		this.dialog = dialog;
	}
	
	protected SMPAbstractDialog(D dialog) {
		this(dialog, null);
	}
	
	public abstract void setTitle(String title);
	public abstract void setContentText(String contentText);
	
	public Optional<R> showAndWait() {
		// Change anything in the dialog before showing.
		customize();
		
		return dialog.showAndWait();
	}
	
	protected void customize() {
		// Remove header from custom dialogs.
		dialog.setHeaderText(null);
		
		// Set icon for the custom dialog.
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(SMPIconService.getHeaderIcon());
	}
	
}
