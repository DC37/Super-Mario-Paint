package smp.components.staff;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.SoundfontLoader;
import smp.components.Values;
import smp.components.InstrumentIndex;
import smp.components.staff.sequences.StaffAccidental;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.topPanel.ButtonLine;
import smp.stateMachine.Settings;
import smp.stateMachine.StateMachine;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * A Staff event handler. The StaffImages implementation was getting bulky
 * and there are still many many features to be implemented here. This
 * handler primarily handles mouse events.
 * @author RehdBlob
 * @since 2013.07.27
 */
public class StaffInstrumentEventHandler implements EventHandler<Event> {

    /** The line number of this note, on the screen. */
    private int line;

    /** The position of this note. */
    private int position;

    /** Whether the mouse is in the frame or not. */
    private boolean mouseInFrame = false;

    /**
     * This is the list of image notes that we have. These should
     * all be ImageView-type objects.
     */
    private ObservableList<Node> theImages;

    /** The StackPane that will display sharps, flats, etc. */
    private ObservableList<Node> accList;

    /**
     * This is the <code>ImageView</code> object responsible for
     * displaying the silhouette of the note that we are about to place
     * on the staff.
     */
    private ImageView silhouette = new ImageView();

    /** The pointer to the staff object that this handler is linked to. */
    private Staff theStaff;

    /**
     * This is the <code>ImageView</code> object responsible
     * for displaying the silhouette of the sharp / flat of the note
     * that we are about to place on the staff.
     */
    private ImageView accSilhouette;

    /** The topmost image of the instrument. */
    private StaffNote theStaffNote;

    /**
     * This is the image that holds the different types of sharps/flats etc.
     */
    private StaffAccidental accidental;


    /** This is the amount that we want to sharp / flat / etc. a note. */
    private static int acc = 0;

    /**
     * Constructor for this StaffEventHandler. This creates a handler
     * that takes a StackPane and a position on the staff.
     * @param stPane The StackPane that we are interested in.
     * @param acc The accidental display pane.
     * @param pos The position that this handler is located on the
     * staff.
     * @param l The line of this event handler. Typically between 0 and 9.
     * @param s The pointer to the Staff object that this event handler is
     * linked to.
     */
    public StaffInstrumentEventHandler(StackPane stPane, StackPane acc,
            int pos, int l, Staff s) {
        position = pos;
        line = l;
        theImages = stPane.getChildren();
        accList = acc.getChildren();
        theStaff = s;
        if ((Settings.debug & 0b10) == 0b10) {
            System.out.println("Line: " + l);
            System.out.println("Position: " + pos);
        }
    }


    @Override
    public void handle(Event event) {
        InstrumentIndex theInd =
                ButtonLine.getSelectedInstrument();
        accSilhouette = new ImageView();
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            System.out.println("Keypress");
        }
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            MouseButton b = ((MouseEvent) event).getButton();
            if (b == MouseButton.PRIMARY)
                leftMousePressed(theInd);
            else if (b == MouseButton.SECONDARY)
                rightMousePressed(theInd);
            event.consume();

        } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            mouseInFrame = true;
            mouseEntered(theInd);
            event.consume();

        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            mouseInFrame = false;
            mouseExited(theInd);
            event.consume();

        }

    }


    /**
     * The method that is called when the left mouse button is pressed.
     * This is generally the signal to add an instrument to that line.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void leftMousePressed(InstrumentIndex theInd) {

        placeNote(theInd);

    }

    /**
     * Places a note where the mouse currently is.
     * @param theInd The <code>InstrumentIndex</code> that we are
     * going to use to place this note.
     */
    private void placeNote(InstrumentIndex theInd) {
        boolean mute = StateMachine.isMutePressed();
        if (!mute)
            playSound(theInd, position, acc);

        theStaffNote = new StaffNote(theInd, position, acc);

        theStaffNote.setMuteNote(mute);
        if (!mute) {
            theStaffNote.setImage(
                    ImageLoader.getSpriteFX(
                            theInd.imageIndex()));
        } else {
            theStaffNote.setImage(
                    ImageLoader.getSpriteFX(
                            theInd.imageIndex().gray()));
        }

        accidental = new StaffAccidental(theStaffNote);
        accidental.setImage(
                ImageLoader.getSpriteFX(
                        Staff.switchAcc(acc)));

        theImages.remove(silhouette);
        accList.remove(accSilhouette);

        if (!theImages.contains(theStaffNote))
            theImages.add(theStaffNote);

        if (!accList.contains(accidental))
            accList.add(accidental);

        StaffNoteLine temp = theStaff.getSequence().getLine(
                line + StateMachine.getMeasureLineNum());

        if (temp.isEmpty())
            temp.setVolume(Values.DEFAULT_VELOCITY);

        if (!temp.contains(theStaffNote))
            temp.add(theStaffNote);
    }



    /**
     * The method that is called when the right mouse button is pressed.
     * This is generally the signal to remove the instrument from that line.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected. (currently not actually used, but
     * can be extended later to selectively remove instruments.
     */
    private void rightMousePressed(InstrumentIndex theInd) {

        removeNote();

    }

    /**
     * This removes a note.
     */
    private void removeNote() {
        theImages.remove(silhouette);
        if (!theImages.isEmpty())
            theImages.remove(theImages.size() - 1);

        accList.remove(accSilhouette);
        if (!accList.isEmpty())
            accList.remove(accList.size() - 1);

        StaffNoteLine temp = theStaff.getSequence().getLine(
                line + StateMachine.getMeasureLineNum());
        if (!temp.isEmpty())
            temp.remove(temp.size() - 1);
    }


    /**
     * The method that is called when the mouse enters the object.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void mouseEntered(InstrumentIndex theInd) {
        StateMachine.setFocusPane(this);
        updateAccidental();
        silhouette.setImage(
                ImageLoader.getSpriteFX(
                        theInd.imageIndex().silhouette()));
        if (!theImages.contains(silhouette))
            theImages.add(silhouette);
        accSilhouette.setImage(
                ImageLoader.getSpriteFX(
                        Staff.switchAcc(acc).silhouette()));
        if (!accList.contains(accSilhouette))
            accList.add(accSilhouette);
    }

    /**
     * The method that is called when the mouse exits the object.
     * @param children List of Nodes that we have here, hopefully full of
     * ImageView-type objects.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void mouseExited(InstrumentIndex theInd) {

        theImages.remove(silhouette);
        accList.remove(accSilhouette);

    }

    /**
     * Updates how much we want to sharp / flat a note.
     */
    public void updateAccidental() {
        if (!mouseInFrame)
            return;
        if (StateMachine.isAltPressed() && StateMachine.isCtrlPressed())
            acc = -2;
        else if (StateMachine.isCtrlPressed() && StateMachine.isShiftPressed())
            acc = 2;
        else if (StateMachine.isShiftPressed())
            acc = 1;
        else if (StateMachine.isAltPressed() || StateMachine.isCtrlPressed())
            acc = -1;
        else
            acc = 0;

        switch (acc) {
        case 2:
            accSilhouette.setImage(ImageLoader.getSpriteFX(
                    ImageIndex.DOUBLESHARP_SIL));
            break;
        case 1:
            accSilhouette.setImage(ImageLoader.getSpriteFX(
                    ImageIndex.SHARP_SIL));
            break;
        case -1:
            accSilhouette.setImage(ImageLoader.getSpriteFX(
                    ImageIndex.FLAT_SIL));
            break;
        case -2:
            accSilhouette.setImage(ImageLoader.getSpriteFX(
                    ImageIndex.DOUBLEFLAT_SIL));
            break;
        default:
            accSilhouette.setImage(null);
            accList.remove(accSilhouette);
            break;
        }

        if (acc != 0 && !accList.contains(accSilhouette))
            accList.add(accSilhouette);

        if ((Settings.debug & 0b01) == 0b01) {
            printID();
        }
    }

    /**
     * Prints the line, position, and accidental of this StackPane.
     */
    private void printID() {
        System.out.println("Line: " + (StateMachine.getMeasureLineNum()
                + line));
        System.out.println("Position: " + position);
        System.out.println("Accidental: " + acc);
    }

    /**
     * Plays a sound given an index and a position.
     * @param theInd The index at which this instrument is located at.
     * @param pos The position at which this note is located at.
     * @param acc The sharp / flat that we want to play this note at.
     */
    private static void playSound(InstrumentIndex theInd, int pos, int acc) {
        SoundfontLoader.playSound(Values.staffNotes[pos].getKeyNum(),
                theInd, acc);
    }

    /**
     * Sets the amount that we want to sharp / flat a note.
     * @param accidental Any integer between -2 and 2.
     */
    public void setAcc(int accidental) {
        acc = accidental;
    }

    /**
     * @return The amount that a note is to be offset from its usual position.
     */
    public int getAcc() {
        return acc;
    }

    /**
     * @return The line that this handler is located on.
     */
    public int getLine() {
        return line;
    }

    /**
     * @return Whether the mouse is currently in the frame.
     */
    public boolean hasMouse() {
        return mouseInFrame;
    }

    /**
     * Called whenever we request a redraw of the staff.
     */
    public void redrawReaction() {
        System.out.println("Redraw");
    }

}
