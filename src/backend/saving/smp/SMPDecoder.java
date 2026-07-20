package backend.saving.smp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import backend.saving.Decoder;
import backend.songs.Accidental;
import backend.songs.MuteModifier;
import backend.songs.Note;
import backend.songs.NoteLine;
import backend.songs.Song;
import backend.songs.TimeSignature;
import gui.InstrumentIndex;
import gui.Utilities;
import gui.Values;

public class SMPDecoder implements Decoder<Song> {

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
     * Parses a bunch of text from a save file and makes a
     * <code>StaffSequence</code> out of it.
     *
     * @param read
     *            <code>ArrayList</code> of notes and parameters.
     * @return Hopefully, a decoded <code>StaffSequence</code>
     */
    private Song parseText(List<String> read) {
        List<NoteLine> lines = new ArrayList<>();
        double tempo = Values.DEFAULT_TEMPO;
        boolean[] noteExtensions = new boolean[Values.NUM_INSTRUMENTS];
        TimeSignature timeSignature = Values.DEFAULT_TIME_SIGNATURE;
        String soundset = Values.DEFAULT_SOUNDFONT;
        
        for (String s : read) {
            if (s.contains("TEMPO") || s.contains("EXT") || s.contains("TIME") || s.contains("SOUNDSET")) {
                String[] sp = s.split(",");
                for (String spl : sp) {
                    String num = spl.substring(spl.indexOf(":") + 1);
                    if (spl.contains("TEMPO")) {
                        tempo = Double.parseDouble(num.trim());
                    } else if (spl.contains("EXT")) {
                        noteExtensions = Utilities.boolFromLong(Long
                                .parseLong(num.trim()));
                        swap15And16(noteExtensions);
                    } else if (spl.contains("TIME")) {
                        timeSignature = TimeSignature.valueOf(num.trim());
                    } else if (spl.contains("SOUNDSET")) {
                        soundset = num.trim();
                    }
                }
            } else {
                String[] sp = s.split(",");
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
                    } else {
                        if (spl.contains("VOL")) {
                            st.setVolume(Integer.parseInt(spl.substring(
                                    spl.indexOf(":") + 1).trim()));
                        } else {
                            try {
                                st.getNotes().add(parseNote(spl));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                
                if (lineNum < lines.size()) {
                    lines.set(lineNum, st);
                    
                } else {
                    while (lines.size() < lineNum) {
                        lines.addLast(new NoteLine());
                    }
                    
                    lines.addLast(st);
                }
            }
        }

        Song loaded = new Song(lines);
        loaded.setTempo(tempo);
        loaded.setNoteExtensions(noteExtensions);
        loaded.setTimeSignature(timeSignature);
        loaded.setSoundset(soundset);
        
        return loaded;
    }
    
    // Coin and piranha used to be swapped, so we unswap the note extensions
    // found in sequences files to conform with existing files
    private void swap15And16(boolean[] exts) {
        boolean ext15 = exts[15];
        exts[15] = exts[16];
        exts[16] = ext15;
    }

    private static Note parseNote(String spl) throws ParseException {
        InstrumentIndex theInstrument;
        int verticalPosition = -1;
        Accidental accidental;
        MuteModifier muteMod;
        
        String[] sp = spl.split(" ");
        if (sp.length != 2) {
            throw new ParseException("Invalid note", 0);
        }
        theInstrument = InstrumentIndex.valueOf(sp[0]);
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
