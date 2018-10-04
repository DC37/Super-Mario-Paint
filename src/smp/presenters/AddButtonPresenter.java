package smp.presenters;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.models.staff.StaffArrangement;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.Settings;
import smp.models.stateMachine.StateMachine;
import smp.models.stateMachine.Variables;
import smp.presenters.api.button.ImagePushButton;

/**
 * This is a button that adds a song to an arrangement.
 *
 * @author RehdBlob
 * @since 2014.07.27
 */
public class AddButtonPresenter extends ImagePushButton {

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
    public AddButtonPresenter(ImageView i) {
        super(i);
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState().get();
        if (curr != ProgramState.ARR_PLAYING) {
            if ((Settings.debug & 0b100000) != 0)
                System.out.println("Add song");

            if (Variables.theSequenceFile.get() != null) {
                StateMachine.setArrModified(true);
                Variables.arrangementList.add(Variables.theSequenceName.get());
                ((StaffArrangement)Variables.theArrangement.get()).add(Variables.theSequence.get(),
                        Variables.theSequenceFile.get());
                controller.getSoundfontLoader().storeInCache();
            }
        }
    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

}
