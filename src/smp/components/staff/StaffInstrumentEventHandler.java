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

    /**
     * State machine. The different representations are as follows: </br>
     * <b>0x0</b>: Mouse not in frame, nothing has been clicked. </br>
     * <b>0x1</b>: Mouse in frame, nothing has been clicked. A silhouette
     * of the current selected instrument is visible.</br>
     * <b>0x2</b>: Mouse in frame, and we've clicked once. A StaffNote has
     * been placed on the staff. </br>
     * <b>0x3</b>: Mouse not in frame, and we are now displaying a
     * StaffNote on the staff.</br>
     * <b>0x4</b>: Mouse in frame, and we're displaying an image
     * silhouette on top of the previous layer instrument.</br>
     * 
     */
    private int state = 0x0;

    /**
     * This is the list of image notes that we have. These should
     * all be ImageView-type objects.
     */
    private ObservableList<Node> theImages;

    /**
     * This is the <code>ImageView</code> object responsible for
     * displaying the silhouette of the note that we are about to place
     * on the staff.
     */
    private ImageView silhouette = new ImageView();

    /** The topmost image of the instrument. */
    private StaffNote theStaffNote;

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
        if (state == 0x1 || state == 0x4) {
            theStaffNote = new StaffNote(theInd, position);
            theStaffNote.setImage(
                    ImageLoader.getSpriteFX(
                            theInd.imageIndex()));
            playSound(theInd, position);
            theImages.remove(silhouette);
            if (!theImages.contains(theStaffNote))
                theImages.add(theStaffNote);
            state = 0x2;
        } else if (state == 0x2) {
            theStaffNote = new StaffNote(theInd, position);
            theStaffNote.setImage(
                    ImageLoader.getSpriteFX(
                            theInd.imageIndex()));
            playSound(theInd, position);
            theImages.remove(silhouette);
        }


    }

    /**
     * The method that is called when the right mouse button is pressed.
     * This is generally the signal to remove the instrument from that line.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void rightMousePressed(InstrumentIndex theInd) {
        if (state == 0x1) {
            if (!theImages.isEmpty())
                theImages.remove(theImages.size() - 1);
        } else if (state == 0x2 || state == 0x4) {
            theImages.remove(silhouette);
            theImages.remove(theImages.size() - 1);
            state = 0x1;
        }
    }


    /**
     * The method that is called when the mouse enters the object.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void mouseEntered(InstrumentIndex theInd) {
        if (state == 0x0) {
            silhouette.setImage(
                    ImageLoader.getSpriteFX(
                            theInd.imageIndex().silhouette()));
            theImages.add(silhouette);
            state = 0x1;
        } else if (state == 0x3) {
            silhouette.setImage(
                    ImageLoader.getSpriteFX(
                            theInd.imageIndex().silhouette()));
            theImages.add(silhouette);
            state = 0x4;
        }
    }

    /**
     * The method that is called when the mouse exits the object.
     * @param children List of Nodes that we have here, hopefully full of
     * ImageView-type objects.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void mouseExited(InstrumentIndex theInd) {
        if (state == 0x1) {
            theImages.remove(silhouette);
            state = 0x0;
        } else if (state == 0x2) {
            theImages.remove(silhouette);
            state = 0x3;
        } else if (state == 0x4) {
            theImages.remove(silhouette);
            state = 0x1;
        }

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
