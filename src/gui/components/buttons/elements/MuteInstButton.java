package gui.components.buttons.elements;

import gui.SMPFXController;
import gui.StateMachine;
import gui.components.buttons.ImageToggleButton;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * This is a button that allows us to mute all of one type of instrument, much
 * like the low A glitch note in MPC.
 * 
 * @author RehdBlob
 * @since 2013.12.24
 */
public class MuteInstButton extends ImageToggleButton {

    /** The mute button that is linked to this button. */
    private MuteButton mt;

    /**
     * This creates a new MuteButton object.
     * 
     * @param i
     *            This <code>ImageView</code> object that you are trying to link
     *            this button with.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public MuteInstButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
        getImages(ImageIndex.MUTE_A_PRESSED, ImageIndex.MUTE_A_RELEASED);
        releaseImage();
        isPressed = false;
        
		// TODO: create getMuteInstButton() somewhere so adding a hotkey can be done elsewhere
        // @since v1.1.2 per request of seymour schlong
		ct.getBasePane().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (controller.getNameTextField().focusedProperty().get()) return; // Disable while textfield is focused
				if(event.getCode() == KeyCode.M)
					reactPressed(null);
			}
		});
    }

    @Override
    protected void reactClicked(MouseEvent event) {
        // Do nothing

    }

    @Override
    public void reactPressed(MouseEvent event) {
        if (isPressed) {
            isPressed = false;
            releaseImage();
            StateMachine.setMuteAPressed(false);
        } else {
            if (mt.isPressed())
                mt.reactPressed(null);
            isPressed = true;
            pressImage();
            StateMachine.setMuteAPressed(true);
        }

    }

    /**
     * @param im
     *            The mute button that we want to set.
     */
    public void setMuteButton(MuteButton im) {
        mt = im;
    }

    /** @return The mute button that this muteA button is linked to. */
    public ImageToggleButton getMuteButton() {
        return mt;
    }

}
