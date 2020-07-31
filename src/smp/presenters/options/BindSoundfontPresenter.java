package smp.presenters.options;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import smp.models.stateMachine.Variables;

public class BindSoundfontPresenter {

	//TODO: auto-add these model comments
	//====Models====
	private BooleanProperty optionsBindSoundfont;
	private StringProperty optionsBindedSoundfont;
	
	private CheckBox bindSoundfont;
	
	public BindSoundfontPresenter(CheckBox bindSoundfont) {
		this.bindSoundfont = bindSoundfont;
		this.optionsBindSoundfont = Variables.optionsBindSoundfont;
		this.optionsBindedSoundfont = Variables.optionsBindedSoundfont;
		
		this.bindSoundfont.selectedProperty().bindBidirectional(optionsBindSoundfont);
		if (optionsBindedSoundfont.get().equals(Variables.optionsCurrentSoundfont.get()))
			this.bindSoundfont.setSelected(true);
		this.bindSoundfont.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean checked) {
				if(checked) {
					optionsBindedSoundfont.set(Variables.optionsCurrentSoundfont.get());
				} else {
					optionsBindedSoundfont.set("");
				}
			}
		});
	}
}
