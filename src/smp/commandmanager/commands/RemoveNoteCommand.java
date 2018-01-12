package smp.commandmanager.commands;

import smp.commandmanager.CommandInterface;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;

public class RemoveNoteCommand implements CommandInterface {

	private StaffNoteLine theLine;
	private StaffNote theNote;
	
	public RemoveNoteCommand(StaffNoteLine line, StaffNote note) {
		theLine = line;
		theNote = note;
	}
	
	@Override
	public void redo() {
		theLine.remove(theNote);
	}

	@Override
	public void undo() {
		theLine.add(theNote);
	}

}
