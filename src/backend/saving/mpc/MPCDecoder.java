package backend.saving.mpc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import backend.saving.Decoder;
import backend.songs.Accidental;
import backend.songs.MuteModifier;
import backend.songs.Note;
import backend.songs.NoteLine;
import backend.songs.Song;
import backend.songs.TimeSignature;
import gui.SMPInstrument;
import gui.Values;
import utilities.ExceptionUtils;

/**
 * Decodes Mario Paint Composer songs into Super Mario Paint- readable songs.
 *
 * @author RehdBlob
 * @since 2012.09.01
 */
public class MPCDecoder implements Decoder<Song, IOException> {

    /**
     * Opens a file and decodes the Mario Paint Composer song data from it,
     * changing it into a Super Mario Paint sequence.
     *
     * @param f
     *            A File, that supposedly contains Mario Paint Composer song
     *            data.
     * @return An <code>StaffSequence</code> that has been converted from the
     *         Mario Paint Composer song.
     * @throws IOException
     *             IF some error occurs during the decoding or parsing process.
     */
    public Song decode(File f) throws IOException {
    	try {
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
    	} catch (IOException ioe) {
    		throw ioe;
    	} catch (Exception e) {
    		throw new IOException("Error while trying to decode the MPC file!", ExceptionUtils.maskIfNeeded(e));
    	}
    }

    /**
     * Decodes a Mario Paint Composer song into an SMP-readable format. Uses
     * <code>TextUtil</code> from <code>MPCTxtTools</code>.
     *
     * @param in
     *            The input String that contains (supposedly) Mario Paint
     *            Composer song file data.
     * @throws IOException
     *             If someone tries to feed this method an invalid text file.
     */
    private Song decode(String in) throws IOException {
        if (in == null || in.isEmpty() || in.indexOf('*') == -1) {
            throw new IOException("Invalid Text File.");
        }
        
        try {
        	List<String> everything = MPCUtils.chop(MPCUtils.clean(in));
        	String timeSig = in.substring(0, in.indexOf('*'));
        	String tempo = in.substring(in.indexOf('%') + 1);
        	return populateSequence(timeSig, everything, tempo);
        } catch (IOException ioe) {
        	throw ioe;
        } catch (Exception e) {
        	throw new IOException("Error while decoding the MPC file!", ExceptionUtils.maskIfNeeded(e));
        }
    }

    /**
     * Parses the notes of a processed note line given its instruments.
     * 
     * @param sl The note line to deposit the parsing results into
     * @param inst The list of instruments
     */
    private void parseNotes(NoteLine sl, List<String> inst) throws IOException {
    	try {
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
	                    pos = MPCUtils.parsePosition(note.charAt(1));
	                    acc = MPCUtils.parseAccidental(note.charAt(2));
	                }
	            } else if (note.length() == 2) {
	                pos = MPCUtils.parsePosition(note.charAt(1));
	            }
	            
	            Note sn = new Note(in, pos, acc, mod);
	            sl.getNotes().add(sn);
	        }
    	} catch (Exception e) {
    		throw new IOException("Error while parsing the MPC note line!", ExceptionUtils.maskIfNeeded(e));
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
    private Song populateSequence(String timeSig, List<String> songData, String tempo) throws IOException {
    	try {
	        List<NoteLine> lines = new ArrayList<>(Values.LINES_PER_MPC_SONG);
	
	        for (String s : songData) {
	            NoteLine sl = new NoteLine();
	            if (s.length() <= 1) {
	                lines.add(sl);
	                continue;
	            }
	
	            List<String> inst = MPCUtils.dice(s);
	            int vol = MPCUtils.parseVolume(s.charAt(s.length() - 2));
	            
	            parseNotes(sl, inst);
	            
	            sl.setVolume(vol);
	            lines.add(sl);
	        }
	
	        Song song = new Song(lines);
	        song.setTempo(Double.parseDouble(tempo));
	        
	        // Ensure the passed time signature is used in the loaded song.
	        song.setTimeSignature(TimeSignature.valueOf(timeSig));
	        
	        return song;
    	} catch (IOException ioe) {
    		throw ioe;
    	} catch (Exception e) {
    		throw new IOException("Error while building the MPC sequence!", ExceptionUtils.maskIfNeeded(e));
    	}
    }

}
