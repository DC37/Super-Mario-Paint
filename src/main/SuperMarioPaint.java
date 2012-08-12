package main;

import smp.MPWindow;

/**
 * Super Mario Paint, aka. MPC3.0 or MPC4.0 depending on
 * how you view Advanced Mario Sequencer.
 * Mario Paint SNES (1992)
 * MarioSequencer (2002)
 * Mario Paint Composer 1.0 (2007)
 * Mario Paint Composer 2.0 (2008)
 * Advanced Mario Sequencer (2009)
 * Super Mario Paint (2012-2013?)
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
