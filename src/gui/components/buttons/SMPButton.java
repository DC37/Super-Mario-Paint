package gui.components.buttons;

import javafx.scene.control.Button;

/**
 * <p>A button that is displayed as an image. Two images can be registered as properties:
 * one for when the button is released and one for when the button is pressed.</p>
 */
public class SMPButton extends SMPAbstractButton<Button> {
	
	public SMPButton() {
		this(null);
	}
	
	public SMPButton(String text) {
		super(() -> new Button(text));
	}
	
}
