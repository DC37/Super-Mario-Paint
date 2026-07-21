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
	
}
