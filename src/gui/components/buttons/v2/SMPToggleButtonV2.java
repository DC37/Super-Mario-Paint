package gui.components.buttons.v2;

import javafx.scene.control.ToggleButton;

public class SMPToggleButtonV2 extends SMPAbstractToggleButton<ToggleButton> {

	public SMPToggleButtonV2() {
		this(null);
	}
	
	public SMPToggleButtonV2(String text) {
		super(() -> new ToggleButton(text));
	}
	
}
