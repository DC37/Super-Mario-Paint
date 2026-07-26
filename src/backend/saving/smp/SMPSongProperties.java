package backend.saving.smp;

import java.util.function.Consumer;

import backend.songs.SongProperties;
import backend.songs.TimeSignature;
import gui.Values;

public class SMPSongProperties implements SongProperties {

    private double tempo = Values.DEFAULT_TEMPO;
    private boolean[] noteExtensions = new boolean[Values.NUM_INSTRUMENTS];
    private TimeSignature timeSignature = Values.DEFAULT_TIME_SIGNATURE;
    private String soundset = Values.DEFAULT_SOUNDFONT;
    
    @Override
    public double getTempo() {
        return tempo;
    }
    
    @Override
    public void setTempo(double tempo) {
        this.tempo = tempo;
    }
    
    @Override
    public boolean[] getNoteExtensions() {
        return noteExtensions;
    }
    
    @Override
    public void setNoteExtensions(boolean[] noteExtensions) {
        setNoteExtensions(noteExtensions, null);
    }
    
    @Override
    public void setNoteExtensions(boolean[] noteExtensions, Consumer<boolean[]> fnOnNoteExtensionsSet) {
        this.noteExtensions = noteExtensions;
        
        if (fnOnNoteExtensionsSet != null)
            fnOnNoteExtensionsSet.accept(noteExtensions);
    }
    
    @Override
    public TimeSignature getTimeSignature() {
        return timeSignature;
    }
    
    @Override
    public void setTimeSignature(TimeSignature timeSignature) {
        this.timeSignature = timeSignature;
    }
    
    @Override
    public String getSoundset() {
        return soundset;
    }
    
    @Override
    public void setSoundset(String soundset) {
        this.soundset = soundset;
    }
    
}
