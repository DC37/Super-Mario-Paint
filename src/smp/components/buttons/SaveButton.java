package smp.components.buttons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import smp.ImageLoader;
import smp.components.general.ImagePushButton;
import smp.components.staff.sequences.StaffArrangement;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.staff.sequences.StaffSequence;
import smp.fx.SMPFXController;
import smp.stateMachine.ProgramState;
import smp.stateMachine.Settings;
import smp.stateMachine.StateMachine;
import smp.stateMachine.TimeSignature;

/**
 * This is the button that saves a song.
 *
 * @author RehdBlob
 * @since 2013.09.28
 *
 */
public class SaveButton extends ImagePushButton {

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

    /** Saves the arrangement. */
    private void saveArrangement() {
        try {
            FileChooser f = new FileChooser();
            f.setInitialDirectory(new File(System.getProperty("user.dir")));
            f.setInitialFileName(controller.getNameTextField().getText()
                    + ".txt");
            f.getExtensionFilters().addAll(
                    new ExtensionFilter("Text file", "*.txt"),
                    new ExtensionFilter("All files", "*"));
            File outputFile = f.showSaveDialog(null);
            if (outputFile == null)
                return;
            FileOutputStream f_out = new FileOutputStream(outputFile);
            ObjectOutputStream o_out = new ObjectOutputStream(f_out);
            StaffArrangement out = theStaff.getArrangement();
            out.getTheSequenceNames().clear();
            out.setTheSequenceNames(theStaff.getArrangementList().getItems());
            o_out.writeObject(out);
            o_out.close();
            f_out.close();
            theStaff.setArrangementFile(outputFile);
            StateMachine.setArrModified(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current song to disk.
     */
    private void saveSong() {
        try {
            FileChooser f = new FileChooser();
            f.setInitialDirectory(new File(System.getProperty("user.dir")));
            f.setInitialFileName(controller.getNameTextField().getText()
                    + ".txt");
            f.getExtensionFilters().addAll(
                    new ExtensionFilter("Text file", "*.txt"),
                    new ExtensionFilter("All files", "*"));
            File outputFile = f.showSaveDialog(null);
            if (outputFile == null)
                return;
            FileOutputStream f_out = new FileOutputStream(outputFile);
            StaffSequence out = theStaff.getSequence();
            out.setTempo(StateMachine.getTempo());
            if (Settings.SAVE_OBJECTS) {
                saveObject(f_out, out);
            } else {
                saveTxt(f_out, out);
            }
            f_out.close();
            theStaff.setSequenceFile(outputFile);
            StateMachine.setSongModified(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves in object file format. Makes decent use of serialization. There are
     * some issues with this because if one changes the staff sequence class,
     * there are going to be issues loading the file.
     */
    private void saveObject(FileOutputStream f_out, StaffSequence out)
            throws IOException {
        ObjectOutputStream o_out = new ObjectOutputStream(f_out);
        o_out.writeObject(out);
        o_out.close();
    }

    /** Saves in text file format. */
    private void saveTxt(FileOutputStream f_out, StaffSequence out)
            throws IOException {
        PrintStream pr = new PrintStream(f_out);
        ArrayList<StaffNoteLine> theLines = out.getTheLines();
        TimeSignature t = out.getTimeSignature();
        if (t == null) {
            t = TimeSignature.FOUR_FOUR;
        }
        pr.printf("TEMPO: %f, EXT: %d, TIME: %s\r\n", out.getTempo(),
                out.getNoteExtensions(), t);

        for (int i = 0; i < theLines.size(); i++) {
            if (theLines.get(i).isEmpty()) {
                continue;
            }
            pr.print("" + (i / t.top() + 1) + ":" + (i % t.top()) + ",");
            ArrayList<StaffNote> line = theLines.get(i).getNotes();
            for (int j = 0; j < line.size(); j++) {
                pr.print(line.get(j) + ",");
            }
            pr.printf("VOL: %d\r\n", theLines.get(i).getVolume());
        }
        pr.close();
    }

}
