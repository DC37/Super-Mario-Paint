package smp.components.general;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * This is a pushbutton image that you click and hold. Releasing the
 * button will cause it to pop back up into the off position.
 * @author RehdBlob
 * @since 2013.08.23
 */
public abstract class ImagePushButton extends AbstractImageButton {

    /**
     * Default constructor.
     * @param i The <code>ImageView</code> object that we want to make
     * into a pushbutton.
     */
    public ImagePushButton(ImageView i) {
        super(i);
    }


    @Override
    protected void reactClick(MouseEvent event) {

    }

    @Override
    protected void reactPressed(MouseEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void reactReleased(MouseEvent event) {
        // TODO Auto-generated method stub

    }

}
