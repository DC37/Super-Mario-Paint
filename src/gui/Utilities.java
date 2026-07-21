package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * Creates a boolean array from a parsed long array, from a loaded file.
     *
     * @param parseLong
     *            The long that we want to parse.
     * @return A boolean array based on the long integer that we have loaded.
     */
    public static boolean[] boolFromLong(long parseLong) {
        boolean[] loaded = new boolean[Values.NUM_INSTRUMENTS];
        for (int i = 0; i < Values.NUM_INSTRUMENTS; i++) {
            loaded[i] = ((1 << i) & parseLong) != 0;
        }
        return loaded;
    }

    /**
     * Creates a long integer from a parsed boolean array.
     *
     * @param parseBool
     *            The boolean array that we want to parse.
     * @return A long integer that is a bitfield that represents the boolean
     *         array.
     */
    public static long longFromBool(boolean[] parseBool) {
        long parsed = 0;
        for (int i = 0; i < parseBool.length; i++) {
            if (parseBool[i]) {
                parsed |= (1 << i);
            }
        }
        return parsed;
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
    
    /**
     * Try waiting for approximately `millis` milliseconds.
     * 
     * @param millis
     *            The number of milliseconds to try to wait for.
     */
    public static void tryWait(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("An exception occurred!", e);
        }
    }

}
