package utilities;

/**
 * Utility class for mathematical operations.
 * 
 * This is kept in case an older Java version is used,
 * in order to ease backporting.
 * 
 * @author Aura Lesse Programmer
 */
public class MathUtils {

    private MathUtils() {}
    
    /**
     * Clamp an integer between two given numbers.
     * 
     * @param value The number to clamp.
     * @param min The minimum value allowed.
     * @param max The maximum value allowed.
     * @return The clamped number.
     */
    public static int clamp(int value, int min, int max) {
        // Classic implementation: Math.max(min, Math.min(max, value))
        return Math.clamp(value, min, max);
    }
    
    /**
     * Clamp a double between two given numbers.
     * 
     * @param value The number to clamp.
     * @param min The minimum value allowed.
     * @param max The maximum value allowed.
     * @return The clamped number.
     */
    public static double clamp(double value, double min, double max) {
        // Classic implementation: Math.max(min, Math.min(max, value))
        return Math.clamp(value, min, max);
    }
    
}
