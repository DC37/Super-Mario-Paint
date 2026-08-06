package me.dc37.smp.models;

import backend.songs.TimeSignature;
import gui.SMPInstrument;
import gui.SMPMode;
import gui.Values;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SMPAppModel {

	private static SMPAppModel instance; 
	
	public static SMPAppModel getInstance() {
		if (instance == null) {
			instance = new SMPAppModel();
		}
		
		return instance;
	}
	
	private final BooleanProperty ctrlPressed = new SimpleBooleanProperty(false);
    private final BooleanProperty shiftPressed = new SimpleBooleanProperty(false);
    
    private final ObjectProperty<SMPInstrument> selectedInstrument = new SimpleObjectProperty<>(SMPInstrument.MARIO);
    private final ObjectProperty<SMPMode> mode = new SimpleObjectProperty<>(SMPMode.SONG);
    
    /** This keeps track of whether we have pressed the loop button or not. */
    private final BooleanProperty loopPressed = new SimpleBooleanProperty(false);

    /** This keeps track of whether we have pressed the mute button or not. */
    private final BooleanProperty mutePressed = new SimpleBooleanProperty(false);

    /** This keeps track of whether we have pressed the low A mute button or not. */
    private final BooleanProperty muteAPressed = new SimpleBooleanProperty(false);

    /** This keeps track of whether we have pressed the clipboard button or not. */
    private final BooleanProperty clipboardPressed = new SimpleBooleanProperty(false);
    
    private final BooleanProperty playbackActive = new SimpleBooleanProperty(false);
    
    /** The default time signature that we start out with is 4/4 time. */
    private final ObjectProperty<TimeSignature> timeSignature = new SimpleObjectProperty<>(TimeSignature.FOUR_FOUR);
    
    /** This is the current tempo that the program is running at. */
    private final DoubleProperty tempo = new SimpleDoubleProperty(Values.DEFAULT_TEMPO);
	
	private SMPAppModel() {}
	
	public boolean isCtrlPressed() {
	    return ctrlPressed.get();
	}
	
	public BooleanProperty getCtrlPressedProperty() {
	    return ctrlPressed;
	}
	
	public void setCtrlPressed(boolean ctrlPressed) {
	    this.ctrlPressed.set(ctrlPressed);
	}
	
	public boolean isShiftPressed() {
	    return shiftPressed.get();
	}
	
	public BooleanProperty getShiftPressedProperty() {
	    return shiftPressed;
	}
	
	public void setShiftPressed(boolean shiftPressed) {
	    this.shiftPressed.set(shiftPressed);
	}
	
	public SMPInstrument getSelectedInstrument() {
	    return selectedInstrument.get();
	}
	
	public ObjectProperty<SMPInstrument> getSelectedInstrumentProperty() {
	    return selectedInstrument;
	}
	
	public void setSelectedInstrument(SMPInstrument selectedInstrument) {
	    this.selectedInstrument.set(selectedInstrument);
	}
	
	public SMPMode getMode() {
        return mode.get();
    }
	
	public ObjectProperty<SMPMode> getModeProperty() {
        return mode;
    }

    public void setMode(SMPMode mode) {
        this.mode.set(mode);
    }
	
	public boolean isLoopPressed() {
        return loopPressed.get();
    }
	
	public BooleanProperty getLoopPressedProperty() {
        return loopPressed;
    }

    public void setLoopPressed(boolean loopPressed) {
        this.loopPressed.set(loopPressed);
    }

    public boolean isMutePressed() {
        return mutePressed.get();
    }
    
    public BooleanProperty getMutePressedProperty() {
        return mutePressed;
    }

    public void setMutePressed(boolean mutePressed) {
        this.mutePressed.set(mutePressed);
    }
    
    public boolean isMuteAPressed() {
        return muteAPressed.get();
    }

    public BooleanProperty getMuteAPressedProperty() {
        return muteAPressed;
    }
    
    public void setMuteAPressed(boolean muteAPressed) {
        this.muteAPressed.set(muteAPressed);
    }
    
    public boolean isClipboardPressed() {
        return clipboardPressed.get();
    }

    public BooleanProperty getClipboardPressedProperty() {
        return clipboardPressed;
    }
    
    public void setClipboardPressed(boolean clipboardPressed) {
        this.clipboardPressed.set(clipboardPressed);
    }
    
    public boolean isPlaybackActive() {
        return playbackActive.get();
    }
    
    public BooleanProperty getPlaybackActiveProperty() {
        return playbackActive;
    }
    
    public void setPlaybackActive(boolean playbackActive) {
        this.playbackActive.set(playbackActive);
    }
    
    public TimeSignature getTimeSignature() {
        return timeSignature.get();
    }
    
    public ObjectProperty<TimeSignature> getTimeSignatureProperty() {
        return timeSignature;
    }
    
    public void setTimeSignature(TimeSignature timeSignature) {
        this.timeSignature.set(timeSignature);
    }
    
    /**
     * @return The tempo that this program is running at.
     */
    public double getTempo() {
        return tempo.get();
    }
    
    public DoubleProperty getTempoProperty() {
        return tempo;
    }
    
    /**
     * Sets the tempo to what we give it here.
     *
     * @param num The tempo we want to set the program to run at.
     * @return The current tempo.
     */
    public void setTempo(double tempo) {
        this.tempo.set(tempo);
    }
	
}
