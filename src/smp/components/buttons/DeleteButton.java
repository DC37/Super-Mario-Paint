package smp.components.buttons;

import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageLoader;
import smp.components.general.ImagePushButton;
import smp.fx.SMPFXController;
import smp.stateMachine.ProgramState;
import smp.stateMachine.Settings;
import smp.stateMachine.StateMachine;

/**
 * This is a button that deletes a song from an arrangement.
 *
 * @author RehdBlob
 * @since 2014.07.27
 */
public class DeleteButton extends ImagePushButton {

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
    public DeleteButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
    }


    @Override
    protected void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState();
        if (curr != ProgramState.ARR_PLAYING) {
            if ((Settings.debug & 0b100000) != 0)
                System.out.println("Delete song");
            ObservableList<String> l = theStaff.getArrangementList().getItems();
            int x = theStaff.getArrangementList().getSelectionModel()
                    .getSelectedIndex();
            if (x != -1) {
                StateMachine.setArrModified(true);
                theStaff.getArrangement().remove(x);
                l.remove(x);
            }
        }
    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

}
