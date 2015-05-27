package smp.components.buttons;

import java.io.EOFException;
import java.io.StreamCorruptedException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import smp.ImageLoader;
import smp.components.general.ImagePushButton;
import smp.components.general.Utilities;
import smp.components.staff.sequences.StaffArrangement;
import smp.components.staff.sequences.StaffSequence;
import smp.components.staff.sequences.mpc.MPCDecoder;
import smp.fx.Dialog;
import smp.fx.SMPFXController;
import smp.stateMachine.ProgramState;
import smp.stateMachine.StateMachine;

/**
 * This is the button that loads a song.
 *
 * @author RehdBlob
 * @since 2013.09.28
 */
public class LoadButton extends ImagePushButton {

    /**
     * The file directory that we are currently located in. We'll start in the
     * user directory.
     */
    private File init = new File(System.getProperty("user.dir"));

    /**
     * Default constructor.
     *
     * @param i
     *            This is the <code>ImageView</code> object that will house the
     *            Load button.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public LoadButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        load();
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        // do nothing.
    }

    /** This loads the song or arrangement. */
    private void load() {
        ProgramState curr = StateMachine.getState();
        if (curr == ProgramState.EDITING)
            loadSong();
        else if (curr == ProgramState.ARR_EDITING)
            loadArrangement();
    }

    /** This loads a song. */
    private void loadSong() {
        boolean cont = true;
        if (StateMachine.isSongModified())
            cont = Dialog
                    .showYesNoDialog("The current song has been modified!\n"
                            + "Load anyway?");
        File inputFile = null;
        if (cont) {
            try {
                FileChooser f = new FileChooser();
                f.setInitialDirectory(init);
                inputFile = f.showOpenDialog(null);
                if (inputFile == null)
                    return;
                init = new File(inputFile.getParent());
                StaffSequence loaded = Utilities.loadSong(inputFile);
                String fname = Utilities.populateStaff(loaded, inputFile,
                        false, theStaff, controller);
                controller.getNameTextField().setText(fname);
                StateMachine.setSongModified(false);
            } catch (ClassCastException | EOFException | StreamCorruptedException e) {
                try {
                    StaffSequence loaded = MPCDecoder.decode(inputFile);
                    String fname = Utilities.populateStaff(loaded, inputFile,
                            true, theStaff, controller);
                    controller.getNameTextField().setText(fname);
                    StateMachine.setSongModified(false);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Dialog.showDialog("Not a valid song file.");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** This loads an arrangement. */
    private void loadArrangement() {
        boolean cont = true;
        if (StateMachine.isSongModified() || StateMachine.isArrModified()) {
            if (StateMachine.isSongModified() && StateMachine.isArrModified()) {
                cont = Dialog
                        .showYesNoDialog("The current song and arrangement\n"
                                + "have both been modified!\nLoad anyway?");
            } else if (StateMachine.isSongModified()) {
                cont = Dialog
                        .showYesNoDialog("The current song has been modified!\n"
                                + "Load anyway?");
            } else if (StateMachine.isArrModified()) {
                cont = Dialog
                        .showYesNoDialog("The current arrangement has been\n"
                                + "modified! Load anyway?");
            }
        }
        File inputFile = null;
        if (cont) {
            try {
                FileChooser f = new FileChooser();
                f.setInitialDirectory(init);
                inputFile = f.showOpenDialog(null);
                if (inputFile == null)
                    return;
                StaffArrangement loaded = Utilities.loadArrangement(inputFile);
                Utilities.populateStaffArrangement(loaded, inputFile, false,
                        theStaff, controller);
                StateMachine.setSongModified(false);
                StateMachine.setArrModified(false);
            } catch (ClassCastException | ClassNotFoundException | EOFException
                    | StreamCorruptedException e) {
                try {
                    StaffArrangement loaded = MPCDecoder
                            .decodeArrangement(inputFile);
                    Utilities.populateStaffArrangement(loaded, inputFile, true,
                            theStaff, controller);
                    StateMachine.setSongModified(false);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Dialog.showDialog("Not a valid arrangement file.");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
