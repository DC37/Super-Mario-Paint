package smp.commandmanager;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import smp.components.staff.Staff;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import smp.fx.SMPFXController;

public class ModifySongManager extends CommandManager {

	private Staff theStaff;
	private SMPFXController controller;
	private ModifySongCommand newCommand;
	
	public ModifySongManager(Staff st, SMPFXController ct) {
		super();
		theStaff = st;
		controller = ct;
		
		controller.getBasePane().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent event) {
				if(event.isControlDown())
					switch(event.getCode()) {
					case Y:
						redo();
						break;
					case Z:
						undo();
						break;
					default:
						break;
					}
			}
		});
	}
	
	private void checkNewCommand() {
		if(newCommand == null) {
			this.redoStack.clear();
			newCommand = new ModifySongCommand(theStaff);
		}
	}
	
	public void addNote(StaffNoteLine line, StaffNote note) {
		checkNewCommand();
		newCommand.addNote(line, note);
	}
	
	public void removeNote(StaffNoteLine line, StaffNote note) {
		checkNewCommand();
		newCommand.removeNote(line, note);
	}

	public void addVolume(StaffNoteLine line, int newVolume) {
		checkNewCommand();
		newCommand.addVolume(line, newVolume);
	}
	
	public void removeVolume(StaffNoteLine line, int oldVolume) {
		checkNewCommand();
		newCommand.removeVolume(line, oldVolume);
	}
	
	public void record() {
		if(newCommand != null) {
			execute(newCommand);
			newCommand = null;
		}
	}
	
	/**
	 * if currently placing or erasing notes and you undo, record the command
	 * and then undo that command
	 */
	public void undo() {
		record();
		super.undo();
	}
	
	/**
	 * temporary until circular dependency is fixed
	 */
	public void setStaff(Staff st) {
		theStaff = st;
	}
}
