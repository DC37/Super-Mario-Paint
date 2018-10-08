package smp.presenters;

import javafx.scene.control.ListView;
import smp.models.stateMachine.Variables;

public class ArrangementListPresenter {
	
	//TODO: auto-add these model comments
	//====Models====
	//This presenter does not modify any models.
	
	ListView<String> arrangementList;
	
	public ArrangementListPresenter(ListView<String> arrangementList) {
		this.arrangementList = arrangementList;
		Variables.setArrangementList(arrangementList.getItems());
	}
}
