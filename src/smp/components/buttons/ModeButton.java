package smp.components.buttons;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import smp.ImageLoader;
import smp.components.general.ImageToggleButton;
import smp.fx.SMPFXController;
import smp.stateMachine.ProgramState;
import smp.stateMachine.StateMachine;

/**
 * The button that changes the mode of Super Mario Paint between arranger and
 * song modes.
 *
 * @author RehdBlob
 * @since 2014.05.21
 */
public class ModeButton extends ImageToggleButton {

    /**
     * This is the text field that displays the current mode of the program.
     */
    private Text modeDisp;

    /**
     * This creates a new ModeButton object.
     *
     * @param i
     *            This <code>ImageView</code> object that you are trying to link
     *            this button with.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public ModeButton(ImageView i, Text t, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
        modeDisp = t;
        modeDisp.setText("Song");
    }

    @Override
    public void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState();
        if (curr != ProgramState.ARR_PLAYING && curr != ProgramState.SONG_PLAYING) {
            if (curr == ProgramState.EDITING) {
                modeDisp.setText("Arranger");
                theStaff.setArrangerMode(true);
            } else if (curr == ProgramState.ARR_EDITING) {
                modeDisp.setText("Song");
                theStaff.setArrangerMode(false);
            }
        }
    }

}
