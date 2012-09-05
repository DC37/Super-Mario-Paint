package smp.components.controls;

import javafx.scene.image.ImageView;
import smp.components.general.ImageRadioButton;

/**
 * Wrapper class for an ImageView that holds the play button
 * image. Pressing the button changes the image and also changes
 * the state of the program.
 * @author RehdBlob
 * @since 2012.08.28
 */
public class PlayButton extends ImageRadioButton {

    private boolean isPressed;


    public PlayButton(ImageView i) {
        super(i);
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setImage(ImageView i) {
        theImage = i;
    }

    public ImageView getImage() {
        return theImage;
    }

    @Override
    protected void react() {
        // TODO Auto-generated method stub

    }

    @Override
    public void link() {
        // TODO Auto-generated method stub

    }
}
