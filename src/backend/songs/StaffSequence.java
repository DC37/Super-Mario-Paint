package backend.songs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import gui.Values;

/**
 * We might not even need MIDI to do this sequencing stuff. This class keeps
 * track of whatever is displaying on the staff right now.
 *
 * @author RehdBlob
 * @since 2013.08.23
 */
public class StaffSequence {
	
	/** The name of this sequence. */
	private String name;

    /** The tempo of this sequence. */
    private double tempo = Values.DEFAULT_TEMPO;

    /** This tells us which notes are extended (green highlight) or not. */
    private final boolean[] noteExtensions = new boolean[Values.NUMINSTRUMENTS];

    /** The time signature of this sequence. */
    private TimeSignature timeSignature = TimeSignature.FOUR_FOUR;
    
    /** The soundset bound to and should be loaded for this sequence. */
    private String soundsetBinding = "";

    /** These are all of the lines on the staff. */
    private final List<StaffNoteLine> theLines;

    /** Default constructor. Makes an empty song. */
    public StaffSequence() {
    	this(Values.DEFAULT_LINES_PER_SONG);
    }
    
    /**
     * Makes an empty song sequence of a specified length.
     * @param length The initial length
     */
    public StaffSequence(int length) {
        theLines = new ArrayList<>();
        
        for (int i = 0; i < length; i++) {
            theLines.add(new StaffNoteLine());
        }
    }
    
    /**
     * Builds a sequence by making a deep copy of another sequence of note lines.
     * @param lines A list of note lines
     */
    public StaffSequence(List<StaffNoteLine> lines) {
    	this.theLines = lines.stream()
    			.map(StaffNoteLine::new)
    			.collect(Collectors.toList());
    }
    
    /**
     * Get the name of this song.
     */
    public String getName() {
    	return name;
    }
    
    /**
     * Set the name of this song.
     * @param name the new name
     */
    public void setName(String name) {
    	this.name = name;
    }

    /** @return The tempo of this sequence. */
    public double getTempo() {
        return tempo;
    }

    /**
     * Sets the tempo of this sequence.
     *
     * @param t
     *            The tempo of this sequence.
     */
    public void setTempo(double t) {
        tempo = t;
    }

    /** @return The bitfield denoting which notes are extended. */
    public boolean[] getNoteExtensions() {
        return noteExtensions;
    }

    /**
     * Set the note extensions.
     * @param exts An array holding the new extension values
     * @throws IllegalArgumentException if the array to assign is the wrong length
     */
    public void setNoteExtensions(boolean[] exts) {
    	if (exts.length != noteExtensions.length) {
    		throw new IllegalArgumentException("setNoteExtensions expects an array of length " + noteExtensions.length);
    	}
    	
    	System.arraycopy(exts, 0, noteExtensions, 0, noteExtensions.length);
    }

    /** @return The time signature of this sequence. */
    public TimeSignature getTimeSignature() {
        return timeSignature;
    }

    public void setTimeSignature(TimeSignature t) {
        this.timeSignature = t;
    }

	/**
	 * @return The soundset bound to this sequence.
	 * @since v1.1.2
	 */
	public String getSoundset() {
		return soundsetBinding;
    }

	/**
	 * Sets the soundset for this sequence which should be loaded with the
	 * sequence.
	 * @since v1.1.2
	 */
	public void setSoundset(String soundset) {
		soundsetBinding = soundset;
    }
	
	/**
	 * The length of the sequence is defined as the number of note lines that
	 * must be played in order to play the full sequence. It is guaranteed to
	 * be a multiple of {@code this.getTimeSignature().barLength()}.
	 * @return the number of playable lines in this sequence
	 */
    public int getLength() {
        int lastNonempty = theLines.size() - 1;
        
        while (lastNonempty >= 0 && theLines.get(lastNonempty).getNotes().isEmpty()) {
            lastNonempty--;
        }
        
        if (lastNonempty < 0) {
            return 0;
            
        } else {
            int barLength = timeSignature.top();
            // return first multiple of barLength that is > lastNonempty
            return barLength * ((lastNonempty / barLength) + 1);
        }
    }

    /**
     * This method extends the sequence with new empty lines if the parameter
     * is greater than the current length.
     * @param i The index of the line to get (first index 0)
     * @return The line at index i
     * @throws IndexOutOfBoundsException if i < 0
     */
    public StaffNoteLine getLine(int i) {
    	if (i >= theLines.size()) {
            resize(i + 1);
    	}
    	
    	return theLines.get(i);
    }
    
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Tempo = " + tempo + "\n");
        out.append("Extensions = " + noteExtensions + "\n");
        out.append(theLines.toString() + "\n");
        return out.toString();
    }
    
    /**
     * <p>Add empty lines to set the length of the sequence.</p>
     * 
     * <p>Without effect if the length is already greater or equal.</p>
     * 
     * @param n the desired length
     */
    private void resize(int n) {
        int currentSize = theLines.size();
        for (int i = 0; i < n - currentSize; i++) {
            theLines.add(new StaffNoteLine());
        }   
    }

}
