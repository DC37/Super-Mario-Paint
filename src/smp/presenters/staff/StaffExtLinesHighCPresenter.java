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

public class StaffExtLinesHighCPresenter {

	// TODO: auto-add these model comments
	// ====Models====
	private WindowLines windowLines;

	private ArrayList<NoteLineReattacher> noteLineReattachers;

	private HBox staffExtLinesHighC;

	private ArrayList<Node> highC;

	public StaffExtLinesHighCPresenter(HBox staffExtLinesHighC) {
		this.staffExtLinesHighC = staffExtLinesHighC;
		highC = new ArrayList<Node>();
		highC.addAll(this.staffExtLinesHighC.getChildren());

		this.windowLines = Variables.windowLines;
		this.noteLineReattachers = new ArrayList<NoteLineReattacher>();
		setupViewUpdater();
	}

	private void setupViewUpdater() {
		for (int i = 0; i < windowLines.size(); i++) {
			final int index=  i;
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
		int high = 0;
		for (StaffNote n : stl.getNotes()) {
			int nt = n.getPosition();
			if (nt >= high)
				high = nt;
		}
		highC.get(index).setVisible(high >= Values.highC);
	}
}
