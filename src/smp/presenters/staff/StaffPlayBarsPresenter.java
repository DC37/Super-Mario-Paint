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
import smp.models.stateMachine.StateMachine;

public class StaffPlayBarsPresenter {

	// TODO: auto-add these model comments
	// ====Models====
	private DoubleProperty measureLineNumber;

	private HBox staffPlayBars;

	private ArrayList<ImageView> staffPlayBarsIV;

	// TODO: set
	private ImageLoader il;

	public StaffPlayBarsPresenter(HBox staffPlayBars) {
		this.staffPlayBars = staffPlayBars;
		initializeStaffPlayBars(this.staffPlayBars);

		this.measureLineNumber = StateMachine.getMeasureLineNum();
		setupViewUpdater();
	}

	/**
	 * Sets up the note highlighting functionality.
	 *
	 * @param staffPlayBars
	 *            The bars that move to highlight different notes.
	 */
	private void initializeStaffPlayBars(HBox playBars) {
		staffPlayBarsIV = new ArrayList<ImageView>();
		for (Node n : playBars.getChildren()) {
			ImageView i = (ImageView) n;
			i.setImage(il.getSpriteFX(ImageIndex.PLAY_BAR1));
			i.setVisible(false);
			staffPlayBarsIV.add(i);
		}
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
