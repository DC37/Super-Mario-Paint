package smp.commandmanager.commands;

import smp.commandmanager.CommandInterface;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;

public class AddNoteCommand implements CommandInterface {

	private StaffNoteLine theLine;
	private StaffNote theNote;

	public AddNoteCommand(StaffNoteLine line, StaffNote note) {
		theLine = line;
		theNote = note;
	}
	
	@Override
	public void redo() {
		theLine.add(theNote);
	}

	@Override
	public void undo() {
		theLine.remove(theNote);
	}

}
