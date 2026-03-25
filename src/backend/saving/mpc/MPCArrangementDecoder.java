package backend.saving.mpc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import backend.saving.Decoder;
import backend.songs.StaffArrangement;
import backend.songs.StaffSequence;

public class MPCArrangementDecoder implements Decoder<StaffArrangement> {

    /**
     * Opens a file and decodes Mario Paint Composer arrangement data from it.
     *
     * @param f
     *            The input file that we are reading from.
     * @return A decoded StaffArrangement, if successful.
     * @throws ParseException
     *             If the file is not the proper format.
     * @throws IOException
     *             If the file is not readable.
     */
    public StaffArrangement decode(File f)
            throws ParseException, IOException {
        BufferedReader bf = new BufferedReader(new FileReader(f));
        String line = "";
        String str = "";
        while (true) {
            line = bf.readLine();
            if (line == null)
                break;
            str += line;
            str += "\n";
        }
        bf.close();
        return parseFiles(str, f);
    }

    /**
     * Reads a string that is (hopefully) a list of files for the arranger to
     * read.
     *
     * @param str
     *            The text from a Mario Paint Composer arranger file.
     * @param inputFile
     *            The location of the arrangement file.
     * @return A StaffArrangement (if successful).
     * @throws ParseException
     *             If the file is the wrong format.
     * @throws IOException
     *             If something goes wrong while attempting to read the files.
     */
    private StaffArrangement parseFiles(String str, File inputFile)
            throws ParseException, IOException {
        if (str.isEmpty() || str == null) {
            throw new ParseException("Invalid Arr File.", 0);
        }
        StaffArrangement theArr = new StaffArrangement();

        for (String s : str.split("\n")) {
        	String sp = s;
            String st = inputFile.getParent() + File.separatorChar + sp + "]MarioPaint.txt";
            File f = new File(st);
            StaffSequence seq = Decoder.MPC_SEQUENCE_DECODER.decode(f);
            theArr.add(seq, f);
            theArr.getTheSequenceNames().add(sp);
        }
        return theArr;
    }

}
