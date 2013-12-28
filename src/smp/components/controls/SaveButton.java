package smp.components.controls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import smp.components.Values;
import smp.components.general.ImagePushButton;
import smp.components.staff.Staff;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.staff.sequences.StaffSequence;
import smp.fx.SMPFXController;
import smp.stateMachine.StateMachine;

/**
 * This is the button that saves a song.
 * @author RehdBlob
 * @since 2013.09.28
 *
 */
public class SaveButton extends ImagePushButton {

    /**
     * Sort of hacky move here, but we're going to save a few songs
     * in a different format, so this exists.
     */
    private boolean saveTxt = false;


    /**
     * Default constructor.
     * @param i This is the <code>ImageView</code> object that holds
     * the save button.
     */
    public SaveButton(ImageView i) {
        super(i);
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
        if (!saveTxt) {
            saveObject();
        } else {
            saveTxt();
        }
    }


    /**
     * Saves in object file format. Makes decent use of serialization.
     * There are some issues with this because if one changes the
     * staff sequence class, there are going to be issues loading the file.
     */
    private void saveObject() {
        try {
            FileChooser f = new FileChooser();
            f.setInitialDirectory(new File(System.getProperty("user.dir")));
            f.setInitialFileName(SMPFXController.getSongName().getText());
            f.getExtensionFilters().add(new ExtensionFilter(
                    "Text file", "*.txt"));
            File outputFile = f.showSaveDialog(null);
            if (outputFile == null)
                return;
            FileOutputStream f_out = new
                    FileOutputStream(outputFile);
            ObjectOutputStream o_out = new
                    ObjectOutputStream(f_out);
            StaffSequence out = theStaff.getSequence();
            out.setTempo(StateMachine.getTempo());
            o_out.writeObject(out);
            o_out.close();
            f_out.close();
            StateMachine.setModified(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Saves in text file format. */
    private void saveTxt() {
        try {
            String outputFile = SMPFXController.getSongName().getText();
            outputFile = outputFile + "TXT.txt";
            PrintStream p = new PrintStream(outputFile);
            StaffSequence out = theStaff.getSequence();
            out.setTempo(StateMachine.getTempo());
            p.println(Values.VERSION);
            for (StaffNoteLine s : out.getTheLines()) {
                p.println(s);
            }
            p.println(out.getTempo());
            p.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
