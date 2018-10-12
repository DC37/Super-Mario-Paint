package smp.presenters.api.reload;

import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import smp.components.Values;
import smp.models.staff.StaffNoteLine;
import smp.models.staff.StaffSequence;
import smp.models.stateMachine.Variables;

public class SequenceReloader {

	//TODO: auto-add these model comments
	//====Models====
	private ObjectProperty<StaffSequence> theSequence;
	
	protected ChangeListener<Number> tempoListener;

	protected ChangeListener<String> soundsetListener;

	protected ListChangeListener<StaffNoteLine> theLinesListener;
	
	protected ArrayList<ChangeListener<Boolean>> noteExtensionsListeners = new ArrayList<ChangeListener<Boolean>>(Values.NUMINSTRUMENTS);

	protected ChangeListener<Object> timeSignatureListener;
	
	public SequenceReloader() {
		this.theSequence = Variables.theSequence;
		setupReloader();
	}

	private void setupReloader() {
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

	protected void addNewTempoListener(ChangeListener<Number> tempoListener) {
		this.theSequence.get().getTempo().removeListener(this.tempoListener);
		this.tempoListener = tempoListener;
		this.theSequence.get().getTempo().addListener(this.tempoListener);
	}
	
	protected void addNewSoundsetListener(ChangeListener<String> soundsetListener) {
		this.theSequence.get().getSoundset().removeListener(this.soundsetListener);
		this.soundsetListener = soundsetListener;
		this.theSequence.get().getSoundset().addListener(this.soundsetListener);
	}
	
	protected void addNewTheLinesListener(ListChangeListener<StaffNoteLine> theLinesListener) {
		this.theSequence.get().getTheLines().removeListener(this.theLinesListener);
		this.theLinesListener = theLinesListener;
		this.theSequence.get().getTheLines().addListener(this.theLinesListener);
	}
	
	protected void addNewNoteExtensionListener(int index, ChangeListener<Boolean> noteExtensionsListener) {
		this.theSequence.get().getNoteExtensions()[index].removeListener(this.noteExtensionsListeners.get(index));
		this.noteExtensionsListeners.set(index, noteExtensionsListener);
		this.theSequence.get().getNoteExtensions()[index].addListener(this.noteExtensionsListeners.get(index));
	}
	
	protected void addNewTimeSignatureListener(ChangeListener<Object> timeSignatureListener) {
		this.theSequence.get().getTimeSignature().removeListener(this.timeSignatureListener);
		this.timeSignatureListener = timeSignatureListener;
		this.theSequence.get().getTimeSignature().addListener(this.timeSignatureListener);
	}
}
