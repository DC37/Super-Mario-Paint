package smp.presenters.buttons;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.models.stateMachine.StateMachine;
import smp.presenters.api.button.ImageToggleButton;

/**
 * This is the button that, when pressed, toggles whether you are placing a mute
 * note or not.
 * 
 * @author RehdBlob
 * @since 2013.11.10
 *
 */
public class MuteAPresenter extends ImageToggleButton {

	//TODO: auto-add these model comments
	//====Models====
	private BooleanProperty mutePressed;
	private BooleanProperty muteAPressed;

    /**
     * This creates a new MuteButton object.
     * 
     * @param muteA
     *            This <code>ImageView</code> object that you are trying to link
     *            this button with.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public MuteAPresenter(ImageView muteA) {
        super(muteA);
        getImages(ImageIndex.MUTE_A_PRESSED, ImageIndex.MUTE_A_RELEASED);
        releaseImage();
        isPressed = false;

        //TODO: add hotkeys in standalone hotkeys class
		// TODO: create getMuteButton() somewhere so adding a hotkey can be done elsewhere
        // @since v1.1.2 per request of seymour schlong
//		ct.getBasePane().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//
//			@Override
//			public void handle(KeyEvent event) {
//				/** alt+n is for deselecting notes @see <code>StaffRubberBandEventHandler</code> */
//				if(event.isAltDown())
//					return;
//				if(event.getCode() == KeyCode.N)
//					reactPressed(null);
//			}
//		});
        
        this.mutePressed = StateMachine.getMutePressed();
        this.muteAPressed = StateMachine.getMuteAPressed();
        setupViewUpdater();
    }

    private void setupViewUpdater() {
		mutePressed.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue.equals(true)) {
		            isPressed = false;
		            releaseImage();
		            muteAPressed.set(false);
				}
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
            this.mutePressed.set(false);
        } else {
            isPressed = true;
            pressImage();
            this.mutePressed.set(true);
        }
    }
}
