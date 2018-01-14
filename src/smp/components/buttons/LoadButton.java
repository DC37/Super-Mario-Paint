package smp.components.buttons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.text.ParseException;

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
                f.setInitialDirectory(StateMachine.getCurrentDirectory());
                inputFile = f.showOpenDialog(null);
                if (inputFile == null)
                    return;
                StateMachine.setCurrentDirectory(new File(inputFile.getParent()));
                loadSong(inputFile);
            } catch (Exception e) {
                Dialog.showDialog("Not a valid song file.");
            }
        }
    }

    /** This loads a song, given a file. */
    private void loadSong(File inputFile) {
        try {
            StaffSequence loaded = null;
            try {
                loaded = MPCDecoder.decode(inputFile);
            } catch (ParseException e1) {
                loaded = Utilities.loadSong(inputFile);
            }
            if (loaded == null) {
                throw new IOException();
            }
            String fname = Utilities.populateStaff(loaded, inputFile, false,
                    theStaff, controller);
            controller.getNameTextField().setText(fname);
            StateMachine.setNoteExtensions(loaded.getNoteExtensions());
            controller.getInstBLine().updateNoteExtensions();
            StateMachine.setSongModified(false);
            controller.getModifySongManager().reset();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Dialog.showDialog("Problem loading file!");
            e.printStackTrace();
        } catch (Exception e) {
            Dialog.showDialog("Not a valid song file.");
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
                f.setInitialDirectory(StateMachine.getCurrentDirectory());
                inputFile = f.showOpenDialog(null);
                if (inputFile == null)
                    return;
                StateMachine.setCurrentDirectory(new File(inputFile.getParent()));
                StaffArrangement loaded = Utilities.loadArrangement(inputFile);
                Utilities.normalizeArrangement(loaded, inputFile);
                Utilities.populateStaffArrangement(loaded, inputFile, false,
                        theStaff, controller);
                StateMachine.setSongModified(false);
                StateMachine.setArrModified(false);
            } catch (ClassNotFoundException | StreamCorruptedException
                    | NullPointerException e) {
                try {
                    StaffArrangement loaded = MPCDecoder
                            .decodeArrangement(inputFile);
                    StateMachine.setCurrentDirectory(new File(inputFile.getParent()));
                    Utilities.normalizeArrangement(loaded, inputFile);
                    Utilities.populateStaffArrangement(loaded, inputFile, true,
                            theStaff, controller);
                    StateMachine.setSongModified(false);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Dialog.showDialog("Not a valid arrangement file.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
