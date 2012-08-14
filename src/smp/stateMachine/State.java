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
	
	EDITING,
	SONG_PLAYING,
	ARR_PLAYING,
	PAUSE,
	MENU_OPEN,
	EDITING_ADV,
	SONG_PLAYING_ADV,
	ARR_PLAYING_ADV,
	PAUSE_ADV;
	
}
