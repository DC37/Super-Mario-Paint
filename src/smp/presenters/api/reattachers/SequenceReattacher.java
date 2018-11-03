package smp.presenters.api.reattachers;

import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import smp.components.Values;
import smp.models.staff.StaffNoteLine;
import smp.models.staff.StaffSequence;

/**
 * This will reattach listeners to the StaffSequence's member properties when a
 * new sequence gets set.
 * 
 * @author J
 *
 */
public class SequenceReattacher {

	// ====(not a Model)====
	private ObjectProperty<StaffSequence> theSequence;

	public ChangeListener<Number> tempoListener;

	public ChangeListener<String> soundsetListener;

	public ListChangeListener<StaffNoteLine> theLinesListener;

	public ArrayList<ChangeListener<Boolean>> noteExtensionsListeners = new ArrayList<ChangeListener<Boolean>>(
			Values.NUMINSTRUMENTS);

	public ChangeListener<Object> timeSignatureListener;

	/**
	 * The listener for when a new sequence is set. For instance, a new sequence
	 * is set: the new sequence's tempo should be listened for and the tempo
	 * view updated.
	 */
	public ChangeListener<Object> onReattachListener;
	
	public SequenceReattacher(ObjectProperty<StaffSequence> theSequence) {
		this.theSequence = theSequence;
		setupReattacher();
	}

	private void setupReattacher() {
		this.theSequence.addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				StaffSequence oldSequence = (StaffSequence) oldValue;
				BooleanProperty[] noteExtensions = oldSequence.getNoteExtensions();
				for (int i = 0; i < noteExtensions.length; i++)
					noteExtensions[i].removeListener(noteExtensionsListeners.get(i));
				oldSequence.getSoundset().removeListener(soundsetListener);
				oldSequence.getTempo().removeListener(tempoListener);
				oldSequence.getTheLines().removeListener(theLinesListener);
				oldSequence.getTimeSignature().removeListener(timeSignatureListener);

				StaffSequence newSequence = (StaffSequence) newValue;
				noteExtensions = newSequence.getNoteExtensions();
				for (int i = 0; i < noteExtensions.length; i++)
					if (noteExtensionsListeners.get(i) != null)
						noteExtensions[i].addListener(noteExtensionsListeners.get(i));
				if (soundsetListener != null)
					newSequence.getSoundset().addListener(soundsetListener);
				if (tempoListener != null)
					newSequence.getTempo().addListener(tempoListener);
				if (theLinesListener != null)
					newSequence.getTheLines().addListener(theLinesListener);
				if (timeSignatureListener != null)
					newSequence.getTimeSignature().addListener(timeSignatureListener);
			}
		});
	}

	public void setNewTempoListener(ChangeListener<Number> tempoListener) {
		this.theSequence.get().getTempo().removeListener(this.tempoListener);
		this.tempoListener = tempoListener;
		this.theSequence.get().getTempo().addListener(this.tempoListener);
	}

	public void setNewSoundsetListener(ChangeListener<String> soundsetListener) {
		this.theSequence.get().getSoundset().removeListener(this.soundsetListener);
		this.soundsetListener = soundsetListener;
		this.theSequence.get().getSoundset().addListener(this.soundsetListener);
	}

	public void setNewTheLinesListener(ListChangeListener<StaffNoteLine> theLinesListener) {
		this.theSequence.get().getTheLines().removeListener(this.theLinesListener);
		this.theLinesListener = theLinesListener;
		this.theSequence.get().getTheLines().addListener(this.theLinesListener);
	}

	public void setNewNoteExtensionListener(int index, ChangeListener<Boolean> noteExtensionsListener) {
		this.theSequence.get().getNoteExtensions()[index].removeListener(this.noteExtensionsListeners.get(index));
		this.noteExtensionsListeners.set(index, noteExtensionsListener);
		this.theSequence.get().getNoteExtensions()[index].addListener(this.noteExtensionsListeners.get(index));
	}

	public void setNewTimeSignatureListener(ChangeListener<Object> timeSignatureListener) {
		this.theSequence.get().getTimeSignature().removeListener(this.timeSignatureListener);
		this.timeSignatureListener = timeSignatureListener;
		this.theSequence.get().getTimeSignature().addListener(this.timeSignatureListener);
	}
	
	public void setOnReattachListener(ChangeListener<Object> onReattachListener) {
		this.onReattachListener = onReattachListener;
		this.theSequence.addListener(onReattachListener);
	}
}
