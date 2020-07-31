package smp.presenters.options;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import smp.models.stateMachine.Variables;

public class TempoMultiplierPresenter {

	//TODO: auto-add these model comments
	//====Models====
	private StringProperty optionsTempoMultiplier;
	
	TextField tempoMultiplier;
	
	public TempoMultiplierPresenter(TextField tempoMultiplier) {
		this.tempoMultiplier = tempoMultiplier;
		this.optionsTempoMultiplier = Variables.optionsTempoMultiplier;
		
		this.tempoMultiplier.textProperty().bindBidirectional(optionsTempoMultiplier);
	}
}
