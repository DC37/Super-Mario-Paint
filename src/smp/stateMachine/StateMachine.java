package smp.stateMachine;

/**
 * This is the state machine that keeps track of what state the 
 * MPWindow is in. 
 * @author RehdBlob
 * @since 2012.08.07
 */
public class StateMachine {

	/**
	 * The default state that the program is in is the 
	 * EDITING state, in which notes are being placed on the staff.
	 */
	private static State state = State.EDITING;
	
	public static State getState() {
		return state;
	}
	
	public static void setState(State s) {
		state = s;
	}
	
}
