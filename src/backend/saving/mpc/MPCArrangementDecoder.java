package backend.saving.mpc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import backend.saving.Decoder;
import backend.saving.SequenceDecoders;
import backend.songs.Arrangement;
import backend.songs.Song;

public class MPCArrangementDecoder implements Decoder<Arrangement, IOException> {

    /**
     * Opens a file and decodes Mario Paint Composer arrangement data from it.
     *
     * @param f
     *            The input file that we are reading from.
     * @return A decoded StaffArrangement, if successful.
     * @throws IOException
     *             If the file is malformed or not readable.
     */
    public Arrangement decode(File f) throws IOException {
        StringBuilder sb = new StringBuilder();
        
        try (
                FileReader fr = new FileReader(f);
                BufferedReader bf = new BufferedReader(fr)
        ) {
            String line;
            while ((line = bf.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        
        return parseFiles(sb.toString(), f);
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
     * @throws IOException
     *             If something goes wrong while attempting to read the files.
     */
    private Arrangement parseFiles(String str, File inputFile) throws IOException {
        if (str == null || str.isEmpty()) {
            throw new IOException("Invalid Arr File.");
        }
        
        Arrangement theArr = new Arrangement();
        
        String inputFileName = inputFile.getName();
        theArr.setTitle(inputFileName.substring(0, inputFileName.lastIndexOf(']')));

        for (String s : str.split("\n")) {
            String st = inputFile.getParent() + File.separatorChar + s + "]MarioPaint.txt";
            File f = new File(st);
            Song seq = SequenceDecoders.MPC.getDecoder().decode(f);
            theArr.getSongs().add(seq);
        }
        
        return theArr;
    }

}
