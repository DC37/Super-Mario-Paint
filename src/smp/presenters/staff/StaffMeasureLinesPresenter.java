package smp.presenters.staff;

import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.TestMain;
import smp.components.Values;
import smp.models.stateMachine.StateMachine;

public class StaffMeasureLinesPresenter {

	// TODO: auto-add these model comments
	// ====Models====
	private DoubleProperty measureLineNumber;

	private HBox staffMeasureLines;

	private ArrayList<ImageView> measureLines;

	// TODO: set
	private ImageLoader il = (ImageLoader) TestMain.imgLoader;

	public StaffMeasureLinesPresenter(HBox staffMeasureLines) {
		this.staffMeasureLines = staffMeasureLines;

		this.measureLineNumber = StateMachine.getMeasureLineNum();
		initializeStaffMeasureLines(this.staffMeasureLines);
		setupViewUpdater();
	}
	
    /**
     * These are the lines that divide up the staff.
     *
     * @param staffMLines
     *            The measure lines that divide the staff.
     */
	private void initializeStaffMeasureLines(HBox mLines) {
        int barLength = StateMachine.getTimeSignature().get().top();
        measureLines = new ArrayList<ImageView>();
        for (Node n : mLines.getChildren())
            measureLines.add((ImageView) n);
        for (int i = 0; i < measureLines.size(); i++) {
            if (i % barLength == 0)
                measureLines.get(i).setImage(
                        il.getSpriteFX(ImageIndex.STAFF_MLINE));
            else
                measureLines.get(i).setImage(
                        il.getSpriteFX(ImageIndex.STAFF_LINE));
        }
	}

	private void setupViewUpdater() {
        int barLength = StateMachine.getTimeSignature().get().top();
		this.measureLineNumber.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				int currLine = newValue.intValue();
		        for (int i = 0; i < measureLines.size(); i++) {
		        	ImageView currImage = measureLines.get(i);
		            if ((currLine + i) % barLength == 0) {
		            	currImage.setImage(il.getSpriteFX(ImageIndex.STAFF_MLINE));
		            } else {
		                currImage.setImage(il.getSpriteFX(ImageIndex.STAFF_LINE));
		            }
		        }
			}
		});
	}
}
