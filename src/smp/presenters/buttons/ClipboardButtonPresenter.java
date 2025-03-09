package smp.presenters.buttons;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.StateMachine;
import smp.presenters.api.button.ImageToggleButton;

/**
 * Wrapper class for an ImageView that holds the stop button image. Pressing the
 * button changes the image and also changes the state of the program.
 *
 * @author RehdBlob
 * @author j574y923
 * 
 * @since 2018.01.14
 */
public class ClipboardButtonPresenter extends ImageToggleButton {

	//TODO: auto-add these model comments
	//====Models====
    private ObjectProperty<ProgramState> programState;

	/**
     * Instantiates the clipboard selection button.
     *
     * @param clipboardButton
     *            The ImageView that will be manipulated by this class.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
	public ClipboardButtonPresenter(ImageView clipboardButton) {
		super(clipboardButton);
		getImages(ImageIndex.CLIPBOARD_PRESSED, ImageIndex.CLIPBOARD_RELEASED);
		releaseImage();
		Tooltip.install(clipboardButton, new Tooltip("Click (or Shift+R) to toggle region selection\n"
				+ "Hover over instrument & press F to filter instrument\n"
				+ "Ctrl+A to select all\n"
				+ "Ctrl+C to copy notes\n"
				+ "Ctrl+V to paste notes\n"
				+ "Ctrl+X to cut notes\n"
				+ "Delete to delete notes\n"
				+ "Alt+N to toggle notes selection\n"
				+ "Alt+V to toggle volumes selection"));
		
		// TODO: create getClipboardButton() somewhere so adding a hotkey can be done elsewhere
//		ct.getBasePane().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//
//			@Override
//			public void handle(KeyEvent event) {
//				if(event.isShiftDown() && event.getCode() == KeyCode.R)
//					reactPressed(null);
//			}
//		});
		this.programState = StateMachine.getState();
		setupViewUpdater();
	}

    private void setupViewUpdater() {
    	this.programState.addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				if (newValue.equals(ProgramState.EDITING))
					theImage.setVisible(true);
				else if (newValue.equals(ProgramState.ARR_EDITING))
					theImage.setVisible(false);
			}
		});
	}

	@Override
    public void reactPressed(MouseEvent e) {
        if (isPressed) {
            isPressed = false;
            releaseImage();
    		StateMachine.setClipboardPressed(false);
        	//TODO: staff, volume presenter
    		//TODO: make clipboard api more intuitive to mvp
    		this.controller.getStaffInstruments().setMouseTransparent(false);
    		this.controller.getVolumeBars().setMouseTransparent(false);
    		
        } else {
            isPressed = true;
            pressImage();
    		StateMachine.setClipboardPressed(true);
    		this.controller.getStaffInstruments().setMouseTransparent(true);
    		this.controller.getVolumeBars().setMouseTransparent(true);
        }
    }
}
