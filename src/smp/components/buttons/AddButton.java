package smp.components.buttons;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageLoader;
import smp.components.general.ImagePushButton;
import smp.fx.SMPFXController;
import smp.stateMachine.ProgramState;
import smp.stateMachine.Settings;
import smp.stateMachine.StateMachine;

/**
 * This is a button that adds a song to an arrangement.
 *
 * @author RehdBlob
 * @since 2014.07.27
 */
public class AddButton extends ImagePushButton {

    /**
     * Default constructor.
     *
     * @param i
     *            The <code>ImageView</code> object that we are going to make
     *            into a button.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public AddButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState();
        if (curr != ProgramState.ARR_PLAYING) {
            if ((Settings.debug & 0b100000) != 0)
                System.out.println("Add song");

            if (theStaff.getSequenceFile() != null) {
                theStaff.getArrangementList().getItems()
                        .add(theStaff.getSequenceName());
                theStaff.getArrangement().add(theStaff.getSequence(),
                        theStaff.getSequenceFile());
            }
        }
    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

}
