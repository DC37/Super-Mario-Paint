package backend.saving;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

import backend.songs.Note;
import backend.songs.Song;
import backend.songs.TimeSignature;
import gui.Values;
import lombok.extern.slf4j.Slf4j;
import utilities.DataTypeUtils;

@Slf4j
public class FileService {

	private static final String LINE_BREAK = "\r\n";
    
    private FileService() {}
    
    public static boolean trySaveSong(File f, Song song) {
        boolean successful = true;
        
        try (FileOutputStream fos = new FileOutputStream(f)) {
            
            PrintStream pr = new PrintStream(fos);
            
            TimeSignature t = Optional
                    .ofNullable(song.getTimeSignature())
                    .orElse(TimeSignature.FOUR_FOUR);
            
            pr.printf("TEMPO: %f, ", song.getTempo());
            pr.printf("EXT: %d, ", DataTypeUtils.packBits(song.getNoteExtensions()));
            pr.printf("TIME: %s, ", t);
            pr.printf("SOUNDSET: %s%s", song.getSoundset(), LINE_BREAK);
            
            for (int i = 0; i < song.getLength(); i++) {
                if (song.getLine(i).getNotes().isEmpty()) {
                    continue;
                }
                
                pr.print("" + (i / t.top() + 1) + ":" + (i % t.top()) + ",");
                
                List<Note> line = song.getLine(i).getNotes();
                for (int j = 0; j < line.size(); j++) {
                    pr.print(noteToString(line.get(j)) + ",");
                }
                
                pr.printf("VOL: %d%s", song.getLine(i).getVolume(), LINE_BREAK);
            }
            
            pr.close();
            
        } catch (IOException | IllegalArgumentException e) {
            successful = false;
            log.error("Error while trying to save the song!", e);
        }
        
        return successful;
    }
    
    private static String noteToString(Note note) {
        String instName = note.getInstrument().toString();
        String noteName = Values.getNoteName(note.getVerticalPosition());
        String noteAcc = note.getAccidental().getToken();
        String muteName = note.getMuteModifier().getToken();
        
        return String.format("%s %s%s%s", instName, noteName, noteAcc, muteName);
    }
	
}
