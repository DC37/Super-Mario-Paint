package gui;

import gui.components.buttons.v2.SMPAbstractButton;
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
    
    @SafeVarargs
    public static <B extends ToggleButton> void groupToggleBtns(ToggleGroup group, SMPAbstractButton<B>... buttons) {
    	for (SMPAbstractButton<B> btn: buttons) {
    		btn.getInnerButton().setToggleGroup(group);
    	}
    }
    
}
