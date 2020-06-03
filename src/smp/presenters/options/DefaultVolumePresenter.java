package smp.presenters.options;

import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Slider;
import smp.models.stateMachine.Variables;

public class DefaultVolumePresenter {

	//TODO: auto-add these model comments
	//====Models====
	private IntegerProperty optionsDefaultVolume;
	
	private Slider defaultVolume;
	
	public DefaultVolumePresenter(Slider defaultVolume) {
		this.defaultVolume = defaultVolume;
		this.optionsDefaultVolume = Variables.optionsDefaultVolume;
		
		this.defaultVolume.valueProperty().bindBidirectional(optionsDefaultVolume);
		this.defaultVolume.autosize();
	}
}
