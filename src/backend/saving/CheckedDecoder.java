package backend.saving;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

/**
 * Parsers whose implementation does not throw {@link ParseException}.
 */
public interface CheckedDecoder<T> extends Decoder<T> {

	@Override
    T decode(File in) throws IOException;
	
	/**
     * Try several parsers in sequence until one succeeds.
     * 
     * @param <T> the generic return type common to all parsers
     * @param parsers the parsers to try in sequence
     * @return Either the value returned by the first parser that succeeds, or empty
     */
	@SafeVarargs
	static <T> CheckedDecoder<Optional<T>> of(Decoder<? extends T>... parsers) {
		return new CheckedDecoder<>() {
            
            @Override
            public Optional<T> decode(File in) throws IOException {
                for (Decoder<? extends T> p : parsers) {
                    try {
                        T result = p.decode(in);
                        return Optional.of(result);
                    } catch (ParseException e) {
                        // Continue.
                    }
                }
                
                return Optional.empty();
            }
            
        };
	}
	
}
