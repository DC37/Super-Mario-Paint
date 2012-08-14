package smp.components.staff;


/**
 * A note on the musical staff.
 * @author RehdBlob
 * @since 2012.08.13
 */
public class Note  {
    
	private int octave;
	
	private char note;
	
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
	
}
