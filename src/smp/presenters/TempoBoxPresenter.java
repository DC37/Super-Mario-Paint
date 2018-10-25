package smp.presenters;

import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import smp.fx.Dialog;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.StateMachine;

public class TempoBoxPresenter {

	//TODO: auto-add these model comments
	//====Models====
	private DoubleProperty tempo;
	
	private StackPane tempoBox;

	public TempoBoxPresenter(StackPane tempoBox) {
		this.tempoBox = tempoBox;
		
		this.tempo = StateMachine.getTempo();
		setupViewUpdater();
	}

	private void setupViewUpdater() {
		tempoBox.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					ProgramState curr = StateMachine.getState().get();
					if (curr == ProgramState.EDITING) {
						String tempoString = Dialog.showTextDialog("Tempo");
						tempo.set(Double.parseDouble(tempoString));
					}
				} catch (NumberFormatException e) {
					// Do nothing.
				}
				event.consume();
			}
		});
	}
}
