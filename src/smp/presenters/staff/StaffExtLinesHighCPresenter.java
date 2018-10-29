package smp.presenters.staff;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.HBox;
import smp.models.stateMachine.StateMachine;

public class StaffExtLinesHighCPresenter {

	// TODO: auto-add these model comments
	// ====Models====
	private DoubleProperty measureLineNumber;

	private HBox staffExtLinesHighC;

	public StaffExtLinesHighCPresenter(HBox staffExtLinesHighC) {
		this.staffExtLinesHighC = staffExtLinesHighC;

		this.measureLineNumber = StateMachine.getMeasureLineNum();
		setupViewUpdater();
	}

	private void setupViewUpdater() {
		this.measureLineNumber.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO:
			}
		});
	}
}
