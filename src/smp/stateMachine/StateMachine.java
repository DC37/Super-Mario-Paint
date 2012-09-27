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
    private static State currentState = State.EDITING;

    /**
     * Do not make an instance of this class! The implementation is such
     * that several classes may check the overall state of the program,
     * so there should only ever be just the class and its static variables
     * and methods around.
     * @deprecated
     */
    private StateMachine(){
    }

    /**
     * Get the current <code>State</code> of the <code>StateMachine</code>
     * @return The current <code>State</code>.
     */
    public static synchronized State getState() {
        return currentState;
    }

    /**
     * @param s Set the <code>StateMachine</code> to a certain State.
     */
    public static synchronized void setState(State s) {
        currentState = s;
    }

    public static synchronized void resetState() {
        currentState = State.EDITING;
    }
}
