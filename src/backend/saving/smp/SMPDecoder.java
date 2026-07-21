package backend.saving.smp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backend.saving.Decoder;
import backend.songs.Accidental;
import backend.songs.MuteModifier;
import backend.songs.Note;
import backend.songs.NoteLine;
import backend.songs.Song;
import backend.songs.SongProperties;
import backend.songs.TimeSignature;
import gui.SMPInstrument;
import gui.Utilities;
import gui.Values;
import utilities.CollectionUtils;

public class SMPDecoder implements Decoder<Song> {
    
    private static final Logger log = LoggerFactory.getLogger(SMPDecoder.class);

    /**
     * Loads a song from the file specified.
     *
     * @param inputFile
     *            The file to load from.
     * @return A loaded song file. The format is a StaffSequence.
     */
    public Song decode(File inputFile) throws IOException {
        FileInputStream fIn = new FileInputStream(inputFile);
        Scanner sc = new Scanner(fIn);
        List<String> read = new ArrayList<>();
        while (sc.hasNext()) {
            read.add(sc.nextLine());
        }
        sc.close();
        Song loaded = parseText(read);
        
        String fname = inputFile.getName();
        loaded.setTitle(fname.substring(0, fname.lastIndexOf('.')));
        
        fIn.close();

        return loaded;
    }

    /**
     * Initializes a {@link SongProperties} with the values read from the save file.
     * 
     * @param props The {@link SongProperties} to initialize
     * @param sp The list of parameters
     */
    private void initializeSongProperties(SongProperties props, String[] sp) {
        for (String spl : sp) {
            String num = spl.substring(spl.indexOf(":") + 1);
            if (spl.contains("TEMPO")) {
                props.setTempo(
                        Double.parseDouble(num.trim()));
            } else if (spl.contains("EXT")) {
                // Coin and piranha used to be swapped, so we unswap the note extensions
                // found in sequences files to conform with existing files
                props.setNoteExtensions(
                        Utilities.boolFromLong(Long.parseLong(num.trim())),
                        exts -> CollectionUtils.swapItems(exts, 15, 16));
            } else if (spl.contains("TIME")) {
                props.setTimeSignature(
                        TimeSignature.valueOf(num.trim()));
            } else if (spl.contains("SOUNDSET")) {
                props.setSoundset(
                        num.trim());
            }
        }
    }
    
    /**
     * Initializes the lines of a {@link Song}.
     * 
     * @param lines The lines container to be initialized.
     * @param sp The list of parameters
     * @param timeSignature The time signature to use.
     */
    private void initializeSongLines(List<NoteLine> lines, String[] sp, TimeSignature timeSignature) {
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
                try {
                    st.getNotes().add(parseNote(spl));
                } catch (ParseException e) {
                	log.error("Exception:", e);
                }
            }
        }
        
        CollectionUtils.addFillerThenElement(lines, st, lineNum, NoteLine::new);
    }
    
    /**
     * Parses a bunch of text from a save file and makes a
     * <code>Song</code> out of it.
     *
     * @param read
     *            <code>List</code> of notes and parameters.
     * @return Hopefully, a decoded <code>Song</code>
     */
    private Song parseText(List<String> read) {
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

    private static Note parseNote(String spl) throws ParseException {
    	SMPInstrument theInstrument;
        int verticalPosition = -1;
        Accidental accidental;
        MuteModifier muteMod;
        
        String[] sp = spl.split(" ");
        if (sp.length != 2) {
            throw new ParseException("Invalid note", 0);
        }
        theInstrument = SMPInstrument.valueOf(sp[0]);
        for (int i = 0; i < Values.STAFF_NOTES.length; i++) {
            if (sp[1].contains(Values.STAFF_NOTE_NAMES[i])) {
                verticalPosition = i;
            }
        }
        switch (sp[1].length()) {
        case 3:
            accidental = decodeAccidental(sp[1].charAt(2));
            muteMod = MuteModifier.REGULAR;
            break;
        case 4: 
            accidental = Accidental.NATURAL;
            muteMod = muteModifierFromInt(Integer.parseInt("" + sp[1].charAt(sp[1].length() - 1)));
            break;
        case 5:
            accidental = decodeAccidental(sp[1].charAt(2));
            muteMod = muteModifierFromInt(Integer.parseInt("" + sp[1].charAt(sp[1].length() - 1)));
            break;
        case 2:
        default:
            accidental = Accidental.NATURAL;
            muteMod = MuteModifier.REGULAR;
            break;
        }
        
        return new Note(theInstrument, verticalPosition, accidental, muteMod);
    }
    
    private static MuteModifier muteModifierFromInt(int v) {
        switch (v) {
        case 0:
            return MuteModifier.REGULAR;
        case 1:
            return MuteModifier.MUTE_THIS_PITCH;
        case 2:
            return MuteModifier.MUTE_THIS_INST;
        default:
            throw new IllegalArgumentException("No mute modifier associated to value " + v);
        }
    }

    /**
     * Given character <code>c</code>, decode it as a doublesharp, sharp, flat,
     * or doubleflat.
     *
     * @param c
     *            The character to decode.
     * @return The accidental to set.
     */
    private static Accidental decodeAccidental(char c) {
        switch (c) {
        case 'X':
            return Accidental.DOUBLE_SHARP;
        case '#':
            return Accidental.SHARP;
        case 'b':
            return Accidental.FLAT;
        case 'B':
            return Accidental.DOUBLE_FLAT;
        default:
            return Accidental.NATURAL;
        }
    }
    
}
