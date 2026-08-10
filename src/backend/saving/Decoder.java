package backend.saving;

import java.io.File;
import java.io.IOException;

public interface Decoder<T> {
    
    T decode(File in) throws IOException;
    
}
