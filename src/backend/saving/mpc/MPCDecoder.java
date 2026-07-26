package backend.saving.mpc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import backend.saving.Decoder;
import backend.songs.Accidental;
import backend.songs.MuteModifier;
import backend.songs.Note;
import backend.songs.NoteLine;
import backend.songs.Song;
import gui.SMPInstrument;
import gui.Values;

/**
 * Decodes Mario Paint Composer songs into Super Mario Paint- readable songs.
 *
 * @author RehdBlob
 * @since 2012.09.01
 */
public class MPCDecoder implements Decoder<Song> {

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
    public Song decode(File f) throws ParseException, IOException {
        StringBuilder sb = new StringBuilder();
        
        try (
                FileReader fr = new FileReader(f);
                BufferedReader bf = new BufferedReader(fr)
        ) {
            String line = "";
            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }
        }
        
        Song seq = decode(sb.toString());
        
        String fname = f.getName();
        seq.setTitle(fname.substring(0, fname.lastIndexOf('.')));
        
        return seq;
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
    private Song decode(String in) throws ParseException {
        if (in == null || in.isEmpty() || in.indexOf('*') == -1) {
            throw new ParseException("Invalid Text File.", 0);
        }
        List<String> everything = TextUtil.chop(TextUtil.clean(in));
        String timeSig = in.substring(0, in.indexOf('*'));
        String tempo = in.substring(in.indexOf('%') + 1);
        return populateSequence(timeSig, everything, tempo);
    }

    /**
     * Parses the notes of a processed note line given its instruments.
     * 
     * @param sl The note line to deposit the parsing results into
     * @param inst The list of instruments
     */
    private void parseNotes(NoteLine sl, List<String> inst) {
    	for (String note : inst) {
            if (note.isEmpty())
                continue;
            
            SMPInstrument in = MPCInstrumentIndex.valueOf(note.charAt(0));
            int pos = 0;
            Accidental acc = Accidental.NATURAL;
            MuteModifier mod = MuteModifier.REGULAR;
            
            if (note.length() == 3) {
                if (note.substring(1).equals("17")) {
                	mod = MuteModifier.MUTE_THIS_INST;
                } else {
                    pos = TextUtil.parsePosition(note.charAt(1));
                    acc = TextUtil.parseAccidental(note.charAt(2));
                }
            } else if (note.length() == 2) {
                pos = TextUtil.parsePosition(note.charAt(1));
            }
            
            Note sn = new Note(in, pos, acc, mod);
            sl.getNotes().add(sn);
        }
    }
    
    /**
     * Creates a new Super Mario Paint song from the
     * Mario Paint Composer text data input.
     *
     * @param timeSig
     *            The time signature of the Mario Paint Composer song.
     * @param songData
     *            The text data of the Mario Paint Composer song.
     *            This defines the notes and instruments on each note line.
     * @param tempo
     *            The tempo at which this should be played at.
     * @return A new <code>Song</code> that is to be loaded by the main
     *         program.
     */
    private Song populateSequence(String timeSig, List<String> songData, String tempo) {
    	
        List<NoteLine> lines = new ArrayList<>(Values.LINES_PER_MPC_SONG);

        for (String s : songData) {
            NoteLine sl = new NoteLine();
            if (s.length() <= 1) {
                lines.add(sl);
                continue;
            }

            List<String> inst = TextUtil.dice(s);
            int vol = TextUtil.parseVolume(s.charAt(s.length() - 2));
            
            parseNotes(sl, inst);
            
            sl.setVolume(vol);
            lines.add(sl);
        }

        Song song = new Song(lines);
        song.setTempo(Double.parseDouble(tempo));
        
        // TODO: Check if this is valid for MPC files.
        // song.setTimeSignature(TimeSignature.valueOf(timeSig));
        
        return song;
    }

}
