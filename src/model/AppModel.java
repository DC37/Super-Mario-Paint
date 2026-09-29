package model;

import javafx.application.Platform;

public class AppModel {

	private static AppModel instance = null;
	
	public static AppModel getInstance() {
		if (instance == null)
			instance = new AppModel();
		
		return instance;
	}
	
	/**
     * The current measure line number. Set to -1 as a special
     * "uninitialized" value, to force the initial redraw.
     */
    private final StateInteger currentLine;
	
	private AppModel() {
		currentLine = new StateInteger(-1);
	}
	
	@SuppressWarnings("rawtypes")
	public void sync() {
    	if (!Platform.isFxApplicationThread())
    		throw new IllegalStateException("Cannot sync state outside JavaFX context!");
    	
    	StateItem[] items = new StateItem[] { currentLine };
    	
    	for (StateItem i: items)
    		i.sync();
    }
	
	public StateInteger currentLineSource() {
        return currentLine;
    }
	
	/**
     * Gets the current line number that we're on. Typically a value between 0
     * and 383 for most files unless you've done fun stuff and removed the
     * 96-measure limit.
     *
     * @return The current line number (left justify)
     */
    public int getCurrentLine() {
    	return currentLine.get();
    }

    /**
     * Sets the current line number to whatever is given to this method.
     *
     * @param num The number that we're trying to set our current line number to.
     */
    public void setCurrentLine(int num) {
        currentLine.set(num);
    }
	
}
