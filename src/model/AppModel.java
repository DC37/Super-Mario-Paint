package model;

import gui.Values;
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
    
    /**
     * The furthest you can reach by scrolling to the end of the sequence.
     * Technically this is the first line that cannot be displayed.
     */
    private final StateInteger maxLine;
    
    /**
     * Currently selected song in arranger mode. Set to -1 while in song mode.
     */
    private final StateInteger arrangementSongIndex;
    
    /**
     * This is the current tempo that the program is running at.
     */
    private final StateDouble tempo;
	
	private AppModel() {
		currentLine = new StateInteger(-1);
		maxLine = new StateInteger(Values.DEFAULT_LINES_PER_SONG);
		arrangementSongIndex = new StateInteger(-1);
		tempo = new StateDouble(Values.DEFAULT_TEMPO);
	}
	
	@SuppressWarnings("rawtypes")
	public void sync() {
    	if (!Platform.isFxApplicationThread())
    		throw new IllegalStateException("Cannot sync state outside JavaFX context!");
    	
    	StateItem[] items = new StateItem[] { currentLine, maxLine, arrangementSongIndex, tempo };
    	
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
    
    public StateInteger maxLineSource() {
        return maxLine;
    }
    
    public int getMaxLine() {
        return maxLine.get();
    }
    
    public void setMaxLine(int num) {
        maxLine.set(num);
    }
    
    public StateInteger arrangementSongIndexSource() {
        return arrangementSongIndex;
    }
    
    public int getArrangementSongIndex() {
        return arrangementSongIndex.get();
    }
    
    public void setArrangementSongIndex(int i) {
        arrangementSongIndex.set(i);
    }
    
    public StateDouble tempoSource() {
        return tempo;
    }
    
    /**
     * @return The tempo that this program is running at.
     */
    public double getTempo() {
        return tempo.get();
    }
    
    /**
     * Sets the tempo to what we give it here.
     *
     * @param num
     *            The tempo we want to set the program to run at.
     * @return The current tempo.
     */
    public void setTempo(double num) {
        tempo.set(num);
    }
	
}
