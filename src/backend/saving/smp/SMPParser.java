package backend.saving.smp;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import backend.saving.mpc.MPCDecoder;
import backend.songs.StaffArrangement;
import backend.songs.StaffNote;
import backend.songs.StaffNoteLine;
import backend.songs.StaffSequence;
import gui.Utilities;

public class SMPParser {

	/**
	 * Loads a song from the file specified.
	 *
	 * @param inputFile
	 *            The file to load from.
	 * @return A loaded song file. The format is a StaffSequence.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static StaffSequence parseSong(File inputFile)
			throws FileNotFoundException, IOException, NullPointerException {
		FileInputStream f_in = new FileInputStream(inputFile);
		StaffSequence loaded = null;
		try {
			loaded = MPCDecoder.parseSong(inputFile);
		} catch (ParseException e1) {
			try {
				// Decode as object
				ObjectInputStream o_in = new ObjectInputStream(f_in);
				loaded = (StaffSequence) o_in.readObject();
				o_in.close();
			} catch (ClassNotFoundException | ClassCastException | EOFException
					| StreamCorruptedException e) {
				// If it's not an object, try using the human-readable option.
				f_in.close();
				f_in = new FileInputStream(inputFile);
				Scanner sc = new Scanner(f_in);
				ArrayList<String> read = new ArrayList<String>();
				while (sc.hasNext()) {
					read.add(sc.nextLine());
				}
				sc.close();
				loaded = parseText(read);
			}
		}
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
	private static StaffSequence parseText(ArrayList<String> read) {
		StaffSequence loaded = new StaffSequence();
		for (String s : read) {
			if (s.contains("TEMPO") || s.contains("EXT") || s.contains("TIME") || s.contains("SOUNDSET")) {
				String[] sp = s.split(",");
				for (String spl : sp) {
					String num = spl.substring(spl.indexOf(":") + 1);
					if (spl.contains("TEMPO")) {
						loaded.setTempo(Double.parseDouble(num.trim()));
					} else if (spl.contains("EXT")) {
						loaded.setNoteExtensions(Utilities.boolFromLong(Long
								.parseLong(num.trim())));
					} else if (spl.contains("TIME")) {
						loaded.setTimeSignature(num.trim());
					} else if (spl.contains("SOUNDSET")) {
						loaded.setSoundset(num.trim());
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
								* loaded.getTimeSignature().top()
								+ Integer.parseInt(meas[1]);
					} else {
						if (spl.contains("VOL")) {
							st.setVolume(Double.parseDouble(spl.substring(
									spl.indexOf(":") + 1).trim()));
						} else {
							try {
								st.add(new StaffNote(spl));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
				}

				if (lineNum >= loaded.getLength()) {
					loaded.resize(lineNum + 1);
				}
				loaded.setLine(lineNum, st);
			}
		}
		loaded.normalize();
		return loaded;
	}

	/**
	 * Loads an arrangement from the file specified.
	 *
	 * @param inputFile
	 *            The file to load from.
	 * @return A loaded arrangement file. The format is StaffArrangement.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static StaffArrangement parseArrangement(File inputFile)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		FileInputStream f_in = new FileInputStream(inputFile);
		StaffArrangement loaded = null;
		try {
			ObjectInputStream o_in = new ObjectInputStream(f_in);
			loaded = (StaffArrangement) o_in.readObject();
			o_in.close();
		} catch (ClassNotFoundException | ClassCastException | EOFException
				| StreamCorruptedException e) {
			// If it's not an object, try using the human-readable option.
			f_in.close();
			f_in = new FileInputStream(inputFile);
			Scanner sc = new Scanner(f_in);
			ArrayList<String> read = new ArrayList<String>();
			while (sc.hasNext()) {
				read.add(sc.nextLine());
			}
			sc.close();
			loaded = parseArrText(read);
		}
		f_in.close();
		return loaded;
	}

	/**
	 * Parses a bunch of text from a save file and makes a
	 * <code>StaffArrangement</code> out of it.
	 *
	 * @param read
	 *            <code>ArrayList</code> of filenames and paths.
	 * @return Hopefully, a decoded <code>StaffArrangement</code>
	 */
	private static StaffArrangement parseArrText(ArrayList<String> read) {
		StaffArrangement loaded = new StaffArrangement();
		File f = null;
		ArrayList<File> files = new ArrayList<File>();
		ArrayList<String> names = new ArrayList<String>();
		for (String s : read) {
			names.add(s);
			f = new File(s + ".txt");
			files.add(f);
		}
		loaded.setTheSequenceNames(names);
		loaded.setTheSequenceFiles(files);
		return loaded;
	}

}
