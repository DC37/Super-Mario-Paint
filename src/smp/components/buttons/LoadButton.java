package smp.components.buttons;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import smp.ImageLoader;
import smp.components.Values;
import smp.components.general.ImagePushButton;
import smp.components.general.Utilities;
import smp.components.staff.sequences.StaffArrangement;
import smp.components.staff.sequences.StaffSequence;
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
        if (cont) {
            try {
                FileChooser f = new FileChooser();
                f.setInitialDirectory(new File(System.getProperty("user.dir")));
                File inputFile = f.showOpenDialog(null);
                if (inputFile == null)
                    return;
                StaffSequence loaded = Utilities.loadSong(inputFile);
                Utilities.normalize(loaded);
                theStaff.setSequence(loaded);
                theStaff.setSequenceFile(inputFile);
                StateMachine.setTempo(loaded.getTempo());
                theStaff.getControlPanel().updateCurrTempo();
                if (theStaff.getControlPanel().getScrollbar().valueProperty()
                        .get() > loaded.getTheLines().size()
                        - Values.NOTELINES_IN_THE_WINDOW) {
                    theStaff.setLocation(0);
                }
                theStaff.getControlPanel()
                        .getScrollbar()
                        .setMax(loaded.getTheLines().size()
                                - Values.NOTELINES_IN_THE_WINDOW);
                theStaff.getNoteMatrix().redraw();
                String fname = inputFile.getName();
                try {
                    fname = fname.substring(0, fname.indexOf("."));
                } catch (IndexOutOfBoundsException e) {
                    // Do nothing
                }
                theStaff.setSequenceName(fname);
                controller.getNameTextField().setText(fname);
                StateMachine.setSongModified(false);
            } catch (ClassCastException | EOFException e) {
                e.printStackTrace();
                Dialog.showDialog("Not a valid song file.");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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
        if (cont) {
            try {
                FileChooser f = new FileChooser();
                f.setInitialDirectory(new File(System.getProperty("user.dir")));
                File inputFile = f.showOpenDialog(null);
                if (inputFile == null)
                    return;
                FileInputStream f_in = new FileInputStream(inputFile);
                ObjectInputStream o_in = new ObjectInputStream(f_in);
                StaffArrangement loaded = (StaffArrangement) o_in.readObject();
                o_in.close();
                f_in.close();
                File firstFile = loaded.getTheSequenceFiles().get(0);
                Utilities.loadSequenceFromArrangement(firstFile, theStaff);
                String fname = inputFile.getName();
                try {
                    fname = fname.substring(0, fname.indexOf("."));
                } catch (IndexOutOfBoundsException e) {
                    // Do nothing
                }
                theStaff.setArrangement(loaded);
                theStaff.setArrangementName(fname);
                theStaff.getArrangementList().getItems().clear();
                theStaff.getArrangementList().getItems()
                        .addAll(loaded.getTheSequenceNames());

                ArrayList<File> files = loaded.getTheSequenceFiles();
                ArrayList<StaffSequence> seq = new ArrayList<StaffSequence>();
                for (int i = 0; i < files.size(); i++) {
                    try {
                        seq.add(Utilities.loadSong(files.get(i)));
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }
                theStaff.getArrangement().setTheSequences(seq);

                controller.getNameTextField().setText(fname);
                StateMachine.setSongModified(false);
                StateMachine.setArrModified(false);
            } catch (ClassCastException | EOFException e) {
                e.printStackTrace();
                Dialog.showDialog("Not a valid arrangement file.");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
