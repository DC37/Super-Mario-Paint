package smp.presenters;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.ImageView;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.StateMachine;

public class ClipboardLabelPresenter {

	//TODO: auto-add these model comments
	//====Models====
	private ObjectProperty<ProgramState> programState;
	
	private ImageView clipboardLabel;

	public ClipboardLabelPresenter(ImageView clipboardLabel) {
		this.clipboardLabel = clipboardLabel;
		this.programState = StateMachine.getState();
		setupViewUpdater();
	}

    private void setupViewUpdater() {
    	this.programState.addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				if (newValue.equals(ProgramState.EDITING))
					clipboardLabel.setVisible(true);
				else if (newValue.equals(ProgramState.ARR_EDITING))
					clipboardLabel.setVisible(false);
			}
		});
	}
}
