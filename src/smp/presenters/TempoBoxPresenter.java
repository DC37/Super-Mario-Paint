package smp.presenters;

import javafx.beans.property.ObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import smp.fx.Dialog;
import smp.models.staff.StaffSequence;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.StateMachine;
import smp.models.stateMachine.Variables;

public class TempoBoxPresenter {

	//TODO: auto-add these model comments
	//====Models====
	private ObjectProperty<StaffSequence> theSequence;
	private ObjectProperty<ProgramState> programState;
	
	private StackPane tempoBox;

	public TempoBoxPresenter(StackPane tempoBox) {
		this.tempoBox = tempoBox;
		
		this.theSequence = Variables.theSequence;
		this.programState = StateMachine.getState();
		setupViewUpdater();
	}

	private void setupViewUpdater() {
		tempoBox.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					ProgramState curr = programState.get();
					if (curr == ProgramState.EDITING) {
						String tempoString = Dialog.showTextDialog("Tempo");
						theSequence.get().getTempo().set(Double.parseDouble(tempoString));
					}
				} catch (NumberFormatException e) {
					// Do nothing.
				}
				event.consume();
			}
		});
	}
}
