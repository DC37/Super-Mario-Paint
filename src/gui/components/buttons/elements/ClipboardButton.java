package gui.components.buttons.elements;

import gui.SMPFXController;
import gui.StateMachine;
import gui.components.buttons.ImageToggleButton;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;
import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * Wrapper class for an ImageView that holds the stop button image. Pressing the
 * button changes the image and also changes the state of the program.
 *
 * @author RehdBlob
 * @author j574y923
 * 
 * @since 2018.01.14
 */
public class ClipboardButton extends ImageToggleButton {

    /**
     * Instantiates the clipboard selection button.
     *
     * @param i
     *            The ImageView that will be manipulated by this class.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
	public ClipboardButton(ImageView i, SMPFXController ct, ImageLoader im) {
		super(i, ct, im);
		getImages(ImageIndex.CLIPBOARD_PRESSED, ImageIndex.CLIPBOARD_RELEASED);
		releaseImage();
		Tooltip.install(i, new Tooltip("Click (or Shift+R) to toggle region selection\n"
				+ "Hover over instrument & press F to filter instrument\n"
				+ "Ctrl+A to select all\n"
				+ "Ctrl+C to copy notes\n"
				+ "Ctrl+V to paste notes\n"
				+ "Ctrl+X to cut notes\n"
				+ "Delete to delete notes\n"
				+ "Alt+N to toggle notes selection\n"
				+ "Alt+V to toggle volumes selection"));
		
		// TODO: create getClipboardButton() somewhere so adding a hotkey can be done elsewhere
		ct.getBasePane().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (controller.getNameTextField().focusedProperty().get()) return; // Disable while textfield is focused
				if(event.isShiftDown() && event.getCode() == KeyCode.R)
					reactPressed(null);
			}
		});
	}

    @Override
    public void reactPressed(MouseEvent e) {
        if (isPressed) {
            isPressed = false;
            releaseImage();
    		StateMachine.setClipboardPressed(false);
            StateMachine.setSelectionModeOn(false);
    		this.controller.getStaffInstruments().setMouseTransparent(false);
    		this.controller.getVolumeBars().setMouseTransparent(false);
    		
        } else {
            isPressed = true;
            pressImage();
    		StateMachine.setClipboardPressed(true);
    		StateMachine.setSelectionModeOn(true);
    		this.controller.getStaffInstruments().setMouseTransparent(true);
    		this.controller.getVolumeBars().setMouseTransparent(true);
        }
    }
}
