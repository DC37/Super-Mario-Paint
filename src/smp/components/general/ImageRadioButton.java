package smp.components.general;

import java.util.ArrayList;

import smp.fx.SMPFXController;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Image radio button that toggles between a list
 * of buttons. This class holds a list of references
 * to other ImageRadioButtons that it's linked to.
 * @author RehdBlob
 * @since 2012.08.30
 */
public abstract class ImageRadioButton extends AbstractImageButton {

    /**
     * The list of radio buttons that are linked to this button.
     */
    protected ArrayList<ImageRadioButton> linkedButtons;

    /**
     * @param i The ImageView passed to the Button
     * wrapper.
     * @param controller The FXML controller object.
     */
    public ImageRadioButton(ImageView i, SMPFXController controller) {
        super(i, controller);
        linkedButtons = new ArrayList<ImageRadioButton>();
    }

    /**
     * Links the ImageRadioButton to another ImageRadioButton
     * such that if it is depressed, the other buttons in the list will
     * be reverted to an unpressed state.
     * @param b The <code>ImageRadioButton</code> to link this
     * <code>ImageRadioButton</code> to.
     */
    public void link(ImageRadioButton b) {
        linkedButtons.add(b);
    }

    /**
     * Sets this button's <code>isPressed</code> to true and also presses
     * the button image down.
     */
    @Override
    protected void reactPressed(MouseEvent e) {
        pressImage();
        isPressed = true;
        for (ImageRadioButton i : linkedButtons) {
            i.releaseImage();
            i.isPressed = false;
        }
    }


}
