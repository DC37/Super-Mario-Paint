package smp.presenters.options;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class OptionsOKPresenter {
	Button optionsOK;
	
	public OptionsOKPresenter(Button optionsOK) {
		this.optionsOK = optionsOK;
		this.optionsOK.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
            	Stage stage = (Stage) optionsOK.getScene().getWindow();
            	stage.close();
            }
        });
	}
}
