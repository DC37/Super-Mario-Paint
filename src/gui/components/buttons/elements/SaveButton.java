package gui.components.buttons.elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import backend.songs.StaffArrangement;
import backend.songs.StaffNote;
import backend.songs.StaffSequence;
import backend.songs.TimeSignature;
import gui.Dialog;
import gui.ProgramState;
import gui.SMPFXController;
import gui.Settings;
import gui.StateMachine;
import gui.Utilities;
import gui.components.buttons.ImagePushButton;
import gui.loaders.ImageLoader;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

/**
 * This is the button that saves a song.
 *
 * @author RehdBlob
 * @since 2013.09.28
 *
 */
public class SaveButton extends ImagePushButton {

    /** Prevent multiple save windows from opening. */
    private boolean saveInProgress = false;
    
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
        
        /* @since v1.4 to accomodate for those with a smaller screen that may not be able to access it. */
 		ct.getBasePane().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

 			@Override
 			public void handle(KeyEvent event) {
 				if(event.isControlDown() && event.getCode() == KeyCode.S)
 				    save(((Node) event.getSource()).getScene().getWindow());
 			}
 		});
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        save(((Node) event.getSource()).getScene().getWindow());
    }
    
    private void save(Window owner) {
        ProgramState curr = StateMachine.getState();
        if (curr == ProgramState.EDITING || curr == ProgramState.SONG_PLAYING) {
            final ProgramState saveState = curr;
            StateMachine.setState(ProgramState.MENU_OPEN);
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    saveSong(owner);
                    StateMachine.setState(saveState);
                }
                
            });
        } else if (!saveInProgress && (curr == ProgramState.ARR_EDITING
                || curr == ProgramState.ARR_PLAYING)) {
            final ProgramState saveState = curr;
            StateMachine.setState(ProgramState.MENU_OPEN);
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    saveArrangement(owner);
                    StateMachine.setState(saveState);
                }
                
            });
        }
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        // do nothing.
    }

    /** Saves the arrangement. */
    private void saveArrangement(Window owner) {
        String songName = controller.getNameTextField().getText();
        if (!Utilities.legalFileName(songName)) {
            Dialog.showDialog("Illegal file name!\nPlease avoid those characters:\n/, \\, <, >, :, |, *, \", ?, ^", owner);
            return;
        }
        
        try {
            FileChooser f = new FileChooser();
            f.setInitialDirectory(StateMachine.getCurrentDirectory());
            f.setInitialFileName(songName + ".txt");
            f.getExtensionFilters().addAll(
                    new ExtensionFilter("Text file", "*.txt"),
                    new ExtensionFilter("All files", "*"));
            File outputFile = null;
            saveInProgress = true;
            outputFile = f.showSaveDialog(null);
            saveInProgress = false;
            if (outputFile == null)
                return;
            FileOutputStream f_out = new FileOutputStream(outputFile);
            StaffArrangement out = theStaff.getArrangement();
            out.getTheSequenceNames().clear();
            out.setTheSequenceNames(theStaff.getArrangementList().getItems());
            if (Settings.SAVE_OBJECTS) {
                saveArrObject(f_out, out);
            } else {
                saveArrTxt(f_out, out);
            }
            f_out.close();
            StateMachine.setCurrentDirectory(new File(outputFile.getParent()));
            theStaff.setArrangementFile(outputFile);
            StateMachine.setArrModified(false);
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
    private void saveSong(Window owner) {
        String songName = controller.getNameTextField().getText();
        if (!Utilities.legalFileName(songName)) {
            Dialog.showDialog("Illegal file name!\nPlease avoid those characters:\n /, \\, <, >, :, |, *, \", ?, ^", owner);
            return;
        }
        
        try {
            FileChooser f = new FileChooser();
            f.setInitialDirectory(StateMachine.getCurrentDirectory());
            f.setInitialFileName(songName + ".txt");
            f.getExtensionFilters().addAll(
                    new ExtensionFilter("Text file", "*.txt"),
                    new ExtensionFilter("All files", "*"));
            File outputFile = null;
            saveInProgress = true;
            outputFile = f.showSaveDialog(null);
            saveInProgress = false;
            if (outputFile == null)
                return;
            FileOutputStream f_out = new FileOutputStream(outputFile);
            StaffSequence out = theStaff.getSequence();
            out.setTempo(StateMachine.getTempo());
            if (Settings.SAVE_OBJECTS) {
                saveSongObject(f_out, out);
            } else {
                saveSongTxt(f_out, out);
            }
            f_out.close();
            StateMachine.setCurrentDirectory(new File(outputFile.getParent()));
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
     * @param seq
     *            The StaffSequence to write.
     * @throws IOException
     */
    private void saveSongTxt(FileOutputStream f_out, StaffSequence seq)
            throws IOException {
        PrintStream pr = new PrintStream(f_out);
        TimeSignature t = seq.getTimeSignature();
        if (t == null) {
            t = TimeSignature.FOUR_FOUR;
        }
        pr.printf("TEMPO: %f, EXT: %d, TIME: %s, SOUNDSET: %s\r\n", seq.getTempo(),
                Utilities.longFromBool(seq.getNoteExtensions()), t, seq.getSoundset());
        
        for (int i = 0; i < seq.getLength(); i++) {
            if (seq.getLine(i).isEmpty()) {
                continue;
            }
            pr.print("" + (i / t.top() + 1) + ":" + (i % t.top()) + ",");
            ArrayList<StaffNote> line = seq.getLine(i).getNotes();
            for (int j = 0; j < line.size(); j++) {
                pr.print(line.get(j) + ",");
            }
            pr.printf("VOL: %d\r\n", seq.getLine(i).getVolume());
        }
		pr.close();

		// when we change the soundfont for a song in the arr, we should store
		// the new soundfont in cache
		Task<Void> soundsetsTaskSave = new Task<Void>() {
			@Override
			public Void call() {
				ArrayList<String> seqNames = controller.getStaff().getArrangement().getTheSequenceNames();
				String currSeqName = controller.getNameTextField().getText();
				for (String seqName : seqNames) 
					if (seqName.equals(currSeqName)) {
						controller.getSoundfontLoader().storeInCache();
						break;
					}
				return null;
			}
		};
		
		new Thread(soundsetsTaskSave).start();
	}

}
