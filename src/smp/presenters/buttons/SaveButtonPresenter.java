package smp.presenters.buttons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import smp.models.staff.StaffArrangement;
import smp.models.staff.StaffNote;
import smp.models.staff.StaffNoteLine;
import smp.models.staff.StaffSequence;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.Settings;
import smp.models.stateMachine.StateMachine;
import smp.models.stateMachine.TimeSignature;
import smp.models.stateMachine.Variables;
import smp.presenters.api.button.ImagePushButton;
import smp.presenters.api.load.Utilities;

/**
 * This is the button that saves a song.
 *
 * @author RehdBlob
 * @since 2013.09.28
 *
 */
public class SaveButtonPresenter extends ImagePushButton {

	//TODO: auto-add these model comments
	//====Models====
    private ObjectProperty<StaffSequence> theSequence;
	private ObjectProperty<StaffArrangement> theArrangement;
	private ObjectProperty<File> currentDirectory;
	private BooleanProperty songModified;
	private BooleanProperty arrModified;
	private StringProperty theSequenceName;
	private ObjectProperty<ProgramState> programState;

	/**
     * Default constructor.
     *
     * @param saveButton
     *            This is the <code>ImageView</code> object that holds the save
     *            button.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public SaveButtonPresenter(ImageView saveButton) {
        super(saveButton);
        this.theSequence = Variables.theSequence;
        this.theArrangement = Variables.theArrangement;
        this.currentDirectory = StateMachine.getCurrentDirectory();
        this.songModified = StateMachine.getSongModified();
        this.arrModified = StateMachine.getArrModified();
        this.theSequenceName = Variables.theSequenceName;
        this.programState = StateMachine.getState();
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        ProgramState curr = this.programState.get();
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
            f.setInitialDirectory(StateMachine.getCurrentDirectory().get());
			f.setInitialFileName(this.theSequenceName.get() + ".txt");
            f.getExtensionFilters().addAll(
                    new ExtensionFilter("Text file", "*.txt"),
                    new ExtensionFilter("All files", "*"));
            File outputFile = f.showSaveDialog(null);
            if (outputFile == null)
                return;
            FileOutputStream f_out = new FileOutputStream(outputFile);
            StaffArrangement out = this.theArrangement.get();
            //TODO: put listeners on theSequenceNames model in theArrangement
            out.getTheSequenceNames().clear();
            out.setTheSequenceNames(theStaff.getArrangementList().getItems());
            if (Settings.SAVE_OBJECTS) {
                saveArrObject(f_out, out);
            } else {
                saveArrTxt(f_out, out);
            }
            f_out.close();
            this.currentDirectory.set(new File(outputFile.getParent()));
//            theStaff.setArrangementFile(outputFile); ??
            this.arrModified.set(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves an arrangement to disk in human readable text format.
     *
     * @param f_out
     *            The FileOutputStream to write in.
     * @param out
     *            The StaffArrangement to write.
     */
    private void saveArrTxt(FileOutputStream f_out, StaffArrangement out) {
        PrintStream pr = new PrintStream(f_out);
        for (String s : out.getTheSequenceNames()) {
            pr.println(s);
        }
        pr.close();
    }

    /**
     * Saves arrangement in object format.
     *
     * @param f_out
     *            FileOutputStream to write in.
     * @param out
     *            The output arrangement file
     * @throws IOException
     * @deprecated
     */
    private void saveArrObject(FileOutputStream f_out, StaffArrangement out)
            throws IOException {
        ObjectOutputStream o_out = new ObjectOutputStream(f_out);
        o_out.writeObject(out);
        o_out.close();
    }

    /**
     * Saves the current song to disk.
     */
    private void saveSong() {
		try {
			FileChooser f = new FileChooser();
			f.setInitialDirectory(this.currentDirectory.get());
			f.setInitialFileName(this.theSequenceName.get() + ".txt");
            f.getExtensionFilters().addAll(
                    new ExtensionFilter("Text file", "*.txt"),
                    new ExtensionFilter("All files", "*"));
            File outputFile = f.showSaveDialog(null);
            if (outputFile == null)
                return;
            FileOutputStream f_out = new FileOutputStream(outputFile);
            StaffSequence out = this.theSequence.get();
            //TODO: put listeners on tempo model in theSequence
            out.setTempo(StateMachine.getTempo().get());
            if (Settings.SAVE_OBJECTS) {
                saveSongObject(f_out, out);
            } else {
                saveSongTxt(f_out, out);
            }
            f_out.close();
            this.currentDirectory.set(new File(outputFile.getParent()));
//            theStaff.setSequenceFile(outputFile); ??
            this.songModified.set(false);
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
     *
     * @param f_out
     *            FileOutputStream for saving into a file.
     * @param out
     *            The StaffSequence to write.
     * @throws IOException
     * @deprecated
     */
    private void saveSongObject(FileOutputStream f_out, StaffSequence out)
            throws IOException {
        ObjectOutputStream o_out = new ObjectOutputStream(f_out);
        o_out.writeObject(out);
        o_out.close();
    }

    /**
     * Saves in text file format.
     *
     * @param f_out
     *            The FileOutputStream to write in.
     * @param out
     *            The StaffSequence to write.
     * @throws IOException
     */
    private void saveSongTxt(FileOutputStream f_out, StaffSequence out)
            throws IOException {
        PrintStream pr = new PrintStream(f_out);
        ObservableList<StaffNoteLine> theLines = out.getTheLines();
        TimeSignature t = out.getTimeSignature().get();
        if (t == null) {
            t = TimeSignature.FOUR_FOUR;
        }
        pr.printf("TEMPO: %f, EXT: %d, TIME: %s, SOUNDSET: %s\r\n", out.getTempo().get(),
                Utilities.longFromBool(out.getNoteExtensions()), t, out.getSoundset().get());
        
        for (int i = 0; i < theLines.size(); i++) {
            if (theLines.get(i).isEmpty()) {
                continue;
            }
            pr.print("" + (i / t.top() + 1) + ":" + (i % t.top()) + ",");
            ObservableList<StaffNote> line = theLines.get(i).getNotes();
            for (int j = 0; j < line.size(); j++) {
                pr.print(line.get(j) + ",");
            }
            pr.printf("VOL: %d\r\n", theLines.get(i).getVolume());
        }
		pr.close();

		// when we change the soundfont for a song in the arr, we should store
		// the new soundfont in cache
		//TODO: add back in
//		Task<Void> soundsetsTaskSave = new Task<Void>() {
//			@Override
//			public Void call() {
//				ArrayList<String> seqNames = controller.getStaff().getArrangement().getTheSequenceNames();
//				String currSeqName = controller.getNameTextField().getText();
//				for (String seqName : seqNames) 
//					if (seqName.equals(currSeqName)) {
//						controller.getSoundfontLoader().storeInCache();
//						break;
//					}
//				return null;
//			}
//		};
//		
//		new Thread(soundsetsTaskSave).start();
	}

}
