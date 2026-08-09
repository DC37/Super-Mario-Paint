package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;

/**
 * A somewhat useful utilities class for images and such. Not so much use right
 * now yet, but will gain some functionality eventually.
 *
 * @author RehdBlob
 * @since 2012.08.20
 */
@Slf4j
public class Utilities {
    
    private Utilities() {}
    
    private static final List<Character> ILLEGAL_CHARS = new ArrayList<>();
    
    static {
        ILLEGAL_CHARS.addAll(Arrays.asList(
                '<', '>', '/', '\\', ':', '?', '|', '*', '"', '^'));
    }
    
    /**
     * Find the window owning an event. Useful for events that trigger a popup window.
     * Returns {@code null} if the owner cannot be found.
     */
    public static Window getOwner(Event evt) {
        if (evt == null)
            return null;
        
        Object src = evt.getSource();
        if (!(src instanceof Node))
            return null;
        
        return ((Node) src).getScene().getWindow();
    }

    /**
     * Prepare a {@link String} for showing in a dialog, with the
     * list of illegal characters following a given preamble. The
     * illegal characters are separated by a comma (<code>,</code>).
     * 
     * @param preamble The text to precede the list with.
     * @return The preamble, a newline, and the list of illegal
     *         characters, each one separated by a comma (<code>,</code>). 
     */
    public static String getIllegalCharsDialogText(String preamble) {
    	String illegalCharsCSL = ILLEGAL_CHARS.stream()
    			.map(Object::toString)
    			.collect(Collectors.joining(", "));
    	
    	return String.format("%s%n%s", preamble, illegalCharsCSL);
    }
    
    /**
     * Check if a string does not contain any illegal character.
     */
    public static boolean legalFileName(String s) {
        for (char c : s.toCharArray()) {
            if (ILLEGAL_CHARS.contains(c))
                return false;
        }
        return true;
    }
    
    /**
     * Group one or more {@link ToggleButton}s together.
     * 
     * @param group
     *            The {@link ToggleGroup} that will contain the buttons. 
     * @param toggles
     *            One or more {@link ToggleButton}s to add to the group.
     */
    public static void groupToggleBtns(ToggleGroup group, ToggleButton... toggles) {
    	for (ToggleButton tb: toggles) {
    		tb.setToggleGroup(group);
    	}
    }
    
}
