package smp.components.staff;


/**
 * A note on the musical staff.
 * @author RehdBlob
 * @since 2012.08.13
 */
public class Note  {
    
	/**
	 * The channel on which to play the note on.
	 */
	private int channel;
	
	/**
	 * Which octave is the note on?
	 */
	private int octave;
	
	/**
	 * Which character note is this note?
	 * A, B, C, D, E, F, G are valid choices.
	 */
	private char note;
	
	/**
	 * Is the note sharp or flat? Or double sharp
	 * or double flat? This integer tells the number
	 * of half steps to add or subtract from the note.
	 * Default is 0.
	 */
	private int accidental;
	
	public int getOctave() {
		return octave;
	}
	
	public void setOctave(int o) throws NoteOutOfBoundsException {
		if (o < 9 && o > 0)
			octave = o;
		else
			throw new NoteOutOfBoundsException();
	}
	
	public char getNote() {
		return note;
	}

	public void setNote(char c) throws NoteOutOfBoundsException {
		if (c >= 'A' && c <= 'G')
			note = c;
		else
			throw new NoteOutOfBoundsException();
	}
	
	public void setChannel(int c) {
		channel = c;
	}
	
	public int getChannel() {
		return channel;
	}
	
	public void setAccidental(int a) throws AccidentalOutOfBoundsException {
		if (Math.abs(a) >= 0 && Math.abs(a) < 3)
			accidental = a;
		else 
			throw new AccidentalOutOfBoundsException();
	}
	
	public int getAccidental() {
		return accidental;
	}
}
