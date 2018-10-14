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
 * Wrapper class for an ImageView that holds the play button image. Pressing the
 * button changes the image and also changes the state of the program.
 *
 * @author RehdBlob
 * @since 2012.08.28
 */
public class PlayPresenter extends ImageRadioButton {

	//TODO: auto-add these model comments
	//====Models====
    private ObjectProperty<ProgramState> programState;

	/**
     * Instantiates the Play button on the staff
     *
     * @param play
     *            The ImageView object that will be manipulated by this class.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public PlayPresenter(ImageView play) {
        super(play);
        getImages(ImageIndex.PLAY_PRESSED, ImageIndex.PLAY_RELEASED);
        releaseImage();
        isPressed = false;
        
        this.programState = StateMachine.getState();
        setupViewUpdater();
    }

    private void setupViewUpdater() {
		this.programState.addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				//TODO: instead of this listener, link with stop?
				if (newValue.equals(ProgramState.SONG_PLAYING) || newValue.equals(ProgramState.ARR_PLAYING)) {
			        reactPressed(null);
				} else if (newValue.equals(ProgramState.EDITING) || newValue.equals(ProgramState.ARR_EDITING)) {
			        releaseImage();
			        isPressed = false;
				}
			}
		});
	}
	@Override
    protected void reactPressed(MouseEvent e) {
        if (isPressed)
            return;
        super.reactPressed(e);
        if (programState.get() == ProgramState.EDITING) {
        	programState.set(ProgramState.SONG_PLAYING);
            //TODO: convert startSong
//            theStaff.startSong();
        } else if (programState.get() == ProgramState.ARR_EDITING) {
        	programState.set(ProgramState.ARR_PLAYING);
            //TODO: convert startArrangement
//            theStaff.startArrangement();
        }
    }

}
