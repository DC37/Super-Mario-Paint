package smp.components.staff;

/**
 * When something tries to set a note to an illegal value, aka
 * octave > 9 or octave < 0, or notes not between 'A' and 'G', 
 * this exception will be thrown.
 * @author RehdBlob
 * @since 2012.08.13
 */
public class NoteOutOfBoundsException extends Exception {

	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -2497135573737999619L;


}
