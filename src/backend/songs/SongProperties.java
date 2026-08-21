package backend.songs;

import java.util.function.UnaryOperator;

public interface SongProperties {

    double getTempo();
    void setTempo(double tempo);
    
    boolean[] getNoteExtensions();
    void setNoteExtensions(boolean[] noteExtensions);
    void setNoteExtensions(boolean[] noteExtensions, UnaryOperator<boolean[]> fnOnNoteExtensionsSet);
    
    TimeSignature getTimeSignature();
    void setTimeSignature(TimeSignature timeSignature);
    
    String getSoundset();
    void setSoundset(String soundset);
    
}
