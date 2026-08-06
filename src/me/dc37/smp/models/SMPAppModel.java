package me.dc37.smp.models;

import backend.songs.TimeSignature;
import gui.SMPInstrument;
import gui.SMPMode;
import gui.Values;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SMPAppModel {

	private static SMPAppModel instance; 
	
	public static SMPAppModel getInstance() {
		if (instance == null) {
			instance = new SMPAppModel();
		}
		
		return instance;
	}
	
	private final BooleanProperty ctrlPressed = new SimpleBooleanProperty(false);
    private final BooleanProperty shiftPressed = new SimpleBooleanProperty(false);
    
    private final ObjectProperty<SMPInstrument> selectedInstrument = new SimpleObjectProperty<>(SMPInstrument.MARIO);
    private final ObjectProperty<SMPMode> mode = new SimpleObjectProperty<>(SMPMode.SONG);
    
    /** This keeps track of whether we have pressed the loop button or not. */
    private final BooleanProperty loopPressed = new SimpleBooleanProperty(false);

    /** This keeps track of whether we have pressed the mute button or not. */
    private final BooleanProperty mutePressed = new SimpleBooleanProperty(false);

    /** This keeps track of whether we have pressed the low A mute button or not. */
    private final BooleanProperty muteAPressed = new SimpleBooleanProperty(false);

    /** This keeps track of whether we have pressed the clipboard button or not. */
    private final BooleanProperty clipboardPressed = new SimpleBooleanProperty(false);
    
    private final BooleanProperty playbackActive = new SimpleBooleanProperty(false);
    
    /** The default time signature that we start out with is 4/4 time. */
    private final ObjectProperty<TimeSignature> timeSignature = new SimpleObjectProperty<>(TimeSignature.FOUR_FOUR);
    
    /** This is the current tempo that the program is running at. */
    private final DoubleProperty tempo = new SimpleDoubleProperty(Values.DEFAULT_TEMPO);
    
    /**
     * The current measure line number. Set to -1 as a special
     * "uninitialized" value, to force the initial redraw.
     */
    private final IntegerProperty currentLine = new SimpleIntegerProperty(-1);
    
    /**
     * The furthest you can reach by scrolling to the end of the sequence.
     * Technically this is the first line that cannot be displayed.
     */
    private final IntegerProperty maxLine = new SimpleIntegerProperty(Values.DEFAULT_LINES_PER_SONG);
    
    /** Currently selected song in arranger mode. Set to -1 while in song mode. */
    private final IntegerProperty arrangementSongIndex = new SimpleIntegerProperty(-1);
    
    /** The list of values denoting which notes should be extended. */
    private final IntegerProperty noteExtensions = new SimpleIntegerProperty(0);
    
    /** The list of values denoting which notes are filtered. */
    private final IntegerProperty filteredNotes = new SimpleIntegerProperty(-1);
    
    private final BooleanProperty cursorOnStaff = new SimpleBooleanProperty();
    
    private final StringProperty currentSongName = new SimpleStringProperty("");
    private final StringProperty currentArrangementName = new SimpleStringProperty("");
    
    /** This tells us whether we have modified the song or not. */
    private final BooleanProperty songModified = new SimpleBooleanProperty(false);
    
    /** This tells us whether we have modified the arrangement or not. */
    private final BooleanProperty arrangementModified = new SimpleBooleanProperty(false);
    
	
	private SMPAppModel() {}
	
	public boolean isCtrlPressed() {
	    return ctrlPressed.get();
	}
	
	public BooleanProperty getCtrlPressedProperty() {
	    return ctrlPressed;
	}
	
	public void setCtrlPressed(boolean ctrlPressed) {
	    this.ctrlPressed.set(ctrlPressed);
	}
	
	public boolean isShiftPressed() {
	    return shiftPressed.get();
	}
	
	public BooleanProperty getShiftPressedProperty() {
	    return shiftPressed;
	}
	
	public void setShiftPressed(boolean shiftPressed) {
	    this.shiftPressed.set(shiftPressed);
	}
	
	public SMPInstrument getSelectedInstrument() {
	    return selectedInstrument.get();
	}
	
	public ObjectProperty<SMPInstrument> getSelectedInstrumentProperty() {
	    return selectedInstrument;
	}
	
	public void setSelectedInstrument(SMPInstrument selectedInstrument) {
	    this.selectedInstrument.set(selectedInstrument);
	}
	
	public SMPMode getMode() {
        return mode.get();
    }
	
	public ObjectProperty<SMPMode> getModeProperty() {
        return mode;
    }

    public void setMode(SMPMode mode) {
        this.mode.set(mode);
    }
	
	public boolean isLoopPressed() {
        return loopPressed.get();
    }
	
	public BooleanProperty getLoopPressedProperty() {
        return loopPressed;
    }

    public void setLoopPressed(boolean loopPressed) {
        this.loopPressed.set(loopPressed);
    }

    public boolean isMutePressed() {
        return mutePressed.get();
    }
    
    public BooleanProperty getMutePressedProperty() {
        return mutePressed;
    }

    public void setMutePressed(boolean mutePressed) {
        this.mutePressed.set(mutePressed);
    }
    
    public boolean isMuteAPressed() {
        return muteAPressed.get();
    }

    public BooleanProperty getMuteAPressedProperty() {
        return muteAPressed;
    }
    
    public void setMuteAPressed(boolean muteAPressed) {
        this.muteAPressed.set(muteAPressed);
    }
    
    public boolean isClipboardPressed() {
        return clipboardPressed.get();
    }

    public BooleanProperty getClipboardPressedProperty() {
        return clipboardPressed;
    }
    
    public void setClipboardPressed(boolean clipboardPressed) {
        this.clipboardPressed.set(clipboardPressed);
    }
    
    public boolean isPlaybackActive() {
        return playbackActive.get();
    }
    
    public BooleanProperty getPlaybackActiveProperty() {
        return playbackActive;
    }
    
    public void setPlaybackActive(boolean playbackActive) {
        this.playbackActive.set(playbackActive);
    }
    
    public TimeSignature getTimeSignature() {
        return timeSignature.get();
    }
    
    public ObjectProperty<TimeSignature> getTimeSignatureProperty() {
        return timeSignature;
    }
    
    public void setTimeSignature(TimeSignature timeSignature) {
        this.timeSignature.set(timeSignature);
    }
    
    /**
     * @return The tempo that this program is running at.
     */
    public double getTempo() {
        return tempo.get();
    }
    
    public DoubleProperty getTempoProperty() {
        return tempo;
    }
    
    /**
     * Sets the tempo to what we give it here.
     *
     * @param num The tempo we want to set the program to run at.
     * @return The current tempo.
     */
    public void setTempo(double tempo) {
        this.tempo.set(tempo);
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
    
    public IntegerProperty getCurrentLineProperty() {
        return currentLine;
    }
    
    /**
     * Sets the current line number to whatever is given to this method.
     *
     * @param currentLine The number that we're trying to set our current line number to.
     */
    public void setCurrentLine(int currentLine) {
        this.currentLine.set(currentLine);
    }
    
    public int getMaxLine() {
        return maxLine.get();
    }
    
    public IntegerProperty getMaxLineProperty() {
        return maxLine;
    }
    
    public void setMaxLine(int maxLine) {
        this.maxLine.set(maxLine);
    }
    
    public int getArrangementSongIndex() {
        return arrangementSongIndex.get();
    }
    
    public IntegerProperty getArrangementSongIndexProperty() {
        return arrangementSongIndex;
    }
    
    public void setArrangementSongIndex(int arrangementSongIndex) {
        this.arrangementSongIndex.set(arrangementSongIndex);
    }
    
    /**
     * Get specific note extension bit
     * @param idx index of the bit to read, is in interval [0, 31]
     */
    public boolean getNoteExtension(int idx) {
        int m = 1 << idx;
        int v = noteExtensions.get();
        return (v & m) != 0;
    }

    public boolean[] getNoteExtensions() {
        boolean[] ret = new boolean[32];
        for (int i = 0; i < 32; i++) {
            ret[i] = getNoteExtension(i);
        }
        return ret;
    }
    
    public IntegerProperty getNoteExtensionsProperty() {
        return noteExtensions;
    }
    
    /**
     * Set specific note extension bit
     * @param idx index of the bit to modify, is in interval [0, 31]
     * @param b value to set
     */
    public void setNoteExtension(int idx, boolean b) {
        int v = noteExtensions.get();
        int m = 1 << idx;
        v = b ? v | m : v & (~m);
        noteExtensions.set(v);
    }
    
    public void setNoteExtensions(boolean[] set) {
        for (int i = 0; i < set.length; i++) {
            setNoteExtension(i, set[i]);
        }
    }
    
    /**
     * Get specific filtered note bit
     * @param idx index of the bit to read, is in interval [0, 31]
     */
    public boolean getFilteredNote(int idx) {
        int m = 1 << idx;
        int v = filteredNotes.get();
        return (v & m) != 0;
    }

    public int getFilteredNotes() {
        return filteredNotes.get();
    }
    
    public IntegerProperty getFilteredNotesProperty() {
        return filteredNotes;
    }
    
    /**
     * Set specific filtered note bit
     * @param idx index of the bit to modify, is in interval [0, 31]
     * @param b value to set
     */
    public void setFilteredNote(int idx, boolean b) {
        int v = filteredNotes.get();
        int m = 1 << idx;
        v = b ? v | m : v & (~m);
        filteredNotes.set(v);
    }

    public void setFilteredNotes(int v) {
        filteredNotes.set(v);
    }
    
    public boolean isCursorOnStaff() {
        return cursorOnStaff.get();
    }
    
    public BooleanProperty getCursorOnStaffProperty() {
        return cursorOnStaff;
    }
    
    public void setCursorOnStaff(boolean cursorOnStaff) {
        this.cursorOnStaff.set(cursorOnStaff);
    }
    
    public String getCurrentSongName() {
        return currentSongName.get();
    }
    
    public StringProperty getCurrentSongNameProperty() {
        return currentSongName;
    }
    
    public void setCurrentSongName(String currentSongName) {
        this.currentSongName.set(currentSongName);
    }
    
    public String getCurrentArrangementName() {
        return currentArrangementName.get();
    }
    
    public StringProperty getCurrentArrangementNameProperty() {
        return currentArrangementName;
    }
    
    public void setCurrentArrangementName(String currentArrangementName) {
        this.currentArrangementName.set(currentArrangementName);
    }
    
    /**
     * @return Whether we have modified the current song or not.
     */
    public boolean isSongModified() {
        return songModified.get();
    }
    
    /**
     * Sets whether the current song is modified.
     *
     * @param songModified Whether we have modified the song or not.
     */
    public void setSongModified(boolean songModified) {
        this.songModified.set(songModified);
    }
    
    /**
     * @return Whether we have modified the current arrangement or not.
     */
    public boolean isArrangementModified() {
        return arrangementModified.get();
    }

    /**
     * Sets whether the current arrangement is modified.
     * 
     * @param arrangementModified Whether we have modified the arrangement or not.
     */
    public void setArrangementModified(boolean arrangementModified) {
        this.arrangementModified.set(arrangementModified);
    }
	
}
