package backend.saving.smp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import backend.saving.Decoder;
import backend.songs.Accidental;
import backend.songs.MuteModifier;
import backend.songs.Note;
import backend.songs.NoteLine;
import backend.songs.Song;
import backend.songs.SongProperties;
import backend.songs.TimeSignature;
import gui.SMPInstrument;
import gui.Values;
import utilities.CollectionUtils;
import utilities.DataTypeUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SMPDecoder implements Decoder<Song> {
    
    /**
     * Loads a song from the file specified.
     *
     * @param inputFile
     *            The file to load from.
     * @return A loaded song file. The format is a StaffSequence.
     */
    public Song decode(File inputFile) throws IOException {
    	try (
    			FileInputStream fIn = new FileInputStream(inputFile);
    			Scanner sc = new Scanner(fIn);
    	) {
	        List<String> read = new ArrayList<>();
	        while (sc.hasNext()) {
	            read.add(sc.nextLine());
	        }
	        
	        Song loaded = parseText(read);
	        
	        String fname = inputFile.getName();
	        loaded.setTitle(fname.substring(0, fname.lastIndexOf('.')));
	        
	        return loaded;
    	} catch (IOException ioe) {
    		throw ioe;
    	} catch (Exception e) {
    		throw new IOException("Error while trying to decode the SMP file!", e);
    	}
    }

    /**
     * Initializes a {@link SongProperties} with the values read from the save file.
     * 
     * @param props The {@link SongProperties} to initialize
     * @param sp The list of parameters
     */
    private void initializeSongProperties(SongProperties props, String[] sp) throws IOException {
        try {
	    	for (String spl : sp) {
	            String num = spl.substring(spl.indexOf(":") + 1);
	            if (spl.contains("TEMPO")) {
	                props.setTempo(
	                        Double.parseDouble(num.trim()));
	            } else if (spl.contains("EXT")) {
	                // Coin and piranha used to be swapped, so we unswap the note extensions
	                // found in sequences files to conform with existing files
	                props.setNoteExtensions(
	                        DataTypeUtils.unpackBits(
	                        		Long.parseLong(num.trim()), Values.NUM_INSTRUMENTS),
	                        exts -> CollectionUtils.swapItems(exts, 15, 16));
	            } else if (spl.contains("TIME")) {
	                props.setTimeSignature(
	                        TimeSignature.valueOf(num.trim()));
	            } else if (spl.contains("SOUNDSET")) {
	                props.setSoundset(
	                        num.trim());
	            }
	        }
        } catch (Exception e) {
        	throw new IOException("Error while parsing SMP song properties!", e);
        }
    }
    
    /**
     * Initializes the lines of a {@link Song}.
     * 
     * @param lines The lines container to be initialized.
     * @param sp The list of parameters
     * @param timeSignature The time signature to use.
     */
    private void initializeSongLines(List<NoteLine> lines, String[] sp,
    		TimeSignature timeSignature) throws IOException {
    	
    	try {
    		int lineNum = 0;
	        NoteLine st = new NoteLine();
	        
	        for (String spl : sp) {
	            if (spl.contains(":") && !spl.contains("VOL")) {
	                String[] meas = spl.split(":");
	                if (meas.length != 2) {
	                    continue;
	                }
	                lineNum = (Integer.parseInt(meas[0]) - 1)
	                        * timeSignature.top()
	                        + Integer.parseInt(meas[1]);
	                
	            } else if (spl.contains("VOL")) {
	                st.setVolume(Integer.parseInt(spl.substring(
	                        spl.indexOf(":") + 1).trim()));
	            } else {
	            	st.getNotes().add(parseNote(spl));
	            }
	        }
	        
	        CollectionUtils.addFillerThenElement(lines, st, lineNum, NoteLine::new);
    	} catch (IOException ioe) {
    		throw ioe;
    	} catch (Exception e) {
    		throw new IOException("Error while parsing SMP song lines!", e);
    	}
    }
    
    /**
     * Parses a bunch of text from a save file and makes a
     * <code>Song</code> out of it.
     *
     * @param read
     *            <code>List</code> of notes and parameters.
     * @return Hopefully, a decoded <code>Song</code>
     */
    private Song parseText(List<String> read) throws IOException {
        List<NoteLine> lines = new ArrayList<>();
        SongProperties songProps = new SMPSongProperties();
        
        for (String s : read) {
            String[] sp = s.split(",");
            
            if (CollectionUtils.containsAny(s, "TEMPO", "EXT", "TIME", "SOUNDSET")) {
                initializeSongProperties(songProps, sp);
            } else {
                initializeSongLines(lines, sp, songProps.getTimeSignature());
            }
        }

        return new Song(lines, songProps);
    }

    private static Note parseNote(String spl) throws IOException {
    	SMPInstrument inst;
        int vertPos = -1;
        Accidental accidental;
        MuteModifier muteMod;
        
        String[] sp = spl.split(" ");
        if (sp.length != 2) {
        	throw new IOException("Invalid note");
        }
        
        inst = SMPInstrument.valueOf(sp[0]);
        for (int i = 0; i < Values.getNotes().size(); i++) {
            if (sp[1].contains(Values.getNotes().get(i).getName())) {
                vertPos = i;
            }
        }
        
        int modFieldLen = sp[1].length();
        
        accidental = Accidental.NATURAL;
        if (modFieldLen == 3 || modFieldLen == 5) {
        	String accTok = String.format("%c", sp[1].charAt(2));
        	accidental = Accidental.ofToken(accTok);
        }
        
        muteMod = MuteModifier.REGULAR;
        if (modFieldLen == 4 || modFieldLen == 5) {
        	String mmTok = String.format("%c", sp[1].charAt(sp[1].length() - 1));
        	muteMod = MuteModifier.ofType(Integer.parseInt(mmTok));
        }
        
        return new Note(inst, vertPos, accidental, muteMod);
    }
    
}
