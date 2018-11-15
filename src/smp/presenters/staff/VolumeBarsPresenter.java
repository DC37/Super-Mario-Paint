package smp.presenters.staff;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import smp.components.Values;
import smp.models.staff.StaffNoteLine;
import smp.models.stateMachine.Variables;
import smp.models.stateMachine.Variables.WindowLines;
import smp.presenters.api.reattachers.NoteLineReattacher;
import smp.presenters.staff.StaffVolumeEventHandler;

public class VolumeBarsPresenter {

	// TODO: auto-add these model comments
	// ====Models====
	private WindowLines windowLines;

	private ArrayList<NoteLineReattacher> noteLineReattachers;

	private HBox volumeBars;

	/**
	 * This is the list of volume bar handlers on the staff.
	 * 
	 * @see smp.components.staff.NoteMatrix.volumeBarHandlers
	 */
	private ArrayList<StaffVolumeEventHandler> volumeBarHandlers;

	/**
	 * This is the list of volume bars on the staff.
	 * 
	 * @see smp.components.staff.NoteMatrix.volumeBars
	 */
	private ArrayList<StackPane> volumeBarsSP;

	public VolumeBarsPresenter(HBox volumeBars) {
		this.volumeBars = volumeBars;
        volumeBarHandlers = new ArrayList<StaffVolumeEventHandler>();

		this.windowLines = Variables.windowLines;
		this.noteLineReattachers = new ArrayList<NoteLineReattacher>();
		initializeVolumeBars(this.volumeBars);
		initializeVolumeBarLinks();
		setupViewUpdater();
	}

	/**
	 * Initializes the volume bars in the program.
	 *
	 * @param volumeBars
	 *            This is the HBox that holds all of the volume bar
	 *            <code>StackPane</code> objects.
	 */
	private void initializeVolumeBars(HBox volumeBars) {
		ArrayList<StackPane> vol = new ArrayList<StackPane>();
		for (Node v : volumeBars.getChildren()) {
			StackPane volBar = (StackPane) v;
			vol.add(volBar);
			StaffVolumeEventHandler sveh = new StaffVolumeEventHandler(volBar);
			volBar.addEventHandler(Event.ANY, sveh);
			volumeBarHandlers.add(sveh);
		}
		volumeBarsSP = vol;
	}
	
    /**
     * Sets up the links between the volume bars display and StaffNoteLines.
     */
    private void initializeVolumeBarLinks() {
        for (int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++) {
            StaffVolumeEventHandler sveh = volumeBarHandlers.get(i);
            StaffNoteLine stl = windowLines.get(i).get();
            sveh.setStaffNoteLine(stl);
        }
    }

	private void setupViewUpdater() {
		for (int i = 0; i < windowLines.size(); i++) {
			final StaffVolumeEventHandler sveh = volumeBarHandlers.get(i);
			NoteLineReattacher nlr = new NoteLineReattacher(windowLines.get(i));
			nlr.setNewVolumeListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					// will be handled by sveh (TODO: make sveh only modify models)
				}
			});
			nlr.setOnReattachListener(new ChangeListener<Object>() {

				@Override
				public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
					sveh.setStaffNoteLine((StaffNoteLine) newValue);
					sveh.updateVolume();
				}
			});
			noteLineReattachers.add(nlr);
		}
	}
}
