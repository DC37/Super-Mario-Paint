package gui.components.buttons.elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.text.ParseException;

import backend.saving.mpc.MPCDecoder;
import backend.songs.StaffArrangement;
import backend.songs.StaffSequence;
import gui.Dialog;
import gui.ProgramState;
import gui.SMPFXController;
import gui.StateMachine;
import gui.Utilities;
import gui.components.buttons.ImagePushButton;
import gui.loaders.ImageLoader;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;

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
        
        // @since v1.4 to accomodate for those with a smaller screen that may not be able to access it.
  		ct.getBasePane().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

  			@Override
  			public void handle(KeyEvent event) {
				if (controller.getNameTextField().focusedProperty().get()) 
				    return; // Disable while textfield is focused
				
  				if(event.isControlDown() && event.getCode() == KeyCode.O)
  					reactPressed(null);
  			}
  		});
    }

    @Override
    protected void reactPressed(MouseEvent event) {
    	Window owner = (event == null) ? null : ((Node) event.getSource()).getScene().getWindow();
        load(owner);
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        // do nothing.
    }

    /** This loads the song or arrangement. */
    private void load(Window owner) {
        ProgramState curr = StateMachine.getState();
        if (curr == ProgramState.EDITING) {
            StateMachine.setState(ProgramState.MENU_OPEN);
            Platform.runLater(new Runnable() {

                @Override
                public void run() {

                    loadSong(owner);
                    StateMachine.setState(ProgramState.EDITING);
                }
                
            });
        } else if (curr == ProgramState.ARR_EDITING) {
            StateMachine.setState(ProgramState.MENU_OPEN);
            Platform.runLater(new Runnable() {

                @Override
                public void run() {

                    loadArrangement(owner);
                    StateMachine.setState(ProgramState.ARR_EDITING);
                    
                }
                
            });
        }
    }

    /** This loads a song. */
    private void loadSong(Window owner) {
        boolean cont = true;
        if (StateMachine.isSongModified())
            cont = Dialog
                    .showYesNoDialog("The current song has been modified!\n"
                            + "Load anyway?", owner);
        File inputFile = null;
        if (cont) {
            try {
                FileChooser f = new FileChooser();
                FileChooser.ExtensionFilter filterTxt = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                FileChooser.ExtensionFilter filterAny = new FileChooser.ExtensionFilter("Any files (*.*)", "*.*");
                f.getExtensionFilters().add(filterTxt);
                f.getExtensionFilters().add(filterAny);
                f.setInitialDirectory(StateMachine.getCurrentDirectory());
                inputFile = f.showOpenDialog(null);
                if (inputFile == null)
                    return;
                StateMachine.setCurrentDirectory(new File(inputFile.getParent()));
                loadSong(inputFile, owner);
            } catch (Exception e) {
                Dialog.showDialog("Not a valid song file.", owner);
            }
        }
    }

    /** This loads a song, given a file. */
    private void loadSong(File inputFile, Window owner) {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Dialog.showDialog("Problem loading file!", owner);
            e.printStackTrace();
        } catch (Exception e) {
            Dialog.showDialog("Not a valid song file.", owner);
        }
    }

    /** This loads an arrangement. */
    private void loadArrangement(Window owner) {
        boolean cont = true;
        if (StateMachine.isSongModified() || StateMachine.isArrModified()) {
            if (StateMachine.isSongModified() && StateMachine.isArrModified()) {
                cont = Dialog
                        .showYesNoDialog("The current song and arrangement\n"
                                + "have both been modified!\nLoad anyway?", owner);
            } else if (StateMachine.isSongModified()) {
                cont = Dialog
                        .showYesNoDialog("The current song has been modified!\n"
                                + "Load anyway?", owner);
            } else if (StateMachine.isArrModified()) {
                cont = Dialog
                        .showYesNoDialog("The current arrangement has been\n"
                                + "modified! Load anyway?", owner);
            }
        }
        File inputFile = null;
        if (cont) {
            try {
                FileChooser f = new FileChooser();
                FileChooser.ExtensionFilter filterTxt = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                FileChooser.ExtensionFilter filterAny = new FileChooser.ExtensionFilter("Any files (*.*)", "*.*");
                f.getExtensionFilters().add(filterTxt);
                f.getExtensionFilters().add(filterAny);
                f.setInitialDirectory(StateMachine.getCurrentDirectory());
                inputFile = f.showOpenDialog(null);
                if (inputFile == null)
                    return;
                StateMachine.setCurrentDirectory(new File(inputFile.getParent()));
                StaffArrangement loaded = Utilities.loadArrangement(inputFile);
                Utilities.normalizeArrangement(loaded, inputFile);
                Utilities.populateStaffArrangement(loaded, inputFile, false,
                        theStaff, controller, owner);
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
                            theStaff, controller, owner);
                    StateMachine.setSongModified(false);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Dialog.showDialog("Not a valid arrangement file.", owner);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
