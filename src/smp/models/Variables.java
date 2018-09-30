package smp.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import smp.components.InstrumentIndex;
import smp.components.Values;

public class Variables {
	public static ObjectProperty<StaffSequence> loadedSequence;
	public static ObjectProperty<StaffArrangement> loadedArrangement;
	public static ObjectProperty<InstrumentIndex> selectedInstrument;
	public static BooleanProperty[] noteExtensions;
	public static StringProperty songName;
	
	public Variables() {
		loadedSequence.set(new StaffSequence());
		loadedArrangement.set(new StaffArrangement());
		selectedInstrument.set(InstrumentIndex.MARIO);
		noteExtensions = new BooleanProperty[Values.NUMINSTRUMENTS];
		songName.set("");
	}
}
