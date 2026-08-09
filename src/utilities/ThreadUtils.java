package utilities;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadUtils {

	private ThreadUtils() {}

	/**
	 * Try waiting for approximately <code>millis.nanos</code> milliseconds.
	 * 
	 * @param millis The number of milliseconds to try to wait for.
	 * @param nanos The number of additional nanoseconds to try to wait for.
	 */
	public static void tryWait(long millis, int nanos) {
		try {
	        Thread.sleep(millis, nanos);
	    } catch (InterruptedException e) {
	        log.error("An exception occurred!", e);
	    }
	}

	/**
	 * Try waiting for approximately <code>millis</code> milliseconds.
	 * 
	 * @param millis The number of milliseconds to try to wait for.
	 */
	public static void tryWait(long millis) {
		tryWait(millis, 0);
	}
	
}
