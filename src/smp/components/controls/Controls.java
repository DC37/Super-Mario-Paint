package smp.components.controls;

import smp.stateMachine.State;
import smp.stateMachine.StateMachine;

public class Controls {

    /**
     * The reference to the play button on the staff.
     */
    private PlayButton play;

    /**
     * The reference to the stop button on the staff.
     */
    private StopButton stop;

    /**
     * Initializes the set of controls that will be used in Super Mario Paint.
     */
    public Controls() {

    }

    /**
     * Change
     * @param s The state that the program is supposed to be changed to.
     */
    public void checkState() {
        State s = StateMachine.getState();
        switch(s) {
        case EDITING:
            break;
        case SONG_PLAYING:
            break;
        case ARR_PLAYING:
            break;
        case PAUSE:
            break;
        case EDITING_ADV:
            break;
        case SONG_PLAYING_ADV:
            break;
        case ARR_PLAYING_ADV:
            break;
        case PAUSE_ADV:
            break;
        default:
            break;
        }
    }

}
