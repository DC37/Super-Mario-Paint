package gui.components.buttons.elements;

import gui.ProgramState;
import gui.SMPFXController;
import gui.Settings;
import gui.StateMachine;
import gui.components.buttons.ImagePushButton;
import gui.loaders.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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
                StateMachine.setArrModified(true);
                theStaff.getArrangementList().getItems()
                        .add(theStaff.getSequenceName());
                theStaff.getArrangement().add(theStaff.getSequence(),
                        theStaff.getSequenceFile());
                controller.getSoundfontLoader().storeInCache();
            }
        }
    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

}
