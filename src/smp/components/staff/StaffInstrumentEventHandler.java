package smp.components.staff;

import smp.ImageLoader;
import smp.SoundfontLoader;
import smp.components.Constants;
import smp.components.InstrumentIndex;
import smp.components.staff.sequences.StaffNote;
import smp.components.topPanel.ButtonLine;
import smp.stateMachine.StateMachine;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * A Staff event handler. The StaffImages implementation was getting bulky
 * and there are still many many features to be implemented here...
 * @author RehdBlob
 * @since 2013.07.27
 */
public class StaffInstrumentEventHandler implements EventHandler<MouseEvent> {


    /** The position of this note. */
    private int position;

    /** Whether we've placed a note down on the staff or not. */
    private boolean clicked = false;

    /**
     * This is the list of image notes that we have. These should
     * all be ImageView-type objects.
     */
    private ObservableList<Node> theImages;

    /** The topmost image of the instrument. */
    private StaffNote theImage = new StaffNote();

    // Somewhere down the line, we will extend ImageView to
    // include more things.
    // private StaffNote theImage = new StaffNote();

    /** The StackPane that this handler is attached to. */
    private StackPane s;

    /**
     * Constructor for this StaffEventHandler. This creates a handler
     * that takes a StackPane and a position on the staff.
     * @param stPane The StackPane that we are interested in.
     * @param position The position that this handler is located on the
     * staff.
     */
    public StaffInstrumentEventHandler(StackPane stPane, int pos) {
        position = pos;
        s = stPane;
        theImages = s.getChildren();
    }

    @Override
    public void handle(MouseEvent event) {
        InstrumentIndex theInd =
                ButtonLine.getSelectedInstrument();
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if (event.getButton() == MouseButton.PRIMARY)
                leftMousePressed(theInd);
            else if (event.getButton() == MouseButton.SECONDARY)
                rightMousePressed(theInd);

        } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            mouseEntered(theInd);

        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            mouseExited(theInd);

        }
    }

    /**
     * The method that is called when the left mouse button is pressed.
     * This is generally the signal to add an instrument to that line.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void leftMousePressed(InstrumentIndex theInd) {
        theImage.setImage(
                ImageLoader.getSpriteFX(
                        theInd.imageIndex()));

        playSound(theInd, position);
        theImage = new StaffNote();
        clicked = true;

    }

    /**
     * The method that is called when the right mouse button is pressed.
     * This is generally the signal to remove the instrument from that line.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void rightMousePressed(InstrumentIndex theInd) {
        if (!theImages.isEmpty()) {
            theImages.remove(theImages.size() - 1);
        }
        if (!theImages.isEmpty()) {
            theImages.remove(theImages.size() - 1);
        }
    }


    /**
     * The method that is called when the mouse enters the object.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void mouseEntered(InstrumentIndex theInd) {
        theImage.setVisible(true);
        theImage.setImage(
                ImageLoader.getSpriteFX(
                        theInd.imageIndex().silhouette()));
        theImages.add(theImage);
    }

    /**
     * The method that is called when the mouse exits the object.
     * @param children List of Nodes that we have here, hopefully full of
     * ImageView-type objects.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void mouseExited(InstrumentIndex theInd) {
        if (!clicked) {
            theImage.setVisible(false);
            if (!theImages.isEmpty())
                theImages.remove(theImages.size() - 1);
        }
        clicked = false;
    }

    /**
     * Plays a sound given an index and a position.
     * @param theInd The index at which this instrument is located at.
     * @param pos The position at which this note is located at.
     */
    private static void playSound(InstrumentIndex theInd, int pos) {
        int acc;
        if (StateMachine.isAltPressed() || StateMachine.isCtrlPressed())
            acc = -1;
        else if (StateMachine.isShiftPressed())
            acc = 1;
        else
            acc = 0;
        SoundfontLoader.playSound(Constants.staffNotes[pos].getKeyNum(),
                theInd, acc);
    }

}
