package smp.stateMachine;

import java.util.Arrays;

/**
 * Time signatures determine the length of a bar and how bar lines should be counted
 * and displayed on the staff.
 *
 * @author RehdBlob
 * @author rozlynd
 * @since 2013.06.28
 */
public class TimeSignature {

    /** Length of a bar, determines how the endpoint of a sequence is calculated. */
    private final int top;
    
    /** Lengths of the subdivisions of a measure.
     * The list is non-empty and {@link top} is always the sum of its elements.
     */
    private final int[] divs;

    /** We only use the bottom number to display time sigs as "4/4" or "12/8"
     * for example, but it's not used for anything.
     * Kept as a legacy feature, as some song files may use this notation.
     */
    @Deprecated
    private final int bottom;
    
    public TimeSignature(int barLength) throws IllegalArgumentException {
        this(barLength, 0);
    }
    
    public TimeSignature(int top, int bottom) throws IllegalArgumentException {
        if (top == 0)
            throw new IllegalArgumentException("Attempted to instantiate TimeSignature with top=0");
            
        this.top = top;
        this.divs = new int[1];
        this.divs[0] = top;
        this.bottom = bottom;
    }
    
    public TimeSignature(int[] divs, int bottom) throws IllegalArgumentException {
        if (divs.length == 0)
            throw new IllegalArgumentException("Attempted to instantiate TimeSignature with top=0");
        
        int top = 0;
        for (int d : divs)
            top += d;
        this.top = top;
        
        this.divs = Arrays.copyOf(divs, divs.length);
        
        this.bottom = bottom;
    }
    
    public TimeSignature(int[] divs) throws IllegalArgumentException {
        this(divs, 0);
    }
    
    public int barLength() {
        return top;
    }

    public int top() {
        return top;
    }

    public int bottom() {
        return bottom;
    }
    
    public int[] divs() {
        return divs;
    }

    @Override
    public String toString() {
        String topStr = String.join("+", Arrays.stream(divs).mapToObj(i -> i + "").toArray(String[]::new));
        return (bottom == 0) ? topStr : topStr + "/" + bottom;
    }

    public static TimeSignature valueOf(String disp) {
        int idxSlash = disp.indexOf("/");
        
        if (idxSlash == -1) {
            String[] divsStrs = disp.split("\\+");
            int[] divs = Arrays.stream(divsStrs).mapToInt(Integer::parseInt).toArray();
            return new TimeSignature(divs);
            
        } else {
            String[] divsStrs = disp.substring(0, idxSlash).split("\\+");
            int[] divs = Arrays.stream(divsStrs).mapToInt(Integer::parseInt).toArray();
            int bottom = Integer.parseInt(disp.substring(idxSlash + 1));
            return new TimeSignature(divs, bottom);
        }
    }
    
    public static TimeSignature FOUR_FOUR = new TimeSignature(4, 4);
    public static TimeSignature THREE_FOUR = new TimeSignature(3, 4);

}
