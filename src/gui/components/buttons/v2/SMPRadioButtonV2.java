package gui.components.buttons.v2;

import javafx.scene.control.RadioButton;

public class SMPRadioButtonV2 extends SMPAbstractToggleButton<RadioButton> {
	
	private static final String STYLE_CLASS_RADIO_NO_CIRCLE = "radio-button-no-circle";
	
	public SMPRadioButtonV2() {
		this(null);
	}
	
	public SMPRadioButtonV2(String text) {
		super(() -> new RadioButton(text));
	}
	
	@Override
	public String getClassStyleCssName() {
		return STYLE_CLASS_RADIO_NO_CIRCLE;
	}
	
}
