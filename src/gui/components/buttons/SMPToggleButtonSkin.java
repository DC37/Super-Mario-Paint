package gui.components.buttons;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;

public class SMPToggleButtonSkin<B extends ToggleButton> extends SMPButtonSkin<B> {
	
	protected SMPToggleButtonSkin(SMPAbstractButton<B> smpBtn) {
		super(smpBtn);
	}
	
	@Override
	public void install() {
		ObservableValue<Boolean> conditionPressed = Bindings.or(smpBtn.armedProperty(), smpBtn.selectedProperty());
		imgSub = subscribeNodeProperty(conditionPressed, smpBtn.graphicProperty());
	}

}
