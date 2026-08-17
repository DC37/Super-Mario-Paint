package gui.components.buttons;

import javafx.scene.control.ToggleButton;

/**
 * <p>A toggle button displayed as an image. See {@link SMPAbstractToggleButton}.</p>
 */
public class SMPToggleButton extends SMPAbstractToggleButton<ToggleButton> {

	public SMPToggleButton() {
		this(null);
	}
	
	public SMPToggleButton(String text) {
		super(() -> new ToggleButton(text));
	}
	
}
