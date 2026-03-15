package backend.songs;

import gui.loaders.ImageIndex;


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
    
    public StaffAccidental(StaffAccidental acc) {
        this(new StaffNote(acc.theNote));
    }
    
    public ImageIndex getImageIndex() {
        switch (theNote.getMuteModifier()) {
        case REGULAR:
            return theNote.getAccidental().imageIndex();
        case MUTE_THIS_PITCH:
            return theNote.getAccidental().imageIndex().alt();
        case MUTE_THIS_INST:
            return theNote.getAccidental().imageIndex().silhouette();
        default:
        	throw new IllegalArgumentException("Unrecognized mute modifier: " + theNote.getMuteModifier());
        }
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
