package smp.presenters.staff;

import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
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

public class StaffAccidentalsPresenter {

	// TODO: auto-add these model comments
	// ====Models====
	private WindowLines windowLines;
	private IntegerProperty selectedAccidental;
	private BooleanProperty accSilhouetteVisible;
	private IntegerProperty selectedLine;
	private IntegerProperty selectedPosition;

	private ArrayList<NoteLineReattacher> noteLineReattachers;

	/** This is an HBox of VBoxes. Each VBox is a vertical list of StackPanes. */
	private HBox staffAccidentals;

	/** This is the matrix of flats / sharps / etc. */
	private ArrayList<ArrayList<StackPane>> accMatrix;
	
    /** Pointer to the image loader object. */
    private transient ImageLoader il = (ImageLoader) TestMain.imgLoader;
    
    private ImageView accSilhouette = new ImageView();

	public StaffAccidentalsPresenter(HBox staffAccidentals) {
		this.staffAccidentals = staffAccidentals;
        accMatrix = new ArrayList<ArrayList<StackPane>>();
        accSilhouette.setImage(il.getSpriteFX(ImageIndex.BLANK));

		this.windowLines = Variables.windowLines;
		this.selectedAccidental = Variables.selectedAccidental;
		this.accSilhouetteVisible = Variables.accSilhouetteVisible;
		this.selectedLine  = Variables.selectedLine;
		this.selectedPosition = Variables.selectedPosition;
		this.noteLineReattachers = new ArrayList<NoteLineReattacher>();
		initializeStaffInstruments(this.staffAccidentals);
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

		//INSTRUMENTEVENTHANDLER
		this.selectedAccidental.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				accSilhouette.setImage(il.getSpriteFX(switchAcc(newValue.intValue()).silhouette()));
			}
		});
		this.accSilhouette.visibleProperty().bindBidirectional(accSilhouetteVisible);
		this.selectedLine.addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				moveAcc();
			}
		});
		this.selectedPosition.addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				moveAcc();
			}
		});
	}
	
	private void moveAcc() {
		if(selectedLine.get() < 0 || selectedPosition.get() < 0)
			return;
		StackPane stp = getNote(selectedLine.get(), selectedPosition.get());
		stp.getChildren().add(accSilhouette);
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
        ArrayList<StackPane> ac = accMatrix.get(index);
        for (int i = 0; i < Values.NOTES_IN_A_LINE; i++) {
            ObservableList<Node> acList = ac.get(i).getChildren();
            acList.clear();
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
			StackPane accSP = getNote(index, s.getPosition());
			ImageView accidental = new ImageView();
			accidental.setImage(il.getSpriteFX(switchAcc(s.getAccidental())));
			accSP.getChildren().add(accidental);
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
        return accMatrix.get(x).get(Values.NOTES_IN_A_LINE - y - 1);
    }
}
