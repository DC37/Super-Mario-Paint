package me.dc37.smp.models;

import gui.SMPInstrument;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    
    /** This keeps track of whether we have pressed the loop button or not. */
    private final BooleanProperty loopPressed = new SimpleBooleanProperty(false);

    /** This keeps track of whether we have pressed the mute button or not. */
    private final BooleanProperty mutePressed = new SimpleBooleanProperty(false);

    /** This keeps track of whether we have pressed the low A mute button or not. */
    private final BooleanProperty muteAPressed = new SimpleBooleanProperty(false);

    /** This keeps track of whether we have pressed the clipboard button or not. */
    private final BooleanProperty clipboardPressed = new SimpleBooleanProperty(false);
	
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
	
	public boolean isLoopPressed() {
        return loopPressed.get();
    }
	
	public BooleanProperty getLoopPressedProperty() {
        return loopPressed;
    }

    public void setLoopPressed(boolean b) {
        loopPressed.set(b);
    }

    public boolean isMutePressed() {
        return mutePressed.get();
    }
    
    public BooleanProperty getMutePressedProperty() {
        return mutePressed;
    }

    public void setMutePressed(boolean b) {
        mutePressed.set(b);
    }
    
    public boolean isMuteAPressed() {
        return muteAPressed.get();
    }

    public BooleanProperty getMuteAPressedProperty() {
        return muteAPressed;
    }
    
    public void setMuteAPressed(boolean b) {
        muteAPressed.set(b);
    }
    
    public boolean isClipboardPressed() {
        return clipboardPressed.get();
    }

    public BooleanProperty getClipboardPressedProperty() {
        return clipboardPressed;
    }
    
    public void setClipboardPressed(boolean b) {
        clipboardPressed.set(b);
    }
	
}
