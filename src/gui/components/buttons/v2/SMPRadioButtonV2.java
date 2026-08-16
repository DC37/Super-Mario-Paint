package gui.components.buttons.v2;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Skin;

public class SMPRadioButtonV2 extends SMPAbstractButton<RadioButton> {
	
	private static final String STYLE_CLASS_RADIO_NO_CIRCLE = "radio-button-no-circle";
	
	public SMPRadioButtonV2() {
		this(null);
	}
	
	public SMPRadioButtonV2(String text) {
		super(() -> new RadioButton(text));
	}
	
	@Override
	public BooleanProperty selectedProperty() {
		return innerBtn.selectedProperty();
	}
	
	@Override
    public final boolean isSelected() {
        return innerBtn.isSelected();
    }
	
	@Override
	public void setSelected(boolean value) {
        innerBtn.setSelected(value);
    }
	
	@Override
	public String getClassStyleCssName() {
		return STYLE_CLASS_RADIO_NO_CIRCLE;
	}
	
	@Override
	protected Skin<?> createDefaultSkin() {
		return new SMPToggleButtonSkin<>(this);
	}
	
}
