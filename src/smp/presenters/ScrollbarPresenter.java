package smp.presenters;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import smp.components.Values;
import smp.models.staff.StaffSequence;
import smp.models.stateMachine.StateMachine;
import smp.models.stateMachine.Variables;
import smp.presenters.api.reattachers.SequenceReattacher;

public class ScrollbarPresenter {

	//TODO: auto-add these model comments
	//====Models====
	private DoubleProperty measureLineNum;
	private ObjectProperty<StaffSequence> theSequence;
	
	private Slider scrollbar;
	
	private SequenceReattacher sequenceReattacher;

	public ScrollbarPresenter(Slider scrollbar) {
		this.scrollbar = scrollbar;
		
		this.measureLineNum = StateMachine.getMeasureLineNum();
		this.theSequence = Variables.theSequence;
		this.sequenceReattacher = new SequenceReattacher(this.theSequence);
		setupViewUpdater();
	}
	
	private void setupViewUpdater() {
		scrollbar.valueProperty().bindBidirectional(this.measureLineNum);
		this.measureLineNum.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double newVal = newValue.doubleValue();
				// we have these if statements because measureLineNum can go out of bounds of bidirectional binding 
				if (newVal > scrollbar.getMax())
					measureLineNum.set(scrollbar.getMax());
				else if (newVal < 0)
					measureLineNum.set(0);
			}
		});

		this.sequenceReattacher.setNewTheLinesSizeListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldVal, Number newVal) {
				// Make sure the newVal is not less than the default size.
				int newMax = Math.max(Values.DEFAULT_LINES_PER_SONG, newVal.intValue());
				scrollbar.setMax(newMax - Values.NOTELINES_IN_THE_WINDOW);
			}
		});
		this.sequenceReattacher.setOnReattachListener(new ChangeListener<StaffSequence>() {

			@Override
			public void changed(ObservableValue<? extends StaffSequence> observable, StaffSequence oldValue,
					StaffSequence newValue) {
				measureLineNum.set(0);
			}
		});
	}
}
