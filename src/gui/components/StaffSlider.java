package gui.components;

import gui.StateMachine;
import gui.Values;
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
		model.maxLineSource().bind(maxProperty(),
				val -> Math.max(val - Values.NOTELINES_IN_THE_WINDOW, 0));
		
		model.currentLineSource().bindBidirectional(valueProperty());
		
        disableProperty().bind(StateMachine.getPlaybackActiveProperty());
	}
	
}
