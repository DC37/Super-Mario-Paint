package gui.components;

import gui.StateMachine;
import gui.Values;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Slider;
import model.AppModel;

public class StaffSlider extends Slider {

	private final AppModel model = AppModel.getInstance();
	
	public StaffSlider() {
		super();
	}
	
	public StaffSlider(double min, double max, double value) {
		super(min, max, value);
	}
	
	public void prepare() {
		maxProperty().bind(Bindings.createIntegerBinding(
                () -> Math.max(StateMachine.getMaxLine() - Values.NOTELINES_IN_THE_WINDOW, 0),
                StateMachine.getMaxLineProperty()));
		
		model.currentLineSource().bindBidirectional(valueProperty());
		
        disableProperty().bind(StateMachine.getPlaybackActiveProperty());
	}
	
}
