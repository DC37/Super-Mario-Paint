package smp.components.controls;

import smp.ImageIndex;
import smp.components.general.ImagePushButton;
import javafx.scene.image.ImageView;

/**
 * This is a button that points left or right.
 * @author RehdBlob
 * @since 2013.08.23
 * 
 */
public class ArrowButton extends ImagePushButton {

    /**
     * Default constructor.
     * @param i The <code>ImageView</code> object that we are
     * going to make into a button.
     */
    public ArrowButton(ImageView i) {
        super(i);
    }

    @Override
    protected void getImages(ImageIndex pr, ImageIndex notPr) {
        super.getImages(pr, notPr);
    }


}
