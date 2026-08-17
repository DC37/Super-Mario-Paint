package gui.components.buttons;

import java.util.function.Supplier;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Skin;
import javafx.scene.control.ToggleButton;

/**
 * The base class for SMP toggle buttons. It enhances functionality for
 * any control that extends {@link ToggleButton}.
 *  
 * @param <B> The <code>ToggleButton</code> to enhance.
 */
public abstract class SMPAbstractToggleButton<B extends ToggleButton> extends SMPAbstractButton<B> {

	protected SMPAbstractToggleButton(Supplier<B> fnCreateInnerBtn) {
		super(fnCreateInnerBtn);
	}
	
	@Override
	public BooleanProperty selectedProperty() {
		return innerBtn.selectedProperty();
	}
	
	@Override
    public boolean isSelected() {
        return innerBtn.isSelected();
    }
	
	@Override
	public void setSelected(boolean value) {
        innerBtn.setSelected(value);
    }
	
	@Override
	protected Skin<?> createDefaultSkin() {
		return new SMPToggleButtonSkin<>(this);
	}
	
}
