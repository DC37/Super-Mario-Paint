package smp.components.controls;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.components.general.ImagePushButton;
import smp.stateMachine.StateMachine;

/**
 * This is a class that takes care of the adjustment of tempo in
 * Super Mario Paint.
 * @author RehdBlob
 * @since 2013.09.28
 */
public class TempoAdjustButton extends ImagePushButton {

    /** This is the current tempo. */
    private StringProperty currTempo;

    /** Tells us whether this is a plus or minus button. */
    private boolean isPositive;

    /**
     * Default constructor
     * @param i The <code>ImageView</code> object that we want this
     * adjustment button to be linked to.
     */
    public TempoAdjustButton(ImageView i) {
        super(i);

    }

    /**
     * @param b Is this a positive button?
     */
    public void setPositive(boolean b) {
        isPositive = b;
    }

    /**
     * @return Whether this is a positive button.
     */
    public boolean isPositive() {
        return isPositive;
    }

    /**
     * Sets the String property to display the tempo.
     * @param s This is the StringProperty that displays the tempo.
     */
    public void setStringProperty(StringProperty s) {
        currTempo = s;
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        setPressed();
        int change = 0;
        if (isPositive)
            change = 1;
        else
            change = -1;
        double tempo = StateMachine.getTempo() + change;
        StateMachine.setTempo(tempo);
        currTempo.setValue(String.valueOf(tempo));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int ch = 0;
                if (isPositive)
                    ch = 1;
                else
                    ch = -1;
                double tmp = StateMachine.getTempo() + ch;
                StateMachine.setTempo(tmp);
            }
        });
    }


    @Override
    protected void reactReleased(MouseEvent event) {
        resetPressed();
        Thread.currentThread().interrupt();
    }


}
