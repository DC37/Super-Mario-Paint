package backend.songs;

import java.util.function.Consumer;

public interface SongProperties {

    double getTempo();
    void setTempo(double tempo);
    
    boolean[] getNoteExtensions();
    void setNoteExtensions(boolean[] noteExtensions);
    void setNoteExtensions(boolean[] noteExtensions, Consumer<boolean[]> fnOnNoteExtensionsSet);
    
    TimeSignature getTimeSignature();
    void setTimeSignature(TimeSignature timeSignature);
    
    String getSoundset();
    void setSoundset(String soundset);
    
}
