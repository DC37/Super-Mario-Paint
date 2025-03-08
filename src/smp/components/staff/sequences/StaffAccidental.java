package smp.components.staff.sequences;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * This denotes a flat / sharp / etc on the staff.
 * @author RehdBlob
 * @since 2013.08.21
 */
public class StaffAccidental {

    /**
     * This is the link to the note that this flat / sharp
     * is linked to.
     */
    private StaffNote theNote;

    private Image image;

    /**
     * Creates a StaffAccidental object which is really just
     * an ImageView with special properties that allow it to
     * be compared.
     * @param note The StaffNote that we want to link this
     * image with.
     */
    public StaffAccidental(StaffNote note) {
        theNote = note;
    }
    
    public ImageView toImageView() {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(32);
        imageView.setFitHeight(32);
        return imageView;
    }
    
    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StaffAccidental)) {
            return false;
        } else {
            StaffAccidental other = (StaffAccidental) o;
            return other.theNote.equals(theNote);
        }
    }

}
