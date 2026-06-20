package backend.saving.smp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import backend.saving.Decoder;
import backend.songs.StaffArrangement;

public class SMPArrangementDecoder implements Decoder<StaffArrangement> {

	/**
	 * Loads an arrangement from the file specified.
	 *
	 * @param inputFile
	 *            The file to load from.
	 * @return A loaded arrangement file. The format is StaffArrangement.
	 */
	public StaffArrangement decode(File inputFile) throws IOException, FileNotFoundException {
		FileInputStream f_in = new FileInputStream(inputFile);
		Scanner sc = new Scanner(f_in);
		ArrayList<String> read = new ArrayList<String>();
		while (sc.hasNext()) {
			read.add(sc.nextLine());
		}
		sc.close();
		StaffArrangement loaded = parseArrText(read);
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
		loaded.getTheSequenceNames().addAll(names);
		loaded.setTheSequenceFiles(files);
		return loaded;
	}

}
