package gui.components.buttons.elements;

import gui.SMPFXController;
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
 * This is the button that creates a new song.
 *
 * @author RehdBlob
 * @since 2013.12.18
 *
 */
public class NewButton extends ImagePushButton {

    /**
     * Default constructor.
     *
     * @param i
     *            This is the <code>ImageView</code> object that will house the
     *            Load button.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public NewButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
        
        // @since v1.4 to accomodate for those with a smaller screen that may not be able to access it.
  		ct.getBasePane().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

  			@Override
  			public void handle(KeyEvent event) {
				if (controller.getNameTextField().focusedProperty().get()) return; // Disable while textfield is focused
  				if(event.isControlDown() && event.getCode() == KeyCode.N)
  					reactPressed(null);
  			}
  		});
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        Window owner = Utilities.getOwner(event);
        controller.newSongOrArrangement(owner);
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        // do nothing.
    }

}
