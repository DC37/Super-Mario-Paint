package gui.components.buttons.elements;

import gui.SMPFXController;
import gui.components.buttons.ImagePushButton;
import gui.loaders.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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
        theStaff.moveSongInArrangement(moveAmt);
    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

}
