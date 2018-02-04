package smp.commandmanager;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import smp.components.staff.Staff;
import smp.fx.SMPFXController;

public class ModifySongManager extends CommandManager {

	private Staff theStaff;
	private SMPFXController controller;
	
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
	
	public void redo() {
		super.redo();
		theStaff.redraw();
	}
	
	public void undo() {
		super.undo();
		theStaff.redraw();
	}
	
	/**
	 * temporary until circular dependency is fixed
	 */
	public void setStaff(Staff st) {
		theStaff = st;
	}
}
