package gui.components.buttons.elements;

import gui.ProgramState;
import gui.SMPFXController;
import gui.StateMachine;
import gui.Utilities;
import gui.components.buttons.ImagePushButton;
import gui.loaders.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

/**
 * This is the options button. It currently doesn't do anything.
 *
 * @author RehdBlob
 * @author seymour
 * @since 2013.12.25
 */
public class OptionsButton extends ImagePushButton {

    /**
     * Default constructor.
     *
     * @param i
     *            The image that we want to link this to.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public OptionsButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState();
        if (curr == ProgramState.EDITING || curr == ProgramState.ARR_EDITING) {
            Window owner = Utilities.getOwner(event);
        	controller.options(owner);
        }
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        // do nothing.
    }

}
