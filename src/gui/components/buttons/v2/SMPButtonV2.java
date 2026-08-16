package gui.components.buttons.v2;

import javafx.scene.control.Button;

public class SMPButtonV2 extends SMPAbstractButton<Button> {
	
	public SMPButtonV2() {
		this(null);
	}
	
	public SMPButtonV2(String text) {
		super(() -> new Button(text));
	}
	
}
