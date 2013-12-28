package smp.components.controls;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.components.general.ImagePushButton;

/**
 * This is the options button. It currently doesn't do anything.
 * @author RehdBlob
 * @since 2013.12.25
 */
public class OptionsButton extends ImagePushButton {

    /**
     * Default constructor.
     * @param i The image that we want to link this to.
     */
    public OptionsButton(ImageView i) {
        super(i);
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        options();
    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

    /** Opens up an options dialog. */
    private void options() {
        updateValues();
    }

    /** Updates the different values of this program. */
    private void updateValues() {

    }

}
