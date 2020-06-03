package smp.presenters;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.text.Text;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.StateMachine;

public class ModeTextPresenter {

	//TODO: auto-add these model comments
	//====Models====
    private ObjectProperty<ProgramState> programState;
    
	Text modeText;
	
	public ModeTextPresenter(Text modeText) {
		this.modeText = modeText;
        this.programState = StateMachine.getState();
        setupViewUpdater();
	}

	private void setupViewUpdater() {
		this.modeText.setText("Song");
		this.programState.addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				if (newValue.equals(ProgramState.EDITING))
					modeText.setText("Song");
				else if (newValue.equals(ProgramState.ARR_EDITING))
					modeText.setText("Arranger");
			}
		});
	}
}
