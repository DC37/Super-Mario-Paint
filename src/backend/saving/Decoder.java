package backend.saving;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public interface Decoder<T> {
    
    T decode(File in) throws ParseException, IOException;
    
}
