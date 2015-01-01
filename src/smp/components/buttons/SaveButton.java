package smp.components.buttons;

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
import smp.ImageLoader;
import smp.components.Values;
import smp.components.general.ImagePushButton;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.staff.sequences.StaffSequence;
import smp.fx.SMPFXController;
import smp.stateMachine.ProgramState;
import smp.stateMachine.StateMachine;

/**
 * This is the button that saves a song.
 *
 * @author RehdBlob
 * @since 2013.09.28
 *
 */
public class SaveButton extends ImagePushButton {

    /**
     * Sort of hacky move here, but we're going to save a few songs in a
     * different format, so this exists.
     */
    private boolean saveTxt = false;

    /**
     * Default constructor.
     *
     * @param i
     *            This is the <code>ImageView</code> object that holds the save
     *            button.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public SaveButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState();
        if (curr == ProgramState.EDITING || curr == ProgramState.SONG_PLAYING)
            saveSong();
        else if (curr == ProgramState.ARR_EDITING
                || curr == ProgramState.ARR_PLAYING)
            saveArrangement();
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        // do nothing.
    }

    /** This saves the song. */
    private void saveSong() {
        saveObject();
    }

    private void saveArrangement() {

    }

    /**
     * Saves in object file format. Makes decent use of serialization. There are
     * some issues with this because if one changes the staff sequence class,
     * there are going to be issues loading the file.
     */
    private void saveObject() {
        try {
            FileChooser f = new FileChooser();
            f.setInitialDirectory(new File(System.getProperty("user.dir")));
            f.setInitialFileName(controller.getSongName().getText() + ".txt");
            f.getExtensionFilters().addAll(
                    new ExtensionFilter("Text file", "*.txt"),
                    new ExtensionFilter("All files", "*"));
            File outputFile = f.showSaveDialog(null);
            if (outputFile == null)
                return;
            FileOutputStream f_out = new FileOutputStream(outputFile);
            ObjectOutputStream o_out = new ObjectOutputStream(f_out);
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

    /** Saves in text file format. CURRENTLY BROKEN. */
    private void saveTxt() {
        try {
            String outputFile = controller.getSongName().getText();
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
