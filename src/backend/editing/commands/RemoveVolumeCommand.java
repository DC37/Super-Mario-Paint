package smp.commandmanager.commands;

import smp.commandmanager.CommandInterface;
import smp.components.staff.sequences.StaffNoteLine;

public class RemoveVolumeCommand implements CommandInterface {

	StaffNoteLine theLine;
	int theOldVolume;
	
	public RemoveVolumeCommand(StaffNoteLine line, int oldVolume) {
		theLine = line;
		theOldVolume = oldVolume;
	}
	
	@Override
	public void redo() {
		//do nothing, AddVolumeCommand will handle this
	}

	@Override
	public void undo() {
		theLine.setVolume(theOldVolume);
	}

}
