package smp.stateMachine;

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

    /** We only use the bottom number to display time sigs as "4/4" or "12/8"
     * for example, but it's not used for anything.
     * Kept as a legacy feature, as some song files may use this notation.
     */
    @Deprecated
    private final int bottom;
    
    public TimeSignature(int barLength) {
        this(barLength, 0);
    }
    
    public TimeSignature(int top, int bottom) {
        this.top = top;
        this.bottom = bottom;
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

    @Override
    public String toString() {
        return (bottom == 0) ? top + "" : top + "/" + bottom;
    }

    public static TimeSignature valueOf(String disp) {
        int idxSlash = disp.indexOf("/");
        
        if (idxSlash == -1) {
            int barLength = Integer.parseInt(disp);
            return new TimeSignature(barLength);
            
        } else {
            int top = Integer.parseInt(disp.substring(0, idxSlash));
            int bottom = Integer.parseInt(disp.substring(idxSlash + 1));
            return new TimeSignature(top, bottom);
        }
    }
    
    public static TimeSignature FOUR_FOUR = new TimeSignature(4, 4);
    public static TimeSignature THREE_FOUR = new TimeSignature(3, 4);

}
