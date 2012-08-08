package main;

import smp.MPWindow;

/**
 * Super Mario Paint, aka. MPC3.0 or MPCv4.0 depending on
 * how you view Advanced Mario Sequencer
 * Based on the old SNES game from 1992, Mario Paint
 * Inspired by:<br>
 * Robby Mulvany's Mario Paint Composer 1.0 / 2.0 <br>
 * FordPrefect's Advanced Mario Sequencer <br>
 * MarioSequencer <br>
 * TrioSequencer <br>
 * @since 2012.08.07
 * @author RehdBlob
 * @version 1.00
 */
public class SuperMarioPaint {

	/**
	 * Initializes the MPWindow.
	 * @param args As per a main class.
	 */
	public static void main(String[] args) {
		MPWindow mp = new MPWindow();
		mp.setVisible(true);
	}

}
