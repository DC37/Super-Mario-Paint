package smp.models.stateMachine;

import java.io.File;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;
import smp.components.InstrumentIndex;
import smp.models.staff.StaffArrangement;
import smp.models.staff.StaffSequence;

public class Variables {
	/** from <code>Staff</code> */
	public static ObjectProperty<StaffSequence> theSequence = new SimpleObjectProperty<>();
	/** from <code>Staff</code> */
	public static ObjectProperty<StaffArrangement> theArrangement = new SimpleObjectProperty<>();
	/** from <code>Staff</code> */
	public static ObjectProperty<File> theSequenceFile = new SimpleObjectProperty<>();
	/** @since 1.3 */
	public static ObjectProperty<InstrumentIndex> selectedInstrument = new SimpleObjectProperty<>();
	/** from <code>Staff</code> */
	public static StringProperty theSequenceName = new SimpleStringProperty();
	/** from either <code>StaffArrangement</code> or maybe <code>Staff</code> */ 
	public static ObservableList<String> arrangementList = FXCollections.observableArrayList();
	public static ObjectProperty<MultipleSelectionModel<String>> selectionModelProperty = new SimpleObjectProperty<>();
	
	public Variables() {
		theSequence.set(new StaffSequence());
		theArrangement.set(new StaffArrangement());
		theSequenceFile.set(null);
		selectedInstrument.set(InstrumentIndex.MARIO);
		theSequenceName.set("");
	}
}
