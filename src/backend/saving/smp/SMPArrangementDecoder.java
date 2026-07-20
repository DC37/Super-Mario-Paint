package backend.saving.smp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import backend.saving.Decoder;
import backend.songs.Arrangement;
import backend.songs.Song;

public class SMPArrangementDecoder implements Decoder<Arrangement> {

    /**
     * Loads an arrangement from the file specified.
     *
     * @param inputFile
     *            The file to load from.
     * @return A loaded arrangement file. The format is StaffArrangement.
     */
    public Arrangement decode(File inputFile) throws IOException, ParseException {
    	String basePath = inputFile.getParent() + File.separatorChar;
        FileInputStream fIn = new FileInputStream(inputFile);
        Scanner sc = new Scanner(fIn);
        List<String> read = new ArrayList<>();
        while (sc.hasNext()) {
            read.add(sc.nextLine());
        }
        sc.close();
        Arrangement loaded = parseArrText(basePath, read);
        
        String inputFileName = inputFile.getName();
        loaded.setTitle(inputFileName.substring(0, inputFileName.lastIndexOf('.')));
        
        fIn.close();
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
    private static Arrangement parseArrText(String basePath, List<String> read) throws ParseException, IOException {
        Arrangement loaded = new Arrangement();
        File f = null;
		List<Song> seqs = new ArrayList<>();
        for (String s : read) {
            f = new File(basePath + s + ".txt");
			seqs.add(Decoder.SMP_SEQUENCE_DECODER.decode(f));
        }
		loaded.getSequences().addAll(seqs);
        return loaded;
    }

}
