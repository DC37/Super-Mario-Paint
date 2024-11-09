package smp.presenters.staff;

import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import smp.components.Values;
import smp.models.stateMachine.StateMachine;

public class StaffMeasureNumbersPresenter {

	// TODO: auto-add these model comments
	// ====Models====
	private DoubleProperty measureLineNumber;

	private HBox staffMeasureNumbers;

	ArrayList<Text> measureNums;

	public StaffMeasureNumbersPresenter(HBox staffMeasureNumbers) {
		this.staffMeasureNumbers = staffMeasureNumbers;

		this.measureLineNumber = StateMachine.getMeasureLineNum();
		initializeStaffMeasureNums(this.staffMeasureNumbers);
		setupViewUpdater();
	}

	/**
	 * These are the numbers above each successive measure.
	 */
	private void initializeStaffMeasureNums(HBox mNums) {
        int barLength = StateMachine.getTimeSignature().get().top();
		ArrayList<HBox> measureNumBoxes = new ArrayList<HBox>();
		measureNums = new ArrayList<Text>();
        for (Node num : mNums.getChildren())
            measureNumBoxes.add((HBox) num);
        int counter = 1;
        for (int i = 0; i < measureNumBoxes.size(); i++) {
            HBox theBox = measureNumBoxes.get(i);
            Text t = new Text();
            theBox.getChildren().add(t);
            measureNums.add(t);
            if (i % barLength == 0) {
                t.setText(String.valueOf(counter));
                counter++;
            } else
                continue;
        }
    }
    

    /**
	 * @see smp.components.staff.StaffImages.updateStaffMeasureLines(int)
	 */
	public void setupViewUpdater() {
        int barLength = StateMachine.getTimeSignature().get().top();
		this.measureLineNumber.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				int currLine = newValue.intValue();
		        int counter = 0;
		        for (int i = 0; i < measureNums.size(); i++) {
		            Text currText = measureNums.get(i);
		            if ((currLine + i) % barLength == 0) {
		                currText.setText(String.valueOf((int) (Math.ceil(currLine
		                        / (double) barLength) + 1 + counter)));
		                counter++;
		            } else {
		                currText.setText("");
		            }
		        }
			}
		});
    }
}
