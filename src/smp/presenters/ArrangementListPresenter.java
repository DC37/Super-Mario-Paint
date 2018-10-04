package smp.presenters;

import javafx.scene.control.ListView;
import smp.models.stateMachine.Variables;

public class ArrangementListPresenter {
	
	ListView<String> arrangementList;
	
	public ArrangementListPresenter(ListView<String> arrangementList) {
		this.arrangementList = arrangementList;
		Variables.setArrangementList(arrangementList.getItems());
	}
}
