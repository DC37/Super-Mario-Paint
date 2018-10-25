package smp.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import smp.presenters.options.BindSoundfontPresenter;
import smp.presenters.options.CurrentSoundfontPresenter;
import smp.presenters.options.DefaultVolumePresenter;
import smp.presenters.options.OptionsOKPresenter;
import smp.presenters.options.TempoMultiplierPresenter;

public class OptionsWindowController {
	
    /**
     * Location of the Options Window fxml file. 
     * TODO: move fxml string to somewhere in models layer
     */
    private String fxml = "./OptionsWindow.fxml";
	
    @FXML
    private Slider defaultVolume;

    @FXML
    private ComboBox<String> currentSoundfont;

    @FXML
    private CheckBox bindSoundfont;

    @FXML
    private TextField tempoMultiplier;

    @FXML
    private Button optionsOK;
    
    /**
     * Zero-argument constructor (explicitly declared).
     */
    public OptionsWindowController() {

    }
    
    /**
     * Initializes the Controller class for the options window
     */
    public void initialize() {
    	new DefaultVolumePresenter(defaultVolume);
    	new CurrentSoundfontPresenter(currentSoundfont);
    	new BindSoundfontPresenter(bindSoundfont);
    	new TempoMultiplierPresenter(tempoMultiplier);
    	new OptionsOKPresenter(optionsOK);
    }
    
    /**
     * @return Location of the Options Window fxml file.
     */
    public String getFXML() {
    	return fxml;
    }
}
