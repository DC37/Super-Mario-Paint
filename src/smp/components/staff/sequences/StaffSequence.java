package smp.components.staff.sequences;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import smp.components.Values;
import smp.stateMachine.TimeSignature;

/**
 * We might not even need MIDI to do this sequencing stuff. This class keeps
 * track of whatever is displaying on the staff right now.
 *
 * @author RehdBlob
 * @since 2013.08.23
 */
public class StaffSequence implements Serializable {

    /**
     * Generated serial ID.
     */
    private static final long serialVersionUID = 5752285850525402081L;

    /**
     * The tempo of this sequence.
     */
    private double tempo = Values.DEFAULT_TEMPO;

    /** These are all of the lines on the staff. */
    private List<StaffNoteLine> theLines;

    /** This tells us which notes are extended (green highlight) or not. */
    private boolean[] noteExtensions = new boolean[Values.NUMINSTRUMENTS];

    /** The time signature of this sequence. */
    private TimeSignature t = TimeSignature.FOUR_FOUR;
    
    /** The soundset bound to and should be loaded for this sequence. */
    private String soundsetBinding = "";

    /** Default constructor. Makes an empty song. */
    public StaffSequence() {
        theLines = new ArrayList<StaffNoteLine>();
        for (int i = 0; i < Values.DEFAULT_LINES_PER_SONG; i++)
            theLines.add(new StaffNoteLine());
    }
    
    public int getLength() {
        return theLines.size();
    }
    
    /**
     * <p>Add empty lines to set the length of the sequence.</p>
     * 
     * <p>Without effect if the length is already greater or equal.</p>
     * 
     * @param n the desired length
     */
    public void resize(int n) {
        int currentSize = theLines.size();
        for (int i = 0; i < n - currentSize; i++) {
            theLines.add(new StaffNoteLine());
        }   
    }
    
    /**
     * <p>Compute the index of the line marking the end of this sequence,
     * <i>i.e.</i> the first line that should <b>not</b> be played.</p>
     * 
     * <p>Takes into account the time signature (length of a bar).</p>
     */
    public int getEndlineIndex() {
        int lastNonempty = theLines.size() - 1;
        while (lastNonempty >= 0 && theLines.get(lastNonempty).isEmpty()) {
            lastNonempty--;
        }
        if (lastNonempty < 0) {
            return 0;
        } else {
            int barLength = t.top();
            // return first multiple of barLength that is > lastNonempty
            return barLength * ((lastNonempty / barLength) + 1);
        }
    }
    
    /**
     * <p>Resize the sequence so that the display won't show any lines beyond the
     * sequence's size while the player is running.</p>
     * 
     * <p>The result is some <i>n</i> such that:</p>
     * <ul>
     * <li><i>n</i> is greater than or equal to the minimal sequence size;</li>
     * <li><i>n</i> is greater than or equal to the result of {@link getEndlineIndex};</li>
     * <li><i>n</i> is a multiple of this sequence's bar length;</li>
     * <li>If <i>k</i> consecutive screens are necessary to display the whole sequence,
     * and one screen is <i>p</i>-wide, then <i>n</i> is greater than or equal to
     * <i>(k+1)*p</i></li>
     * <li><i>n</i> is minimal for the conditions above.</li>
     * </ul>
     */
    public void normalize() {
        int endline = getEndlineIndex();
        int barLength = t.top();
        
        int screenWidth = Values.NOTELINES_IN_THE_WINDOW;
        int numberOfScreens = (endline / screenWidth) + 1;
        
        int n = endline;
        if (n < Values.DEFAULT_LINES_PER_SONG)
            n = Values.DEFAULT_LINES_PER_SONG;
        
        // "+1" in case one additional screen is necessary; this can happen if a
        // song is played from the middle
        if (n < (numberOfScreens + 1) * screenWidth)
            n = (numberOfScreens + 1) * screenWidth;
        
        int r = n % barLength;
        if (r != 0)
            n = n - r + barLength;
        
        resize(n);
    }
    
    public StaffNoteLine getLine(int i) throws IndexOutOfBoundsException {
        return theLines.get(i);
    }

    /**
     * <p>Safe version of {@link getLine} that resizes the sequence if the
     * index is out of bounds.</p>
     * 
     * <p>If the sequence's length is bound to UI behavior in some way, it is
     * recommended to use {@link getLine} instead, as calling this method may
     * have unexpected consequences on the UI side.
     */
    public StaffNoteLine getLineSafe(int i) {
        try {
            if (i < 0) {
            /* Out of bounds in a bad way, add one to size, 
               then get the last element
               Should be an empty line... */
                resize(theLines.size() + 1);
                return getLineSafe(theLines.size() - 1);
            }
            return theLines.get(i);
        } catch (IndexOutOfBoundsException e) {
            /* Otherwise recursively add lines until we have enough */
            resize(i + 1);
            return getLineSafe(i);
        }
    }

    /**
     * @param i
     *            The index that we want to modify.
     * @param s
     *            The StaffNoteLine that we want to place at this index.
     */
    public void setLine(int i, StaffNoteLine s) {
        theLines.set(i, s);
    }
    
    /**
     * <p>Insert empty lines to implement the "Multiply Tempo" feature.</p>
     */
    public void expand(int n) {
        if (n < 2 || theLines.isEmpty())
            return;
        
        int sz = theLines.size();
        for (int i = 0; i < sz; i++) {
            int idx = (i * n) + 1;
            for (int j = 0; j < n - 1; j++)
                theLines.add(idx, new StaffNoteLine());
        }
    }
    
    /**
     * <p>Remove the empty lines resulting from {@link expand}.</p>
     * @throws IllegalArgumentException when attempting to remove a non empty line.
     */
    public void retract(int n) throws IllegalArgumentException {
        if (n < 2 || theLines.isEmpty())
            return;
        
        int sz = theLines.size();
        // Check there is no error before modifying the list
        for (int i = 0; i < sz; i++) {
            if (i % n == 0)
                continue;
            
            if (!theLines.get(i).isEmpty())
                throw new IllegalArgumentException("Can't undo Multiply Tempo");
        }
        
        for (int i = sz - 1; i >= 0; i--) {
            if (i % n == 0)
                continue;
            
            theLines.remove(i);
        }
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

    /**
     * @param i
     *            The note extensions bitfield that we want to set.
     */
    public void setNoteExtensions(boolean[] i) {
        noteExtensions = i;
    }

    /** @return The bitfield denoting which notes are extended. */
    public boolean[] getNoteExtensions() {
        return noteExtensions;
    }

    public void setTimeSignature(TimeSignature t) {
        this.t = t;
    }

    public void setTimeSignature(String s) {
        t = TimeSignature.valueOf(s);
    }

    /** @return The time signature of this sequence. */
    public TimeSignature getTimeSignature() {
        return t;
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
	 * @return The soundset bound to this sequence.
	 * @since v1.1.2
	 */
	public String getSoundset() {
		return soundsetBinding;
    }
    
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Tempo = " + tempo + "\n");
        out.append("Extensions = " + noteExtensions + "\n");
        out.append(theLines.toString() + "\n");
        return out.toString();
    }

}
