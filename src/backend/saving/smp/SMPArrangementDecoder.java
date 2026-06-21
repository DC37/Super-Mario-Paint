package backend.saving.smp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
		List<String> read = new ArrayList<>();
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
	private static StaffArrangement parseArrText(List<String> read) {
		StaffArrangement loaded = new StaffArrangement();
		File f = null;
		List<File> files = new ArrayList<>();
		List<String> names = new ArrayList<>();
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
