package smp.models.stateMachine;

import java.io.File;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import smp.components.InstrumentIndex;
import smp.models.staff.StaffArrangement;
import smp.models.staff.StaffSequence;

public class Variables {
	/** from <code>Staff</code> */
	public static ObjectProperty<StaffSequence> theSequence;
	/** from <code>Staff</code> */
	public static ObjectProperty<StaffArrangement> theArrangement;
	/** from <code>Staff</code> */
    public static ObjectProperty<File> theSequenceFile;
	/** @since 1.3 */
	public static ObjectProperty<InstrumentIndex> selectedInstrument;
	/** from <code>Staff</code> */
	public static StringProperty theSequenceName;
	/** from either <code>StaffArrangement</code> or maybe <code>Staff</code> */ 
	public static ObservableList<String> arrangementList;
	
	public Variables() {
		theSequence.set(new StaffSequence());
		theArrangement.set(new StaffArrangement());
		theSequenceFile.set(null);
		selectedInstrument.set(InstrumentIndex.MARIO);
		theSequenceName.set("");
//		arrangementList = FXCollections.observableArrayList();
	}
	
	public static void setArrangementList(ObservableList<String> arrangementList) {
		Variables.arrangementList = arrangementList;
	}
}
