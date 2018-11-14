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
import smp.TestMain;
import smp.components.Values;
import smp.models.staff.StaffNote;
import smp.models.staff.StaffNoteLine;
import smp.models.stateMachine.Variables;
import smp.models.stateMachine.Variables.WindowLines;
import smp.presenters.api.reattachers.NoteLineReattacher;

public class StaffInstrumentsPresenter {

	// TODO: auto-add these model comments
	// ====Models====
	private WindowLines windowLines;

	private ArrayList<NoteLineReattacher> noteLineReattachers;

	/** This is an HBox of VBoxes. Each VBox is a vertical list of StackPanes. */
	private HBox staffInstruments;

    /**
     * The list of lists that holds the different <code>StackPane</code>
     * objects.
     */
	private ArrayList<ArrayList<StackPane>> matrix;
	
    /** Pointer to the image loader object. */
    private transient ImageLoader il = (ImageLoader) TestMain.imgLoader;

	public StaffInstrumentsPresenter(HBox staffInstruments) {
		this.staffInstruments = staffInstruments;
        matrix = new ArrayList<ArrayList<StackPane>>();

		this.windowLines = Variables.windowLines;
		this.noteLineReattachers = new ArrayList<NoteLineReattacher>();
		initializeStaffInstruments(this.staffInstruments);
		setupViewUpdater();
	}

	/**
	 * Sets up the various note lines of the staff. These are the notes that can
	 * appear on the staff. This method also sets up sharps, flats, etc.
	 *
	 * @param accidentals
	 *            The HBox that holds the framework for the sharps / flats.
	 */
	private void initializeStaffInstruments(HBox instruments) {
        ArrayList<VBox> noteLines = new ArrayList<VBox>();
        for (Node n : instruments.getChildren())
            noteLines.add((VBox) n);

		for (int line = 0; line < noteLines.size(); line++) {
			VBox verticalHolder = noteLines.get(line);

			ObservableList<Node> lineOfNotes = verticalHolder.getChildren();

			ArrayList<StackPane> notes = new ArrayList<StackPane>();

			for (int pos = 1; pos <= Values.NOTES_IN_A_LINE; pos++) {
				StackPane note = (StackPane) lineOfNotes.get(pos - 1);
				notes.add(note);
			}

			matrix.add(notes);
		}
	}

	private void setupViewUpdater() {
		for (int i = 0; i < windowLines.size(); i++) {
			final int index = i;
			final ObjectProperty<StaffNoteLine> windowLine = windowLines.get(i);
			NoteLineReattacher nlr = new NoteLineReattacher(windowLine);
			nlr.setNewNotesListener(new ListChangeListener<StaffNote>() {

				@Override
				public void onChanged(Change<? extends StaffNote> c) {
					clearNoteDisplay(index);
					populateNoteDisplay(windowLine.get(), index);
				}
			});
			nlr.setOnReattachListener(new ChangeListener<Object>() {

				@Override
				public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
					clearNoteDisplay(index);
					populateNoteDisplay((StaffNoteLine) newValue, index);
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
    
    /**
     * Clears the note display on the staff.
     *
     * @param index
     *            The index that we are clearing.
     */
    private synchronized void clearNoteDisplay(int index) {
        ArrayList<StackPane> nt = matrix.get(index);
        for (int i = 0; i < Values.NOTES_IN_A_LINE; i++) {
            ObservableList<Node> ntList = nt.get(i).getChildren();
            ntList.clear();
        }
    }

    /**
     * Repopulates the note display on the staff.
     *
     * @param stl
     *            The StaffNoteLine that we are interested in.
     * @param index
     *            The index to repopulate.
     */
	private void populateNoteDisplay(StaffNoteLine stl, int index) {
		ObservableList<StaffNote> st = stl.getNotes();
		for (StaffNote s : st) {
			StackPane noteSP = getNote(index, s.getPosition());
			ImageView sIV = new ImageView();
			noteSP.getChildren().add(sIV);

            if (s.muteNoteVal() == 0) {
                sIV.setImage(il.getSpriteFX(s.getInstrument().imageIndex()));
            } else if (s.muteNoteVal() == 1) {
                sIV.setImage(il.getSpriteFX(s.getInstrument().imageIndex().alt()));
            } else {
                sIV.setImage(il.getSpriteFX(s.getInstrument().imageIndex()
                        .silhouette()));
            }

            sIV.setVisible(true);
		}
    }    
	
	/**
     * Gets you an object based on the coordinate that you give this method.
     * This method should help a lot when working on those portions of code that
     * ask the entire staff to update its images. Bypassing the individual
     * StackPane object links should be a lot easier with this here.
     *
     * @param x
     *            The note line number.
     * @param y
     *            The note number.
     * @return Index 0 is the <code>StackPane</code> of the note that is located
     *         at the location. Index 1 is the <code>StackPane</code> of the
     *         flat / sharp / etc box that it is associated with.
     */
    public StackPane getNote(int x, int y) {
        return matrix.get(x).get(Values.NOTES_IN_A_LINE - y - 1);
    }
}
