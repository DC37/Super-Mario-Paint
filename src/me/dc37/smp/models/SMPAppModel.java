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
	
}
