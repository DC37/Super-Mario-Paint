package me.dc37.smp.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

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
	
}
