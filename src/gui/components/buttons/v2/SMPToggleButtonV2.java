package gui.components.buttons.v2;

import javafx.scene.control.ToggleButton;

/**
 * <p>A toggle button displayed as an image. See {@link SMPAbstractToggleButton}.</p>
 */
public class SMPToggleButtonV2 extends SMPAbstractToggleButton<ToggleButton> {

	public SMPToggleButtonV2() {
		this(null);
	}
	
	public SMPToggleButtonV2(String text) {
		super(() -> new ToggleButton(text));
	}
	
}
