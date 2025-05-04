package smp.components.buttons;

import java.io.File;

import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageLoader;
import smp.components.general.ImagePushButton;
import smp.components.staff.sequences.StaffSequence;
import smp.fx.SMPFXController;
import smp.stateMachine.ProgramState;
import smp.stateMachine.Settings;
import smp.stateMachine.StateMachine;

/**
 * This is a button that moves a song on an arrangement.
 *
 * @author RehdBlob
 * @since 2014.07.27
 */
public class MoveButton extends ImagePushButton {

    /** The amount to move a song up or down. */
    private int moveAmt = 0;

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
    public MoveButton(ImageView i, int mv, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
        moveAmt = mv;
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState();
        if (curr != ProgramState.ARR_PLAYING) {
            if ((Settings.debug & 0b100000) != 0)
                System.out.println("Move song " + moveAmt);
            ObservableList<String> l = theStaff.getArrangementList().getItems();
            int x = theStaff.getArrangementList().getSelectionModel()
                    .getSelectedIndex();
            if (x != -1) {
                StateMachine.setArrModified(true);
                Object[] o = theStaff.getArrangement().remove(x);
                String s = l.remove(x);
                StaffSequence ss = (StaffSequence) o[0];
                File f = (File) o[1];
                int moveTo = x - moveAmt;
                if (moveTo > l.size())
                    moveTo = l.size();
                if (moveTo < 0)
                    moveTo = 0;
                l.add(moveTo, s);
                theStaff.getArrangement().add(moveTo, ss, f);
                theStaff.getArrangementList().getSelectionModel()
                        .select(moveTo);
            }
        }
    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

}
