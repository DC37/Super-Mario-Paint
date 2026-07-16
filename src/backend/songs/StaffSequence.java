package backend.songs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import gui.Values;

/**
 * <p>A song is a list of {@link StaffNoteLine}s and a context determining how
 * the notes should be played: tempo, time signature, sustain flags, soundfont
 * binding. A song is also given a name.
 * 
 * <p>The internal list of note lines is not directly accessible, but a
 * {@link #getLine} method is provided to access the modifiable
 * lines. The method will resize the song with empty lines if necessary, making
 * songs virtually infinite.
 */
public class StaffSequence {
    
    /** The name of this song. */
    private String name;

    /** The tempo of this song. */
    private double tempo = Values.DEFAULT_TEMPO;

    /** The sustain flags for each instrument. */
    private final boolean[] noteExtensions = new boolean[Values.NUM_INSTRUMENTS];

    /** The time signature of this song. */
    private TimeSignature timeSignature = TimeSignature.FOUR_FOUR;
    
    /** The soundset bound to this song (assumed to be loaded). */
    private String soundsetBinding = "";

    /** The note lines in this song. */
    private final List<StaffNoteLine> theLines;

    /** Default constructor. Makes an empty song. */
    public StaffSequence() {
        this(Values.DEFAULT_LINES_PER_SONG);
    }
    
    /**
     * Makes an empty song of a specified length.
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

    /**
     * Get the tempo of this song.
     */
    public double getTempo() {
        return tempo;
    }

    /**
     * Set the tempo of this song.
     * @param tempo A tempo value
     */
    public void setTempo(double tempo) {
        this.tempo = tempo;
    }

    /**
     * Get the array holding sustain flags for each instrument in this song.
     * @return
     */
    public boolean[] getNoteExtensions() {
        return noteExtensions;
    }

    /**
     * Set the sustain flags.
     * @param exts An array holding the flag values
     * @throws IllegalArgumentException if the array to assign is the wrong length
     */
    public void setNoteExtensions(boolean[] exts) {
        if (exts.length != noteExtensions.length) {
            throw new IllegalArgumentException("setNoteExtensions expects an array of length " + noteExtensions.length);
        }
        
        System.arraycopy(exts, 0, noteExtensions, 0, noteExtensions.length);
    }

    /**
     * Get the time signature of this song.
     */
    public TimeSignature getTimeSignature() {
        return timeSignature;
    }

    /**
     * Set the time signature of this song.
     * @param timeSignature A time signature
     */
    public void setTimeSignature(TimeSignature timeSignature) {
        this.timeSignature = timeSignature;
    }

    /**
     * Get the soundset bound to this song.
     */
    public String getSoundset() {
        return soundsetBinding;
    }

    /**
     * Bind a soundset to this song.
     * @param soundsetBinding The soundset to bind
     */
    public void setSoundset(String soundsetBinding) {
        this.soundsetBinding = soundsetBinding;
    }
    
    /**
     * <p>Get the length of this song.
     * 
     * <p>The length is defined as the number of note lines that must be played
     * in order to play the full song. It is guaranteed to be a multiple of
     * {@code this.getTimeSignature().barLength()}.
     * 
     * @return the number of playable lines in this song
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
     * <p>Get a line of notes in this song.
     * 
     * <p>This method extends the song with empty lines if the parameter is
     * greater than the current length.
     * 
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
     * <p>Add empty lines to set the length of the song.</p>
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
