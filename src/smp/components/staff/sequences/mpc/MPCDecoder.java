package smp.components.staff.sequences.mpc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import smp.components.InstrumentIndex;
import smp.components.Values;
import smp.components.staff.sequences.StaffArrangement;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.staff.sequences.StaffSequence;
import static smp.components.staff.sequences.mpc.TextUtil.*;

/**
 * Decodes Mario Paint Composer songs into Super Mario Paint- readable songs.
 *
 * @author RehdBlob
 * @since 2012.09.01
 */
public class MPCDecoder {

    /**
     * Opens a file and decodes the Mario Paint Composer song data from it,
     * changing it into a Super Mario Paint sequence.
     *
     * @param f
     *            A File, that supposedly contains Mario Paint Composer song
     *            data.
     * @return An <code>StaffSequence</code> that has been converted from the
     *         Mario Paint Composer song.
     * @throws ParseException
     *             If for some reason the parsing fails at some point in the
     *             conversion process.
     * @throws IOException
     *             IF some error occurs during the decoding process.
     */
    public static StaffSequence decode(File f) throws ParseException,
            IOException {
        BufferedReader bf = new BufferedReader(new FileReader(f));
        String line = "";
        String str = "";
        do {
            str += line;
            line = bf.readLine();
        } while (line != null);
        bf.close();
        return decode(str);
    }

    /**
     * Decodes a Mario Paint Composer song into an SMP-readable format. Uses
     * <code>TextUtil</code> from <code>MPCTxtTools</code>.
     *
     * @param in
     *            The input String that contains (supposedly) Mario Paint
     *            Composer song file data.
     * @throws ParseException
     *             If someone tries to feed this method an invalid text file.
     */
    public static StaffSequence decode(String in) throws ParseException {
        if (in.indexOf('*') == -1 || in.isEmpty() || in == null) {
            throw new ParseException("Invalid Text File.", 0);
        }
        ArrayList<String> everything = chop(clean(in));
        String timeSig = in.substring(0, in.indexOf('*'));
        String tempo = in.substring(in.indexOf('%') + 1);
        return populateSequence(timeSig, everything, tempo);
    }

    /**
     * Creates a new Super Mario Paint sequence from the input Mario Paint
     * Composer text data.
     *
     * @param timeSig
     *            The time signature of the Mario Paint Composer song.
     * @param songData
     *            The text data of the Mario Paint Composer song. This defines
     *            the notes and instruments on each note line.
     * @param tempo
     *            The tempo at which this should be played at.
     * @return A new <code>StaffSequence</code> that is to be loaded by the main
     *         program.
     */
    private static StaffSequence populateSequence(String timeSig,
            ArrayList<String> songData, String tempo) {
        StaffSequence song = new StaffSequence();
        song.getTheLines().clear();
        song.setTempo(Double.parseDouble(tempo));
        int accum = 0;
        for (String s : songData) {
            if (accum >= Values.LINES_PER_MPC_SONG)
                break;
            accum++;
            StaffNoteLine sl = new StaffNoteLine();
            if (s.length() <= 1) {
                song.addLine(sl);
                continue;
            }
            ArrayList<String> inst = dice(s);
            int vol = parseVolume(s.charAt(s.length() - 2));
            for (String note : inst) {
                if (note.isEmpty())
                    continue;
                InstrumentIndex in = MPCInstrumentIndex.valueOf(note.charAt(0));
                int pos = 0;
                int acc = 0;
                if (note.length() == 3) {
                    if (note.substring(1).equals("17")) {
                        StaffNote mn = new StaffNote(in, pos, acc);
                        mn.setMuteNote(2);
                        sl.add(mn);
                        continue;
                    } else {
                        pos = parsePosition(note.charAt(1));
                        acc = parseAccidental(note.charAt(2));
                    }
                } else if (note.length() == 2) {
                    pos = parsePosition(note.charAt(1));
                    acc = 0;
                }
                StaffNote sn = new StaffNote(in, pos, acc);
                sl.add(sn);
            }
            sl.setVolume(vol);
            song.addLine(sl);
        }
        while (song.getTheLines().size() < Values.LINES_PER_MPC_SONG) {
            song.getTheLines().add(new StaffNoteLine());
        }
        return song;
    }

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
    public static StaffArrangement decodeArrangement(File f)
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
    private static StaffArrangement parseFiles(String str, File inputFile)
            throws ParseException, IOException {
        if (str.isEmpty() || str == null) {
            throw new ParseException("Invalid Arr File.", 0);
        }
        StaffArrangement theArr = new StaffArrangement();

        for (String s : str.split("\n")) {
            s = s.replace("/", "_");
            String st = inputFile.getParent() + "\\" + s + "]MarioPaint.txt";
            File f = new File(st);
            StaffSequence seq = decode(f);
            theArr.add(seq, f);
            theArr.getTheSequenceNames().add(s);
        }
        return theArr;
    }

}
