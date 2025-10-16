package gui.components.buttons.elements;

import gui.ProgramState;
import gui.SMPFXController;
import gui.StateMachine;
import gui.Utilities;
import gui.components.buttons.ImagePushButton;
import gui.loaders.ImageLoader;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
        
        ct.getBasePane().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (controller.getNameTextField().focusedProperty().get()) 
                    return; // Disable while textfield is focused
                
                if(event.isControlDown() && event.getCode() == KeyCode.COMMA)
                    reactPressed(null);
            }
        });
        
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
