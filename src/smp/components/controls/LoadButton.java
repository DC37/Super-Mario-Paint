package smp.components.controls;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.components.general.ImagePushButton;
import smp.components.staff.Staff;
import smp.components.staff.sequences.StaffSequence;
import smp.fx.SMPFXController;

/**
 * This is the button that loads a song.
 * @author RehdBlob
 * @since 2013.09.28
 */
public class LoadButton extends ImagePushButton {

    /** This is the staff. */
    private Staff theStaff;

    /**
     * Default constructor.
     * @param i This is the <code>ImageView</code> object
     * that will house the Load button.
     */
    public LoadButton(ImageView i) {
        super(i);
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        load();
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

    /** This loads the song. */
    private void load() {
        try {
            String outputFile = SMPFXController.getSongName().getText();
            outputFile = outputFile + ".txt";
            FileInputStream f_in = new
                    FileInputStream (outputFile);
            ObjectInputStream o_in = new
                    ObjectInputStream(f_in);
            StaffSequence loaded = (StaffSequence) o_in.readObject();
            theStaff.setSequence(loaded);
            theStaff.getNoteMatrix().redraw();
            o_in.close();
            f_in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
