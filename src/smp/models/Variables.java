package smp.models;

import javafx.beans.property.ObjectProperty;
import smp.components.InstrumentIndex;

public class Variables {
	public static ObjectProperty<InstrumentIndex> selectedInstrument;
	
	public Variables() {
		selectedInstrument.set(InstrumentIndex.MARIO);
	}
}
