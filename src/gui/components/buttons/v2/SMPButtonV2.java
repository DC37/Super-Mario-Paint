package gui.components.buttons.v2;

import javafx.scene.control.Button;

/**
 * <p>A button that is displayed as an image. Two images can be registered as properties:
 * one for when the button is released and one for when the button is pressed.</p>
 */
public class SMPButtonV2 extends SMPAbstractButton<Button> {
	
	public SMPButtonV2() {
		this(null);
	}
	
	public SMPButtonV2(String text) {
		super(() -> new Button(text));
	}
	
}
