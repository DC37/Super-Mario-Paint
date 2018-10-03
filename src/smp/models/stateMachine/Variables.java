package smp.models.stateMachine;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import smp.components.InstrumentIndex;
import smp.models.staff.StaffArrangement;
import smp.models.staff.StaffSequence;

public class Variables {
	/** from <code>Staff</code> */
	public static ObjectProperty<StaffSequence> theSequence;
	/** from <code>Staff</code> */
	public static ObjectProperty<StaffArrangement> theArrangement;
	/** @since 1.3 */
	public static ObjectProperty<InstrumentIndex> selectedInstrument;
	/** from <code>Staff</code> */
	public static StringProperty theSequenceName;
	
	public Variables() {
		theSequence.set(new StaffSequence());
		theArrangement.set(new StaffArrangement());
		selectedInstrument.set(InstrumentIndex.MARIO);
		theSequenceName.set("");
	}
}
