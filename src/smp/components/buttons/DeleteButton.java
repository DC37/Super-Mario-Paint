package smp.components.buttons;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageLoader;
import smp.components.general.ImagePushButton;
import smp.fx.SMPFXController;
import smp.stateMachine.Settings;

/**
 * This is a button that deletes a song from an arrangement.
 *
 * @author RehdBlob
 * @since 2014.07.27
 */
public class DeleteButton extends ImagePushButton {

    /** The ListView that we modify. */
    private ListView<String> theList;

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

    /**
     * @param l
     *            The ListView we want to set.
     */
    public void setList(ListView<String> l) {
        theList = l;
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        if ((Settings.debug & 0b100000) != 0)
            System.out.println("Delete song");

        ObservableList<String> l = theStaff.getArrangementList().getItems();
        int x = theStaff.getArrangementList().getSelectionModel()
                .getSelectedIndex();
        if (x != -1) {
            theStaff.getArrangement().remove(x);
            l.remove(x);
        }
    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

}
