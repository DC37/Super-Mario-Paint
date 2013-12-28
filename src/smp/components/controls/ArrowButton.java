package smp.components.controls;

import smp.ImageIndex;
import smp.components.general.ImagePushButton;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * This is a button that points left or right.
 * @author RehdBlob
 * @since 2013.08.23
 * 
 */
public class ArrowButton extends ImagePushButton {

    /**
     * The amount of movement that this arrow button will cause
     * on the staff. For a regular arrow, this will be a single
     * measure line, but that can be changed. The fast arrows
     * will generally go to the ends of the staff.
     */
    private double skipAmount;

    /** This is the slider that the scrollbar will affect. */
    private Slider scrollbar;

    /**
     * Default constructor.
     * @param i The <code>ImageView</code> object that we are
     * going to make into a button.
     */
    public ArrowButton(ImageView i, Slider scr, ImageIndex pr,
            ImageIndex notPr) {
        super(i);
        scrollbar = scr;
        getImages(pr, notPr);
    }

    /**
     * @param i The amount that we want this arrow button to skip on the
     * staff. Positive values move the staff to the right while negative
     * numbers move it to the left.
     */
    public void setSkipAmount(double i) {
        skipAmount = i;
    }

    /**
     * @return The amount of measure lines that the staff will move if one
     * presses this button. Positive values move the staff to the right
     * whilst negative values move it to the left.
     */
    public double getSkipAmount() {
        return skipAmount;
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        super.reactPressed(event);
        scrollbar.adjustValue(scrollbar.getValue() + skipAmount);
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        super.reactReleased(event);
    }

}
