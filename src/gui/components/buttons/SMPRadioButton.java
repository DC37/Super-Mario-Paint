package gui.components.buttons;

import java.util.List;
import java.util.stream.Stream;

import javafx.scene.control.RadioButton;

/**
 * <p>A radio button displayed as an image. See {@link SMPAbstractToggleButton}.</p>
 */
public class SMPRadioButton extends SMPAbstractToggleButton<RadioButton> {
	
	private static final String STYLE_CLASS_RADIO_NO_CIRCLE = "radio-button-no-circle";
	
	public SMPRadioButton() {
		this(null);
	}
	
	public SMPRadioButton(String text) {
		super(() -> new RadioButton(text));
	}
	
	@Override
	public List<String> getClassStyleCssNames() {
		return Stream.concat(
				super.getClassStyleCssNames().stream(),
				List.of(STYLE_CLASS_RADIO_NO_CIRCLE).stream()
		).toList();
	}
	
}
