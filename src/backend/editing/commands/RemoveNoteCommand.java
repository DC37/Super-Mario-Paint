package backend.editing.commands;

import backend.editing.CommandInterface;
import backend.songs.StaffNote;
import backend.songs.StaffNoteLine;

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
