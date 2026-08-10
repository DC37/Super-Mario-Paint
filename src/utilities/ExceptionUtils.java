package utilities;

import java.util.Arrays;
import java.util.Optional;

public class ExceptionUtils {

	private static final String[] LOCAL_ROOTS = new String[] { "backend", "gui", "utilities" };
	
	private ExceptionUtils() {}
	
	public static Optional<StackTraceElement> getFirstLocalStacktraceElement(StackTraceElement[] trace) {
		return Arrays.asList(trace)
				.stream()
				.filter(t -> t.getClassName() != null
						&& CollectionUtils.startsWithAny(t.getClassName(), LOCAL_ROOTS))
				.findFirst();
	}
	
	@SuppressWarnings("unchecked")
	public static <E extends Exception> E maskIfNeeded(E exception) {
		if (exception instanceof NumberFormatException nfe)
			return (E) maskNFEInput(nfe);
		
		return exception;
	}
	
	private static NumberFormatException maskNFEInput(NumberFormatException nfe) {
		Optional<StackTraceElement> firstLocalElem = ExceptionUtils
				.getFirstLocalStacktraceElement(nfe.getStackTrace());
		
		String newDesc = firstLocalElem
				.map(ste -> String.format("For input string parsed at %s", ste))
				.orElse("At unspecified location.");
		
		return new NumberFormatException(newDesc);
	}
	
}
