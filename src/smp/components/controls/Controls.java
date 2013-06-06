package smp.components.controls;

import javafx.scene.image.ImageView;
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

    /** Starts the song. */
    public void play() {
        State s = StateMachine.getState();

    }

    /** Stops the song. */
    public void stop() {
        State s = StateMachine.getState();
    }

}
