package utilities;

import org.apache.commons.lang3.function.FailableFunction;

/**
 * Utility class for data type operations.
 * 
 * @author Aura Lesse Programmer
 */
public class DataTypeUtils {

	private DataTypeUtils() {}
	
	/**
     * Executes a {@link FailableFunction} and throws
     * an unchecked exception if it fails.
     * 
     * @param <A> Input type of the function.
     * @param <B> Output type of the function.
     * @param <E> Checked exception thrown by the function.
     * @param fnOperation The function to execute.
     * @param input The input to provide to the function.
     * @return The result of the function execution.
     * @throws {@link IllegalStateException} if the function fails.
     */
	public static <A, B, E extends Exception> B rethrowAsUnchecked(
			FailableFunction<A, B, E> fnOperation, A input) {
		
		try {
			return fnOperation.apply(input);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Unpacks a long integer into a boolean array.
	 *
	 * @param parseLong The long integer to unpack.
	 * @param size The number of bits to unpack, counted
	 *             from the least significant.
	 * @return A boolean array that maps each bit of the
	 *         long integer as a separate boolean.
	 */
	public static boolean[] unpackBits(long parseLong, int size) {
	    boolean[] loaded = new boolean[size];
	    for (int i = 0; i < size; i++) {
	        loaded[i] = ((1 << i) & parseLong) != 0;
	    }
	    return loaded;
	}

	/**
	 * Packs a boolean array into a long integer.
	 *
	 * @param parseBool The boolean array to pack.
	 * @return A long integer that is a bit-field
	 *         representing the boolean array.
	 */
	public static long packBits(boolean[] parseBool) {
	    long parsed = 0;
	    for (int i = 0; i < parseBool.length; i++) {
	        if (parseBool[i]) {
	            parsed |= (1 << i);
	        }
	    }
	    return parsed;
	}
	
}
