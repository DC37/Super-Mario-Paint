package smp.presenters.buttons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.text.ParseException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import smp.fx.Dialog;
import smp.models.staff.StaffArrangement;
import smp.models.staff.StaffSequence;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.StateMachine;
import smp.models.stateMachine.Variables;
import smp.presenters.api.button.ImagePushButton;
import smp.presenters.api.load.MPCDecoder;
import smp.presenters.api.load.Utilities;

/**
 * This is the button that loads a song.
 *
 * @author RehdBlob
 * @since 2013.09.28
 */
public class LoadButtonPresenter extends ImagePushButton {

	//TODO: auto-add these model comments
	//====Models====
	private ObjectProperty<StaffSequence> theSequence;
	private ObjectProperty<StaffArrangement> theArrangement;
    private StringProperty theSequenceName;
	private BooleanProperty songModified;
	private BooleanProperty arrModified;
	private ObjectProperty<File> currentDirectory;
	private StringProperty theArrangementName;
	private ObjectProperty<ProgramState> programState;

	/**
     * Default constructor.
     *
     * @param loadButton
     *            This is the <code>ImageView</code> object that will house the
     *            Load button.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public LoadButtonPresenter(ImageView loadButton) {
        super(loadButton);
        this.theSequenceName = Variables.theSequenceName;
        this.theSequence = Variables.theSequence;
        this.theArrangementName = Variables.theArrangementName;
        this.theArrangement = Variables.theArrangement;
        this.songModified = StateMachine.getSongModified();
        this.arrModified = StateMachine.getArrModified();
        this.currentDirectory = StateMachine.getCurrentDirectory();
        this.programState = StateMachine.getState();
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
        ProgramState curr = this.programState.get();
        if (curr == ProgramState.EDITING)
            loadSong();
        else if (curr == ProgramState.ARR_EDITING)
            loadArrangement();
    }

    /** This loads a song. */
    private void loadSong() {
        boolean cont = true;
        if (this.songModified.get())
            cont = Dialog
                    .showYesNoDialog("The current song has been modified!\n"
                            + "Load anyway?");
        File inputFile = null;
        if (cont) {
            try {
                FileChooser f = new FileChooser();
                f.setInitialDirectory(this.currentDirectory.get());
                inputFile = f.showOpenDialog(null);
                if (inputFile == null)
                    return;
                this.currentDirectory.set(new File(inputFile.getParent()));
                loadSong(inputFile);
            } catch (Exception e) {
                Dialog.showDialog("Not a valid song file.");
                e.printStackTrace();
            }
        }
    }

    /** This loads a song, given a file. */
    private void loadSong(File inputFile) {
    	//TODO: only set a new staffsequence for the theSequence model,
    	//TODO: let all the other presenters update themselves...
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
//            String fname = Utilities.populateStaff(loaded, inputFile, false,
//                    theStaff, controller);
            boolean mpc = false;
			String fname = inputFile.getName();
			if (mpc)
				fname = fname.substring(0, fname.lastIndexOf(']'));
			else
				fname = fname.substring(0, fname.lastIndexOf("."));
			
			this.theSequence.set(loaded);
            this.theSequenceName.set(fname);
            this.songModified.set(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Dialog.showDialog("Problem loading file!");
            e.printStackTrace();
        } catch (Exception e) {
            Dialog.showDialog("Not a valid song file.");
            e.printStackTrace();
        }
    }

    /** This loads an arrangement. */
    private void loadArrangement() {
    	//TODO: only set a new staffarrangement for the theArrangement model,
    	//TODO: let all the other presenters update themselves...
        boolean cont = true;
        if (this.songModified.get() || this.arrModified.get()) {
            if (this.songModified.get() && this.arrModified.get()) {
                cont = Dialog
                        .showYesNoDialog("The current song and arrangement\n"
                                + "have both been modified!\nLoad anyway?");
            } else if (this.songModified.get()) {
                cont = Dialog
                        .showYesNoDialog("The current song has been modified!\n"
                                + "Load anyway?");
            } else if (this.arrModified.get()) {
                cont = Dialog
                        .showYesNoDialog("The current arrangement has been\n"
                                + "modified! Load anyway?");
            }
        }
        File inputFile = null;
        if (cont) {
            try {
                FileChooser f = new FileChooser();
                f.setInitialDirectory(this.currentDirectory.get());
                inputFile = f.showOpenDialog(null);
                if (inputFile == null)
                    return;
                this.currentDirectory.set(new File(inputFile.getParent()));
                StaffArrangement loaded = Utilities.loadArrangement(inputFile);
                Utilities.normalizeArrangement(loaded, inputFile);
//                Utilities.populateStaffArrangement(loaded, inputFile, false,
//                        theStaff, controller);
                this.theArrangement.set(loaded);
                this.theArrangementName.set(inputFile.getName());
                this.songModified.set(false);
                this.arrModified.set(false);
            } catch (ClassNotFoundException | StreamCorruptedException
                    | NullPointerException e) {
                try {
                    StaffArrangement loaded = MPCDecoder
                            .decodeArrangement(inputFile);
                    StateMachine.setCurrentDirectory(new File(inputFile.getParent()));
                    Utilities.normalizeArrangement(loaded, inputFile);
//                    Utilities.populateStaffArrangement(loaded, inputFile, true,
//                            theStaff, controller);
                    this.theArrangement.set(loaded);
                    this.theArrangementName.set(inputFile.getName());
                    this.songModified.set(false);
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
