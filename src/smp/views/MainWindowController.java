package smp.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import smp.presenters.ArrangementListPresenter;
import smp.presenters.InstLinePresenter;
import smp.presenters.ModeTextPresenter;
import smp.presenters.ScrollbarPresenter;
import smp.presenters.SelectedInstPresenter;
import smp.presenters.SongNamePresenter;
import smp.presenters.TempoBoxPresenter;
import smp.presenters.TempoIndicatorPresenter;
import smp.presenters.buttons.AddButtonPresenter;
import smp.presenters.buttons.DeleteButtonPresenter;
import smp.presenters.buttons.DownButtonPresenter;
import smp.presenters.buttons.LoadButtonPresenter;
import smp.presenters.buttons.ModeButtonPresenter;
import smp.presenters.buttons.NewButtonPresenter;
import smp.presenters.buttons.OptionsButtonPresenter;
import smp.presenters.buttons.SaveButtonPresenter;
import smp.presenters.buttons.TempoMinusPresenter;
import smp.presenters.buttons.TempoPlusPresenter;
import smp.presenters.buttons.UpButtonPresenter;
import smp.presenters.staff.StaffAccidentalsPresenter;
import smp.presenters.staff.StaffExtLinesHighAPresenter;
import smp.presenters.staff.StaffExtLinesHighCPresenter;
import smp.presenters.staff.StaffExtLinesLowAPresenter;
import smp.presenters.staff.StaffExtLinesLowCPresenter;
import smp.presenters.staff.StaffInstrumentsPresenter;
import smp.presenters.staff.StaffMeasureLinesPresenter;
import smp.presenters.staff.StaffMeasureNumbersPresenter;
import smp.presenters.staff.VolumeBarsPresenter;

public class MainWindowController {
	
    /**
     * Location of the Options Window fxml file. 
     * TODO: move fxml string to somewhere in models layer
     */
    private String fxml = "./MainWindow.fxml";
    
    @FXML
    private HBox staffExtLinesHighC;

    @FXML
    private ImageView play;

    @FXML
    private HBox staffExtLinesHighA;

    @FXML
    private ImageView deleteButton;

    @FXML
    private HBox controls;

    @FXML
    private ImageView loadButton;

    @FXML
    private Text tempoIndicator;

    @FXML
    private ImageView rightFastArrow;

    @FXML
    private HBox staffExtLinesLowC;

    @FXML
    private StackPane tempoBox;

    @FXML
    private HBox staffExtLinesLowA;

    @FXML
    private HBox staffInstruments;

    @FXML
    private HBox scrollbarHolder;

    @FXML
    private ImageView tempoPlus;

    @FXML
    private ImageView loop;

    @FXML
    private HBox volumeBars;

    @FXML
    private ImageView optionsButton;

    @FXML
    private ListView<String> arrangementList;

    @FXML
    private ImageView mute;

    @FXML
    private ImageView addButton;

    @FXML
    private ImageView tempoMinus;

    @FXML
    private ImageView leftFastArrow;

    @FXML
    private ImageView stop;

    @FXML
    private ImageView modeButton;

    @FXML
    private Color x1;

    @FXML
    private Font x2;

    @FXML
    private AnchorPane basePane;

    @FXML
    private ImageView selectedInstHolder;

    @FXML
    private StackPane staffPane;

    @FXML
    private TextField songName;

    @FXML
    private Slider scrollbar;

    @FXML
    private HBox staffMeasureLines;

    @FXML
    private HBox staffPlayBars;

    @FXML
    private HBox topBoxRight;

    @FXML
    private ImageView upButton;

    @FXML
    private ImageView clipboardButton;

    @FXML
    private ImageView downButton;

    @FXML
    private Pane topPane;

    @FXML
    private HBox instLine;

    @FXML
    private HBox staffMeasureNumbers;

    @FXML
    private Button secretButton;

    @FXML
    private ImageView saveButton;

    @FXML
    private ImageView clipboardLabel;

    @FXML
    private HBox staffAccidentals;

    @FXML
    private ImageView selectedInst;

    @FXML
    private ImageView leftArrow;

    @FXML
    private ImageView rightArrow;

    @FXML
    private BorderPane mainLayout;

    @FXML
    private ImageView newButton;

    @FXML
    private ImageView muteA;

    @FXML
    private Text modeText;    
    
    /**
     * Initializes the Controller class for the options window
     */
    public void initialize() {
    	new ArrangementListPresenter(arrangementList);
    	new InstLinePresenter(instLine);
    	new ModeTextPresenter(modeText);
    	new SelectedInstPresenter(selectedInst);
    	new ScrollbarPresenter(scrollbar);
    	new SongNamePresenter(songName);
    	new TempoBoxPresenter(tempoBox);
    	new TempoIndicatorPresenter(tempoIndicator);
    	
    	new AddButtonPresenter(addButton);
    	new DeleteButtonPresenter(deleteButton);
    	new DownButtonPresenter(downButton);
    	new LoadButtonPresenter(loadButton);
    	new ModeButtonPresenter(modeButton);
    	new NewButtonPresenter(newButton);
    	new OptionsButtonPresenter(optionsButton);
    	new SaveButtonPresenter(saveButton);
    	new TempoMinusPresenter(tempoMinus);
    	new TempoPlusPresenter(tempoPlus);
    	new UpButtonPresenter(upButton);
    	
    	new StaffAccidentalsPresenter(staffAccidentals);
    	new StaffExtLinesHighAPresenter(staffExtLinesHighA);
    	new StaffExtLinesHighCPresenter(staffExtLinesHighC);
    	new StaffExtLinesLowAPresenter(staffExtLinesLowA);
    	new StaffExtLinesLowCPresenter(staffExtLinesLowC);
    	new StaffInstrumentsPresenter(staffInstruments);
    	new StaffMeasureLinesPresenter(staffMeasureLines);
    	new StaffMeasureNumbersPresenter(staffMeasureNumbers);
    	new VolumeBarsPresenter(volumeBars);
    }
    
    /**
     * @return Location of the Options Window fxml file.
     */
    public String getFXML() {
    	return fxml;
    }
}
