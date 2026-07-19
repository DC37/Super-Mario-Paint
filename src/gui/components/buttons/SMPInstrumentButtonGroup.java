package gui.components.buttons;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class SMPInstrumentButtonGroup implements Iterable<SMPInstrumentButton> {

	private Map<SMPInstrumentButton, InvalidationListener> group;
    private BooleanProperty allActive;
    
    public SMPInstrumentButtonGroup() {
        this.group = new HashMap<>();
        this.allActive = new SimpleBooleanProperty(true);
    }
    
    public void addButton(SMPInstrumentButton b) {
        InvalidationListener activeListen = obs -> invalid();
        b.active().addListener(activeListen);
        group.put(b, activeListen);
    }
    
    public void removeButton(SMPInstrumentButton b) {
        InvalidationListener l = group.remove(b);
        b.active().removeListener(l);
    }
    
    private void invalid() {
        for (SMPInstrumentButton b : group.keySet()) {
            if (!b.isActive()) {
                allActive.setValue(false);
                return;
            }
            
            allActive.setValue(true);
        }
    }

    public Iterator<SMPInstrumentButton> iterator() {
        return group.keySet().iterator();
    }
    
    public ReadOnlyBooleanProperty allActive() {
        return allActive;
    }
	
}
