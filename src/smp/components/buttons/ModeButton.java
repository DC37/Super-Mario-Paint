package smp.components.buttons;

import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import smp.components.general.ImageToggleButton;

/**
 * The button that changes the mode of Super Mario Paint between
 * arranger and song modes.
 * @author RehdBlob
 * @since 2014.05.21
 */
public class ModeButton extends ImageToggleButton {

    /**
     * This is the text field that displays the current mode of the program.
     */
    private Text modeDisp;

    /**
     * This creates a new ModeButton object.
     * @param i This <code>ImageView</code> object that you are
     * trying to link this button with.
     */
    public ModeButton(ImageView i, Text t) {
        super(i);
    }

}
