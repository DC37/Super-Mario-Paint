package smp.stateMachine;

/**
 * This class defines the states that the
 * state machine references. Listeners may also check
 * these states to determine whether they are allowed
 * to react to events.
 * @author RehdBlob
 * @since 2012.08.12
 */
public enum ProgramState {

    /**
     * This is the default state of the program, which allows
     * the staff to be edited, moved, etc.
     */
    EDITING,

    /**
     * This state denotes that we are currently editing an arrangement file.
     */
    ARR_EDITING,

    /**
     * This state denotes that we are currently playing a song.
     */
    SONG_PLAYING,

    /**
     * Just like the SONG_PLAYING state, except that we are now playiing an
     * arrangement.
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
