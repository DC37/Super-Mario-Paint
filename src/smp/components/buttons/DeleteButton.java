package smp.components.buttons;

import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.components.general.ImagePushButton;

/**
 * This is a button that deletes a song from an arrangement.
 * @author RehdBlob
 * @since 2014.07.27
 */
public class DeleteButton extends ImagePushButton {


    /** The ListView that we modify. */
    private ListView<String> theList;

    /**
     * Default constructor.
     * @param i The <code>ImageView</code> object that we are
     * going to make into a button.
     */
    public DeleteButton(ImageView i) {
        super(i);
    }

    /**
     * @param l The ListView we want to set.
     */
    public void setList(ListView<String> l) {
        theList = l;
    }

    @Override
    protected void reactPressed(MouseEvent event) {

    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

}
