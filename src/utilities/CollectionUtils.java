package utilities;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class CollectionUtils {

    private CollectionUtils() {}
    
    /**
     * Determines if the given reference {@link String} contains
     * any of the provided probes.
     * 
     * @param ref The reference to compare against
     * @param probes One or more probe values to look for
     * @return Whether at least one of the probes is contained in the reference
     */
    public static boolean containsAny(String ref, String... probes) {
        return Arrays.asList(probes).stream().anyMatch(ref::contains);
    }
    
    /**
     * Swaps two items of a boolean array, in-place.
     * 
     * @param items The boolean array to process
     * @param idx1 The first index to swap
     * @param idx2 The second index to swap
     */
    public static void swapItems(boolean[] items, int idx1, int idx2) {
        boolean tmp = items[idx1];
        items[idx1] = items[idx2];
        items[idx2] = tmp;
    }
    
    /**
     * Adds an element to a list at the specified index,
     * after optionally adding filler to reach that index.
     * 
     * @param l The list to process
     * @param elem The element to add
     * @param idx The index at which the element is to be added
     * @param fnGetFiller A generator function that provides filler if the index is the size of the list or greater
     */
    public static <T> void addFillerThenElement(List<T> l, T elem, int idx, Supplier<T> fnGetFiller) {
        if (idx < l.size()) {
            l.set(idx, elem);
        } else {
            while (l.size() < idx) {
                l.add(fnGetFiller.get());
            }
            
            l.add(elem);
        }
    }
    
}
