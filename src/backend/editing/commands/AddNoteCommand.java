package backend.editing.commands;

import backend.editing.CommandInterface;
import backend.songs.StaffNote;
import backend.songs.StaffNoteLine;

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
