package smp.stateMachine;

/**
 * This class defines the states that the
 * state machine references. Listeners may also check
 * these states to determine whether they are allowed
 * to react to events.
 * @author RehdBlob
 * @since 2012.08.12
 */
public enum State {

    /**
     * This is the default state of the program, which allows
     * the staff to be edited, moved, etc.
     */
    EDITING,

    /**
     * This state disables everything except for the instrument button line
     * and the stop and loop buttons.
     */
    SONG_PLAYING,

    /**
     * Just like the SONG_PLAYING state, this state disables many things, but
     * ARR_PLAYING also disables all of the arrangement functions.
     */
    ARR_PLAYING,

    /**
     * This state disables all buttons except for the play and the stop
     * buttons, since the song is paused in this case.
     */
    PAUSE,

    /**
     * Whenever a menu is open, all buttons are disabled except for those in
     * the menu.
     */
    MENU_OPEN,

    /**
     * Easter egg.
     */
    RPG;
}
