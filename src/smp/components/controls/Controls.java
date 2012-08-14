package smp.components.controls;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import smp.stateMachine.State;

public class Controls extends JPanel {
	
	/**
	 * Genereated serial ID.
	 */
	private static final long serialVersionUID = -7128020032635313050L;
	
	private PlayButton play;
	private StopButton stop;
	private PauseButton pause;
	private LoopButton loop;
	private TimeSigButton timeSig;
	private SoundfontSelector selector;
	private MuteButton mute;
	private EraseButton erase;
	private TempoEntry tempo;
	private ScrollBar scroll;
	
	public Controls() {
		
	}
	
	public void changeState(State s) {
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
