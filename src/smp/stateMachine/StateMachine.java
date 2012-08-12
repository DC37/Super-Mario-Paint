package smp.stateMachine;

/**
 * This is the state machine that keeps track of what state the 
 * MPWindow is in. 
 * @author RehdBlob
 * @since 2012.08.07
 */
public class StateMachine {

	private int state = State.EDITING;
	
	
	public int getState() {
		return state;
	}
	
	public void setState(int s) throws InvalidStateException {
		if (State.isValid(s))
			state = s;
		else
			throw new InvalidStateException();
	}
}
