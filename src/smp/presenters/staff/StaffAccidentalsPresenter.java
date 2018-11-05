package smp.presenters.staff;

import java.util.ArrayList;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.Values;
import smp.models.staff.StaffNote;
import smp.models.staff.StaffNoteLine;
import smp.models.stateMachine.Variables;
import smp.models.stateMachine.Variables.WindowLines;
import smp.presenters.api.reattachers.NoteLineReattacher;

public class StaffAccidentalsPresenter {

	// TODO: auto-add these model comments
	// ====Models====
	private WindowLines windowLines;

	private ArrayList<NoteLineReattacher> noteLineReattachers;

	/** This is an HBox of VBoxes. Each VBox is a vertical list of StackPanes. */
	private HBox staffAccidentals;

	/** This is the matrix of flats / sharps / etc. */
	private ArrayList<ArrayList<StackPane>> accMatrix;
	
    /** Pointer to the image loader object. */
    private transient ImageLoader il;

	public StaffAccidentalsPresenter(HBox staffAccidentals) {
		this.staffAccidentals = staffAccidentals;
		initializeStaffInstruments(this.staffAccidentals);

		this.windowLines = Variables.windowLines;
		this.noteLineReattachers = new ArrayList<NoteLineReattacher>();
		setupViewUpdater();
	}

	/**
	 * Sets up the various note lines of the staff. These are the notes that can
	 * appear on the staff. This method also sets up sharps, flats, etc.
	 *
	 * @param accidentals
	 *            The HBox that holds the framework for the sharps / flats.
	 */
	private void initializeStaffInstruments(HBox accidentals) {
		ArrayList<VBox> accidentalLines = new ArrayList<VBox>();
		for (Node n : accidentals.getChildren())
			accidentalLines.add((VBox) n);

		for (int line = 0; line < accidentalLines.size(); line++) {
			VBox accVerticalHolder = accidentalLines.get(line);

			ObservableList<Node> lineOfAcc = accVerticalHolder.getChildren();

			ArrayList<StackPane> accs = new ArrayList<StackPane>();

			for (int pos = 1; pos <= Values.NOTES_IN_A_LINE; pos++) {
				StackPane acc = (StackPane) lineOfAcc.get(pos - 1);
				accs.add(acc);
			}

			accMatrix.add(accs);
		}
	}

	private void setupViewUpdater() {
		for (int i = 0; i < windowLines.size(); i++) {
			final ArrayList<StackPane> accColumn = accMatrix.get(i);
			final ObjectProperty<StaffNoteLine> windowLine = windowLines.get(i);
			NoteLineReattacher nlr = new NoteLineReattacher(windowLine);
			nlr.setNewNotesListener(new ListChangeListener<StaffNote>() {

				@Override
				public void onChanged(Change<? extends StaffNote> c) {
					//clearnotedisplay
					for (StackPane sp : accColumn)
						sp.getChildren().clear();
					//populatenotedisplay
					for (StaffNote s : windowLine.get().getNotes()) {
						int pos = s.getPosition();
						int acc = s.getAccidental();
						ImageView accIV = new ImageView(il.getSpriteFX(switchAcc(acc)));
						accColumn.get(pos).getChildren().add(accIV);
					}
				}
			});
			nlr.setOnReattachListener(new ChangeListener<Object>() {

				@Override
				public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
					//clearnotedisplay
					for (StackPane sp : accColumn)
						sp.getChildren().clear();
					//populatenotedisplay
					StaffNoteLine newLine = (StaffNoteLine) newValue;
					for (StaffNote s : newLine.getNotes()) {
						int pos = s.getPosition();
						int acc = s.getAccidental();
						ImageView accIV = new ImageView(il.getSpriteFX(switchAcc(acc)));
						accColumn.get(pos).getChildren().add(accIV);
					}
				}
			});
			noteLineReattachers.add(nlr);
		}
	}
	
    /**
     * @param acc
     *            The offset that we are deciding upon.
     * @return An <code>ImageIndex</code> based on the amount of sharp or flat
     *         we want to implement.
     */
    public static ImageIndex switchAcc(int acc) {
        switch (acc) {
        case 2:
            return ImageIndex.DOUBLESHARP;
        case 1:
            return ImageIndex.SHARP;
        case 0:
            return ImageIndex.BLANK;
        case -1:
            return ImageIndex.FLAT;
        case -2:
            return ImageIndex.DOUBLEFLAT;
        default:
            return ImageIndex.BLANK;
        }
    }
}
