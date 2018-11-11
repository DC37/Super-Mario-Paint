package smp.presenters;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.StateMachine;
import smp.models.stateMachine.Variables;

public class ArrangementListPresenter {
	
	//TODO: auto-add these model comments
	//====Models====
	private ObservableList<String> arrangementListModel;
	private ObjectProperty<MultipleSelectionModel<String>> selectionModelProperty;
	private ObjectProperty<ProgramState> programState;
	
	private ListView<String> arrangementList;
	
	public ArrangementListPresenter(ListView<String> arrangementList) {
		this.arrangementList = arrangementList;
		this.arrangementListModel = Variables.arrangementList;
		this.selectionModelProperty = Variables.selectionModelProperty;
		this.programState = StateMachine.getState();
		setupViewUpdater();
	}

	private void setupViewUpdater() {
		this.arrangementList.setEditable(true);
		this.arrangementList.setStyle("-fx-font: 8pt \"Arial\";");
		this.arrangementList.setVisible(false);
		
		ListProperty<String> listProperty = new SimpleListProperty<>();
		listProperty.set(arrangementListModel);
		arrangementList.itemsProperty().bind(listProperty);
		arrangementList.selectionModelProperty().bindBidirectional(this.selectionModelProperty);
		
		this.programState.addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				if (newValue.equals(ProgramState.EDITING))
					arrangementList.setVisible(false);
				else if (newValue.equals(ProgramState.ARR_EDITING))
					arrangementList.setVisible(true);
			}
		});
	}
}
