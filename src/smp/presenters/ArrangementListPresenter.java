package smp.presenters;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import smp.models.stateMachine.Variables;

public class ArrangementListPresenter {
	
	//TODO: auto-add these model comments
	//====Models====
	private ObservableList<String> arrangementListModel;
	private ObjectProperty<MultipleSelectionModel<String>> selectionModelProperty;
	
	private ListView<String> arrangementList;
	
	public ArrangementListPresenter(ListView<String> arrangementList) {
		this.arrangementList = arrangementList;
		this.arrangementListModel = Variables.arrangementList;
		this.selectionModelProperty = Variables.selectionModelProperty;
		setupViewUpdater();
	}

	private void setupViewUpdater() {
		ListProperty<String> listProperty = new SimpleListProperty<>();
		listProperty.set(arrangementListModel);
		arrangementList.itemsProperty().bind(listProperty);
		arrangementList.selectionModelProperty().bindBidirectional(this.selectionModelProperty);
	}
}
