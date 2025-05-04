package smp.commandmanager.commands;

import smp.commandmanager.CommandInterface;
import smp.components.staff.sequences.StaffNoteLine;

public class AddVolumeCommand implements CommandInterface {

	StaffNoteLine theLine;
	int theNewVolume;
	
	public AddVolumeCommand(StaffNoteLine line, int newVolume) {
		theLine = line;
		theNewVolume = newVolume;
	}
	
	@Override
	public void redo() {
		theLine.setVolume(theNewVolume);
	}

	@Override
	public void undo() {
		//do nothing, RemoveVolumeCommand will handle this
	}

}
