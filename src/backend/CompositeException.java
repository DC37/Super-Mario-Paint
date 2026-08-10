package backend;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

public class CompositeException extends Exception {

	private static final long serialVersionUID = -5220628388593068814L;

	private final List<ExceptionLevelInfo<Exception>> exceptions = new ArrayList<>();
	
	public void addException(Exception e, int level, String details) {
		exceptions.add(new ExceptionLevelInfo<>(e, level, details));
	}
	
	public void addException(Exception e, int level) {
		addException(e, level, null);
	}
	
	public void addException(Exception e, String details) {
		addException(e, 0, details);
	}
	
	public void addException(Exception e) {
		addException(e, null);
	}
	
	public boolean isEmpty() {
		return exceptions.isEmpty();
	}
	
	private void traverse(Consumer<ExceptionLevelInfo<Exception>> fnProcess) {
		Deque<ExceptionLevelInfo<Exception>> q = new ArrayDeque<>(exceptions);
		
		while (!q.isEmpty()) {
			ExceptionLevelInfo<Exception> e = q.pop();
			
			fnProcess.accept(e);
			
			Optional.ofNullable(e)
					.map(ExceptionLevelInfo::getException)
					.map(Exception::getCause)
					.filter(Exception.class::isInstance)
					.map(Exception.class::cast)
					.ifPresent(c -> q.addFirst(new ExceptionLevelInfo<>(c, e.getLevel() + 1)));
		}
	}
	
	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder("\n");
		
		traverse(eli -> {
			sb.append(StringUtils.repeat("  ", eli.getLevel() + 1))
				.append("*");
			
			if (eli.getDetails() != null) {
				sb.append(" [").append(eli.getDetails()).append("]");
			}
			
			sb.append(" ")
				.append(eli.getException().getClass().getSimpleName())
				.append(": ")
				.append(eli.getException().getMessage())
				.append("\n");
		});
		
		if (!sb.isEmpty())
			sb.deleteCharAt(sb.length() - 1);
		
		return sb.toString();
	}
	
	private static class ExceptionLevelInfo<E extends Throwable> {
		
		private E exception;
		private int level;
		private String details;
		
		public ExceptionLevelInfo(E exception, int level, String details) {
			this.exception = exception;
			this.level = level;
			this.details = details;
		}
		
		public ExceptionLevelInfo(E exception, int level) {
			this(exception, level, null);
		}
		
		public E getException() {
			return exception;
		}
		
		public int getLevel() {
			return level;
		}
		
		public String getDetails() {
			return details;
		}
		
	}
	
}
