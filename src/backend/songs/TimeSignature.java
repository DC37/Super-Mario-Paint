package backend.songs;

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
        if (top <= 0)
            throw new IllegalArgumentException("TimeSignature constructor expects positive number(s)");
            
        this.top = top;
        this.divs = new int[1];
        this.divs[0] = top;
        this.bottom = bottom;
    }
    
    public TimeSignature(int[] divs, int bottom) throws IllegalArgumentException {
        if (divs.length == 0)
            throw new IllegalArgumentException("TimeSignature constructor expects positive number(s)");
        
        int top = 0;
        for (int d : divs) {
            if (d <= 0)
                throw new IllegalArgumentException("TimeSignature constructor expects positive number(s)");
            top += d;
        }
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
    
    @Override
    public boolean equals(Object oth) {
        if (!(oth instanceof TimeSignature))
            return false;
        
        TimeSignature t = (TimeSignature) oth;
        
        if (this.divs.length != t.divs.length)
            return false;
        
        for (int i = 0; i < this.divs.length; i++)
            if (this.divs[i] != t.divs[i])
                return false;
        
        return true;
    }

    public static TimeSignature valueOf(String disp) throws IllegalArgumentException {
        int idxSlash = disp.indexOf("/");
        
        if (idxSlash == -1) {
            String[] divsStrs = disp.split("\\+");
            int[] divs = Arrays.stream(divsStrs).mapToInt(Integer::parseInt).toArray();
            return new TimeSignature(divs);
            
        } else {
        	// We support time signatures with a bottom number for some cases:
        	// 4/4, 3/4, 6/8... possibly more later
        	
            String[] divsStrs = disp.substring(0, idxSlash).split("\\+");
            int[] divs = Arrays.stream(divsStrs).mapToInt(Integer::parseInt).toArray();
            int bottom = Integer.parseInt(disp.substring(idxSlash + 1));
            
            if (bottom == 4 && divs.length == 1) {
            	int top = divs[0];
            	if (top == 3) {
            		return THREE_FOUR;
            	} else if (top == 4) {
            		return FOUR_FOUR;
            	}
            	
            } else if (bottom == 8 && divs.length == 1) {
            	int top = divs[0];
            	if (top == 6) {
            		return SIX_EIGHT;
            	}
            	
            } else if (bottom == 8 && divs.length == 2) {
            	int top1 = divs[0];
            	int top2 = divs[1];
            	if (top1 == 3 && top2 == 3) {
            		return SIX_EIGHT;
            	}
            }
            
            throw new IllegalArgumentException("Unrecognized Time Signature Specification: " + disp);
        }
    }
    
    public static TimeSignature multiply(TimeSignature t, int multiplyBy) throws IllegalArgumentException {
        int[] divs = t.divs;
        int[] newDivs = Arrays.stream(divs).map(i -> i * multiplyBy).toArray();
        return new TimeSignature(newDivs);
    }
    
    public static TimeSignature FOUR_FOUR = new TimeSignature(4, 4);
    public static TimeSignature THREE_FOUR = new TimeSignature(3, 4);
    public static TimeSignature SIX_EIGHT = new TimeSignature(new int[] { 3, 3 }, 8);

}
