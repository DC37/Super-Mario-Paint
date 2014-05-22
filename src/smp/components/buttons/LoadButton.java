package smp.components.buttons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import smp.components.Values;
import smp.components.general.ImagePushButton;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.staff.sequences.StaffSequence;
import smp.fx.Dialog;
import smp.fx.SMPFXController;
import smp.stateMachine.StateMachine;

/**
 * This is the button that loads a song.
 * @author RehdBlob
 * @since 2013.09.28
 */
public class LoadButton extends ImagePushButton {

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
        // do nothing.
    }

    /** This loads the song. */
    private void load() {
        boolean cont = true;
        if (StateMachine.isModified())
            cont = Dialog.showYesNoDialog("The current song has been modified!\n"
                    + "Load anyway?");
        if (cont) {
            try {
                FileChooser f = new FileChooser();
                f.setInitialDirectory(new File(System.getProperty("user.dir")));
                File inputFile = f.showOpenDialog(null);
                if (inputFile == null)
                    return;
                FileInputStream f_in = new
                        FileInputStream (inputFile);
                ObjectInputStream o_in = new
                        ObjectInputStream(f_in);
                StaffSequence loaded = (StaffSequence) o_in.readObject();
                normalize(loaded);
                theStaff.setSequence(loaded);
                StateMachine.setTempo(loaded.getTempo());
                theStaff.getControlPanel().updateCurrTempo();
                theStaff.getControlPanel().getScrollbar().setMax(
                        loaded.getTheLines().size()
                        - Values.NOTELINES_IN_THE_WINDOW);
                theStaff.getNoteMatrix().redraw();
                o_in.close();
                f_in.close();
                String fname = inputFile.getName();
                try {
                    fname = fname.substring(0, fname.indexOf("."));
                } catch (IndexOutOfBoundsException e) {
                    // Do nothing
                }
                SMPFXController.getSongName().setText(fname);
                StateMachine.setModified(false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Makes a sequence fit on the screen.
     * @param theSeq The sequence to normalize.
     */
    private void normalize(StaffSequence theSeq) {
        ArrayList<StaffNoteLine> theLines = theSeq.getTheLines();
        while (theLines.size() % 4 != 0 ||
                theLines.size() % Values.NOTELINES_IN_THE_WINDOW != 0) {
            theLines.add(new StaffNoteLine());
        }

    }


}
