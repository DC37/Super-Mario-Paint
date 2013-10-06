package smp.components.controls;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.components.general.ImagePushButton;
import smp.components.staff.Staff;
import smp.fx.SMPFXController;

public class SaveButton extends ImagePushButton {

    /** This is the staff that we want to save a song from. */
    private Staff theStaff;

    /**
     * Default constructor.
     * @param i This is the <code>ImageView</code> object that holds
     * the save button.
     */
    public SaveButton(ImageView i) {
        super(i);
    }

    /**
     * Sets the staff that this button is connected to.
     * @param s The staff we want to set.
     */
    public void setStaff(Staff s) {
        theStaff = s;
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        save();
    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

    /** This saves the song. */
    private void save() {
        try {
            String outputFile = SMPFXController.getSongName().getText();
            outputFile = outputFile + ".txt";
            FileOutputStream f_out = new
                    FileOutputStream("./" + outputFile);
            ObjectOutputStream o_out = new
                    ObjectOutputStream(f_out);
            o_out.writeObject(theStaff.getSequence());
            o_out.close();
            f_out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
