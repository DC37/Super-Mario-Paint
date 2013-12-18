package smp.components.controls;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.components.general.ImagePushButton;
import smp.components.staff.Staff;
import smp.components.staff.sequences.StaffSequence;

/**
 * This is the button that creates a new song.
 * @author RehdBlob
 * @since 2013.12.18
 *
 */
public class NewButton extends ImagePushButton {

    /** This is the staff. */
    private Staff theStaff;

    /**
     * Default constructor.
     * @param i This is the <code>ImageView</code> object
     * that will house the Load button.
     */
    public NewButton(ImageView i) {
        super(i);
    }


    @Override
    protected void reactPressed(MouseEvent event) {
        newSong();
    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

    /**
     * Sets the staff that this button is connected to.
     * @param s The staff we want to set.
     */
    public void setStaff(Staff s) {
        theStaff = s;
    }

    /**
     * Creates a new song and clears the staff of all notes.
     * Make sure you save your song first!
     */
    private void newSong() {
        theStaff.setSequence(new StaffSequence());
        theStaff.getNoteMatrix().redraw();
    }

}
