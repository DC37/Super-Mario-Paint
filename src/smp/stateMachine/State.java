package smp.stateMachine;

/**
 * This class defines the integer states that the 
 * state machine references. Listeners may also check
 * these states to determine whether they are allowed
 * to react to events.
 * @author RehdBlob
 * @since 2012.08.12
 */
public class State {
	
	public static final int EDITING = 0;
	public static final int SONG_PLAYING = 1;
	public static final int ARR_PLAYING = 2;
	public static final int MENU_OPEN = 3;
	
	private static final int LOW_STATE = 0;
	private static final int HIGH_STATE = 3;
	
	public static boolean isValid(int i) {
		return i >= LOW_STATE && i <= HIGH_STATE ;
	}
	
}
