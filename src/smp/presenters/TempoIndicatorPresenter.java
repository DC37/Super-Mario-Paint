package smp.presenters;

import javafx.beans.property.DoubleProperty;
import javafx.scene.text.Text;
import smp.models.stateMachine.StateMachine;

public class TempoIndicatorPresenter {

	private DoubleProperty tempo;
	
	private Text tempoIndicator;

	public TempoIndicatorPresenter(Text tempoIndicator) {
		this.tempoIndicator = tempoIndicator;
		
		this.tempo = StateMachine.getTempo();
		setupViewUpdater();
	}

	private void setupViewUpdater() {
		tempoIndicator.textProperty().bind(tempo.asString());
	}
}
