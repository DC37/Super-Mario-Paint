package gui.components;

import java.io.File;

import gui.StateMachine;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Class that centralizes {@link FileChooser} configuration.
 * 
 * @author
 *     Aura Lesse Programmer
 */
public class FileChooserManager {

	private FileChooserManager() {}
	
	private static FileChooser buildMainDialog() {
		FileChooser f = new FileChooser();
        
		f.setInitialDirectory(StateMachine.getCurrentDirectory());
        f.getExtensionFilters().addAll(
                new ExtensionFilter("Text files (*.txt)", "*.txt"),
                new ExtensionFilter("All files (*.*)", "*.*"));
        
        return f;
	}
	
	private static FileChooser buildSoundfontDialog() {
		FileChooser f = new FileChooser();
		
		f.setInitialDirectory(new File(System.getProperty("user.dir")));
        f.getExtensionFilters().addAll(
        		new ExtensionFilter("Soundfonts (*.sf2)", "*.sf2"));
        
        return f;
	}
	
	/**
	 * Opens the main "Save as..." file dialog.
	 * 
	 * @param owner The {@link Window} that owns this dialog.
	 * @param initialName The initial basename of the file to save. 
	 * @return A {@link File} representing the chosen filename to save.
	 */
	public static File saveAs(Window owner, String initialName) {
		FileChooser f = buildMainDialog();
		f.setTitle("Save as");
		f.setInitialFileName(initialName + ".txt");
        
        return f.showSaveDialog(owner);
	}
	
	/**
	 * Opens the main "Open" file dialog.
	 * 
	 * @param owner The {@link Window} that owns this dialog. 
	 * @return A {@link File} representing the chosen filename to open.
	 */
	public static File open(Window owner) {
		FileChooser f = buildMainDialog();
		f.setTitle("Open");
		
		return f.showOpenDialog(owner);
	}
	
	/**
	 * Opens the ancillary "Open" file dialog for soundfonts.
	 * 
	 * @param owner The {@link Window} that owns this dialog. 
	 * @return A {@link File} representing the chosen soundfont to open.
	 */
	public static File openSoundfont(Window owner) {
		FileChooser f = buildSoundfontDialog();
		f.setTitle("Add Soundfont");
		
		return f.showOpenDialog(owner);
	}
	
}
