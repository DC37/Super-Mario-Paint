package smp.commandmanager;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import smp.fx.SMPFXController;

public class ModifySongManager extends CommandManager {

	private Runnable refreshDisplay;
	private SMPFXController controller;
	
	public ModifySongManager(Runnable refreshDisplay, SMPFXController ct) {
		super();
		this.refreshDisplay = refreshDisplay;
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
		refreshDisplay.run();
	}
	
	public void undo() {
		super.undo();
        refreshDisplay.run();
	}
}
