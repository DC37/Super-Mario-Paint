package smp.presenters.options;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import smp.models.stateMachine.Variables;

public class CurrentSoundfontPresenter {

	//TODO: auto-add these model comments
	//====Models====
	private StringProperty optionsCurrentSoundfont;
	private BooleanProperty optionsBindSoundfont;
	
	private ComboBox<String> currentSoundfont;
	
	public CurrentSoundfontPresenter(ComboBox<String> currentSoundfont) {
		this.currentSoundfont = currentSoundfont;
		this.optionsCurrentSoundfont = Variables.optionsCurrentSoundfont;
		this.optionsBindSoundfont = Variables.optionsBindSoundfont;
		
		this.currentSoundfont.valueProperty().bindBidirectional(optionsCurrentSoundfont);
		
		String[] listOfFiles = null;//TODO: = controller.getSoundfontLoader().getSoundfontsList();
//FIXME:		for (String filename : listOfFiles) {
//			this.currentSoundfont.getItems().add(filename);
//
//			if (filename.equals(StateMachine.getCurrentSoundset().get()))
//				this.currentSoundfont.getSelectionModel().selectLast();
//		}
		this.currentSoundfont.getItems().add("Add Soundfont...");
		this.currentSoundfont.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obsv, String oldVal, String newValue) {
				if(newValue.equals("Add Soundfont...")) {
					FileChooser f = new FileChooser();
					f.setInitialDirectory(new File(System.getProperty("user.dir")));
					f.setTitle("Add Soundfont...");
					f.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("soundfonts (*.sf2)", "*.sf2"));
					final File sf = f.showOpenDialog(null);
					
					// Any programmed selection triggers the ChangedListener again 
					// Wrap in a runLater to avoid a huge stacktrace
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (sf == null) {
								currentSoundfont.getSelectionModel().selectPrevious();
								return;
							}
							
							//TODO:
//							if(!controller.getSoundfontLoader().addSoundfont(sf)) {
//								currentSoundfont.getSelectionModel().selectPrevious();
//								return;
//							}
							// Add soundfont name, or just pick it
							ObservableList<String> soundfonts = currentSoundfont.getItems();
							int i;
							String sfName = sf.getName();
							for (i = 0; i < soundfonts.size() - 1; i++) {
								int compare = soundfonts.get(i).compareTo(sfName);
								if (compare == 0)
									break;
								else if (compare > 0) {
									currentSoundfont.getItems().add(i, sfName);
									break;
								}
							}
							currentSoundfont.getSelectionModel().select(i);
						}
					});
				} 
				
				optionsBindSoundfont.set(newValue.equals(Variables.optionsBindedSoundfont.get()));
				// we don't want to change the state of bindBox's userData
				// so set it back to theStaff.getSequence().getSoundset()
				// TODO: find out what does this do again? bindBox.setUserData(theStaff.getSequence().getSoundset());
			}
		});
	}
}
