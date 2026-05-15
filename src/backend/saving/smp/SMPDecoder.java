package backend.saving.smp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import backend.saving.Decoder;
import backend.songs.StaffNote;
import backend.songs.StaffNoteLine;
import backend.songs.StaffSequence;
import backend.songs.TimeSignature;
import gui.Utilities;
import gui.Values;

public class SMPDecoder implements Decoder<StaffSequence> {

	/**
	 * Loads a song from the file specified.
	 *
	 * @param inputFile
	 *            The file to load from.
	 * @return A loaded song file. The format is a StaffSequence.
	 */
	public StaffSequence decode(File inputFile)
			throws FileNotFoundException, IOException {
		FileInputStream f_in = new FileInputStream(inputFile);
		Scanner sc = new Scanner(f_in);
		ArrayList<String> read = new ArrayList<String>();
		while (sc.hasNext()) {
			read.add(sc.nextLine());
		}
		sc.close();
		StaffSequence loaded = parseText(read);
		f_in.close();
		if (loaded == null) {
			throw new NullPointerException();
		}
		return loaded;
	}

	/**
	 * Parses a bunch of text from a save file and makes a
	 * <code>StaffSequence</code> out of it.
	 *
	 * @param read
	 *            <code>ArrayList</code> of notes and parameters.
	 * @return Hopefully, a decoded <code>StaffSequence</code>
	 */
	private StaffSequence parseText(ArrayList<String> read) {
		List<StaffNoteLine> lines = new ArrayList<>();
		double tempo = Values.DEFAULT_TEMPO;
		boolean[] noteExtensions = new boolean[Values.NUMINSTRUMENTS];
		TimeSignature timeSignature = Values.DEFAULT_TIME_SIGNATURE;
		String soundset = Values.DEFAULT_SOUNDFONT;
		
		for (String s : read) {
			if (s.contains("TEMPO") || s.contains("EXT") || s.contains("TIME") || s.contains("SOUNDSET")) {
				String[] sp = s.split(",");
				for (String spl : sp) {
					String num = spl.substring(spl.indexOf(":") + 1);
					if (spl.contains("TEMPO")) {
						tempo = Double.parseDouble(num.trim());
					} else if (spl.contains("EXT")) {
						noteExtensions = Utilities.boolFromLong(Long
								.parseLong(num.trim()));
						swap15And16(noteExtensions);
					} else if (spl.contains("TIME")) {
						timeSignature = TimeSignature.valueOf(num.trim());
					} else if (spl.contains("SOUNDSET")) {
						soundset = num.trim();
					}
				}
			} else {
				String[] sp = s.split(",");
				int lineNum = 0;
				StaffNoteLine st = new StaffNoteLine();
				for (String spl : sp) {
					if (spl.contains(":") && !spl.contains("VOL")) {
						String[] meas = spl.split(":");
						if (meas.length != 2) {
							continue;
						}
						lineNum = (Integer.parseInt(meas[0]) - 1)
								* timeSignature.top()
								+ Integer.parseInt(meas[1]);
					} else {
						if (spl.contains("VOL")) {
							st.setVolume(Integer.parseInt(spl.substring(
									spl.indexOf(":") + 1).trim()));
						} else {
							try {
								st.getNotes().add(StaffNote.valueOf(spl));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				if (lineNum < lines.size()) {
					lines.set(lineNum, st);
					
				} else {
					while (lines.size() < lineNum) {
						lines.addLast(new StaffNoteLine());
					}
					
					lines.addLast(st);
				}
			}
		}

		StaffSequence loaded = new StaffSequence(lines);
		loaded.normalize();
		loaded.setTempo(tempo);
		loaded.setNoteExtensions(noteExtensions);
		loaded.setTimeSignature(timeSignature);
		loaded.setSoundset(soundset);
		
		return loaded;
	}
	
	// Coin and piranha used to be swapped, so we unswap the note extensions
	// found in sequences files to conform with existing files
	private void swap15And16(boolean[] exts) {
		boolean ext15 = exts[15];
		exts[15] = exts[16];
		exts[16] = ext15;
	}

}
