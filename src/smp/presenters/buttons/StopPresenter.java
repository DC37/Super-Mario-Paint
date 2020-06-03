package smp.presenters.buttons;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.StateMachine;
import smp.presenters.api.button.ImageRadioButton;

/**
 * Wrapper class for an ImageView that holds the stop button image. Pressing the
 * button changes the image and also changes the state of the program.
 *
 * @author RehdBlob
 * @since 2012.09.14
 */
public class StopPresenter extends ImageRadioButton {

	//TODO: auto-add these model comments
	//====Models====
    private ObjectProperty<ProgramState> programState;

	/**
     * Instantiates the stop button.
     *
     * @param i
     *            The ImageView that will be manipulated by this class.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public StopPresenter(ImageView i) {
        super(i);
        getImages(ImageIndex.STOP_PRESSED, ImageIndex.STOP_RELEASED);
        pressImage();
        isPressed = true;
        
        this.programState = StateMachine.getState();
        setupViewUpdater();
    }

	private void setupViewUpdater() {
		this.programState.addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				//TODO: instead of this listener, link with play?
				if (newValue.equals(ProgramState.SONG_PLAYING) || newValue.equals(ProgramState.ARR_PLAYING)) {
			        releaseImage();
			        isPressed = false;
				} else if (newValue.equals(ProgramState.EDITING) || newValue.equals(ProgramState.ARR_EDITING)) {
			        reactPressed(null);
				}
			}
		});
	}

	@Override
    public void reactPressed(MouseEvent e) {
        if (isPressed)
            return;
        super.reactPressed(e);
        if (programState.get() == ProgramState.SONG_PLAYING) {
        	programState.set(ProgramState.EDITING);
            //TODO: stopSong
//            theStaff.stopSong();
        } else if (programState.get() == ProgramState.ARR_PLAYING) {
        	programState.set(ProgramState.ARR_EDITING);
            //TODO: stopSong
//            theStaff.stopSong();
        }
    }

}
