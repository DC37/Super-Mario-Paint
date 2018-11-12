package smp.presenters;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.text.Text;
import smp.models.staff.StaffSequence;
import smp.models.stateMachine.Variables;
import smp.presenters.api.reattachers.SequenceReattacher;

public class TempoIndicatorPresenter {

	//TODO: auto-add these model comments
	//====Models====
	private ObjectProperty<StaffSequence> theSequence;
	
	private Text tempoIndicator;

	private SequenceReattacher sequenceReattacher;

	public TempoIndicatorPresenter(Text tempoIndicator) {
		this.tempoIndicator = tempoIndicator;

		this.theSequence = Variables.theSequence;
		this.sequenceReattacher = new SequenceReattacher(this.theSequence);
		setupViewUpdater();
	}

	private void setupViewUpdater() {
		tempoIndicator.textProperty().bind(theSequence.get().getTempo().asString());
		sequenceReattacher.setOnReattachListener(new ChangeListener<StaffSequence>() {

			@Override
			public void changed(ObservableValue<? extends StaffSequence> observable, StaffSequence oldValue,
					StaffSequence newValue) {
				StaffSequence newSequence = (StaffSequence) newValue;
				tempoIndicator.textProperty().unbind();
				tempoIndicator.textProperty().bind(newSequence.getTempo().asString());
			}
		});
	}
}
