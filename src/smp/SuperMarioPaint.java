package smp;

import smp.components.MPWindow;

/**
 * Super Mario Paint
 * Based on the old SNES game from 1992, Mario Paint
 * Inspired by:<br>
 * Robby Mulvany's Mario Paint Composer 1.0 / 2.0 (2007-2008) <br>
 * FordPrefect's Advanced Mario Sequencer (2009) <br>
 * MarioSequencer (2002) <br>
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
