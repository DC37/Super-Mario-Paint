package backend.saving;

import java.io.File;

public interface Decoder<T, E extends Exception> {
    
    T decode(File in) throws E;
    
}
