package backend.saving;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

import backend.CompositeException;

/**
 * Parsers whose implementation does not throw {@link ParseException}.
 */
public interface CheckedDecoder<T> extends Decoder<T, CompositeException> {

	@Override
    T decode(File in) throws CompositeException;
	
	/**
     * Try several parsers in sequence until one succeeds.
     * 
     * @param <T> the generic return type common to all parsers
     * @param parsers the parsers to try in sequence
     * @return Either the value returned by the first parser that succeeds, or empty
     */
	@SafeVarargs
	static <T> CheckedDecoder<Optional<T>> of(Decoder<? extends T, IOException>... parsers) {
		return new CheckedDecoder<>() {
            
            @Override
            public Optional<T> decode(File in) throws CompositeException {
            	CompositeException ce = new CompositeException();
            	
            	for (Decoder<? extends T, IOException> p : parsers) {
                    try {
                        T result = p.decode(in);
                        return Optional.of(result);
                    } catch (IOException e) {
                    	ce.addException(e, p.getClass().getSimpleName());
                    }
                }
                
                if (!ce.isEmpty())
                	throw ce;
                
                return Optional.empty();
            }
            
        };
	}
	
}
