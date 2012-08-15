package smp;

import java.awt.BorderLayout;

import smp.components.MPWindow;
import smp.components.instrumentLine.InstrumentPanel;
import smp.components.staff.Staff;
import smp.components.staff.StaffFrame;

/**
 * Super Mario Paint
 * Based on the old SNES game from 1992, Mario Paint
 * Inspired by:<br>
 * MarioSequencer (2002) <br>
 * TrioSequencer <br>
 * Robby Mulvany's Mario Paint Composer 1.0 / 2.0 (2007-2008) <br>
 * FordPrefect's Advanced Mario Sequencer (2009) <br>
 * @since 2012.08.07
 * @author RehdBlob
 * @version 1.00
 */
public class SuperMarioPaint {

	static MPWindow mp;
	
	/**
	 * Initializes an ImageLoader object and a 
	 * SoundfontLoader as Threads. These will initialize
	 * the necessary resources in memory such that they
	 * will be much easier to access later on. Hopefully
	 * most people have >30MB of RAM.
	 * @param args As per a main class.
	 */
	public static void main(String[] args) {
		Thread imgLd = new Thread(new ImageLoader());
		Thread sfLd = new Thread(new SoundfontLoader());
		imgLd.start();
		sfLd.start();
		while(imgLd.isAlive() || sfLd.isAlive())
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		mp = new MPWindow();
		testThings();
		mp.setVisible(true);
	}
	
	/**
	 * Debug method for GUI.
	 */
	public static void testThings() {
		StaffFrame x = new StaffFrame(0, 0);
		Staff y = new Staff(4, 4);
		InstrumentPanel z = new InstrumentPanel(0, 0);
		//mp.add(x, BorderLayout.CENTER);
		//x.add(y, BorderLayout.CENTER);
		mp.add(z, BorderLayout.CENTER);
	}

}
