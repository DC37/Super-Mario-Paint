package smp.presenters.api.reattachers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import smp.models.staff.StaffNote;
import smp.models.staff.StaffNoteLine;

/**
 * This will reattach listeners to the StaffNoteLine's member properties when a
 * new noteline gets set.
 * 
 * @author J
 *
 */
public class NoteLineReattacher {

	// ====(not a Model)====
	private ObjectProperty<StaffNoteLine> noteLine;

	public ChangeListener<Number> volumeListener;

	public ListChangeListener<StaffNote> notesListener;

	public ChangeListener<Number> notesSizeListener;
	/**
	 * The listener for when a new noteLine is set. For instance, a new note
	 * line is set: the new note line's volume should be listened for and the
	 * volume view updated.
	 */
	public ChangeListener<Object> onReattachListener;
	
	public NoteLineReattacher(ObjectProperty<StaffNoteLine> noteLine) {
		this.noteLine = noteLine;
		setupReattacher();
	}

	private void setupReattacher() {
		this.noteLine.addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				StaffNoteLine oldNoteLine = (StaffNoteLine) oldValue;
				if (volumeListener != null)
					oldNoteLine.getVolume().removeListener(volumeListener);
				if (notesListener != null)
					oldNoteLine.getNotes().removeListener(notesListener);

				StaffNoteLine newNoteLine = (StaffNoteLine) newValue;
				if (volumeListener != null)
					newNoteLine.getVolume().addListener(volumeListener);
				if (notesListener != null)
					newNoteLine.getNotes().addListener(notesListener);
			}
		});
	}

	public void setNewVolumeListener(ChangeListener<Number> volumeListener) {
		if (this.volumeListener != null)
			this.noteLine.get().getVolume().removeListener(this.volumeListener);
		this.volumeListener = volumeListener;
		this.noteLine.get().getVolume().addListener(this.volumeListener);
	}

	public void setNewNotesListener(ListChangeListener<StaffNote> notesListener) {
		if (this.notesListener != null)
			this.noteLine.get().getNotes().removeListener(this.notesListener);
		this.notesListener = notesListener;
		this.noteLine.get().getNotes().addListener(this.notesListener);
	}

	public void setNewNotesSizeListener(ChangeListener<Number> notesSizeListener) {
		if (this.notesSizeListener != null)
			this.noteLine.get().getNotesSize().removeListener(this.notesSizeListener);
		this.notesSizeListener = notesSizeListener;
		this.noteLine.get().getNotesSize().addListener(this.notesSizeListener);
	}

	public void setOnReattachListener(ChangeListener<Object> onReattachListener) {
		this.onReattachListener = onReattachListener;
		this.noteLine.addListener(onReattachListener);
	}
}
