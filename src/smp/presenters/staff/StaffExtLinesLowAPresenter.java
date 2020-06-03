package smp.presenters.staff;

import java.util.ArrayList;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import smp.components.Values;
import smp.models.staff.StaffNote;
import smp.models.staff.StaffNoteLine;
import smp.models.stateMachine.Variables;
import smp.models.stateMachine.Variables.WindowLines;
import smp.presenters.api.reattachers.NoteLineReattacher;

public class StaffExtLinesLowAPresenter {

	// TODO: auto-add these model comments
	// ====Models====
	private WindowLines windowLines;

	private ArrayList<NoteLineReattacher> noteLineReattachers;

	private HBox staffExtLinesLowA;

	private ArrayList<Node> lowA;

	public StaffExtLinesLowAPresenter(HBox staffExtLinesLowA) {
		this.staffExtLinesLowA = staffExtLinesLowA;

		this.windowLines = Variables.windowLines;
		this.noteLineReattachers = new ArrayList<NoteLineReattacher>();
		initializeStaffLedgerLines();
		setupViewUpdater();
	}

	private void setupViewUpdater() {
		for (int i = 0; i < windowLines.size(); i++) {
			final int index = i;
			final ObjectProperty<StaffNoteLine> windowLine = windowLines.get(i);
			NoteLineReattacher nlr = new NoteLineReattacher(windowLines.get(i));
			nlr.setNewNotesListener(new ListChangeListener<StaffNote>() {

				@Override
				public void onChanged(Change<? extends StaffNote> c) {
					populateStaffLedgerLines(windowLine.get(), index);
				}
			});
			nlr.setOnReattachListener(new ChangeListener<Object>() {

				@Override
				public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
					populateStaffLedgerLines((StaffNoteLine) newValue, index);
				}
			});
			noteLineReattachers.add(nlr);
		}
	}

	/**
	 * Re-draws the staff ledger lines based on the notes present in a
	 * StaffNoteLine at a certain index. Position 14 & 16 are the positions of
	 * the high A and high C lines, and positions 0 and 2 are the positions of
	 * the low A and low C lines.
	 *
	 * @param stl
	 *            The <code>StaffNoteLine</code> that we want to check.
	 * @param index
	 *            The index that we are updating.
	 *
	 */
	private void populateStaffLedgerLines(StaffNoteLine stl, int index) {
		int low = Values.NOTES_IN_A_LINE;
		for (StaffNote n : stl.getNotes()) {
			int nt = n.getPosition();
			if (nt <= low)
				low = nt;
		}
		lowA.get(index).setVisible(low <= Values.lowA);
	}

	/**
	 * Sets up the staff expansion lines, which are to hold notes that are
	 * higher than or lower than the regular lines of the staff.
	 *
	 * @param staffLLines
	 *            An array of ledger lines. This method expects that there will
	 *            be four of these, two of which indicate the lines above the
	 *            staff and the other of which indicating the lines below the
	 *            staff.
	 */
	private void initializeStaffLedgerLines() {
		lowA = new ArrayList<Node>();
		lowA.addAll(this.staffExtLinesLowA.getChildren());
		hideAllLedgerLines();
	}

	/**
	 * Hides all of the ledger lines.
	 */
	public void hideAllLedgerLines() {

		for (Node nd : lowA)
			nd.setVisible(false);
	}
}
