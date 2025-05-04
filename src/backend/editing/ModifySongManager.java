package backend.editing;

public class ModifySongManager extends CommandManager {

	private Runnable refreshDisplay;
	
	public ModifySongManager(Runnable refreshDisplay) {
		super();
		this.refreshDisplay = refreshDisplay;
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
