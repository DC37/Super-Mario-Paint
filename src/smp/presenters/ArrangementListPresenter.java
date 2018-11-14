package smp.presenters;

import java.io.File;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import smp.models.staff.StaffArrangement;
import smp.models.staff.StaffSequence;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.StateMachine;
import smp.models.stateMachine.Variables;
import smp.presenters.api.load.Utilities;
import smp.presenters.api.reattachers.ArrangementReattacher;

public class ArrangementListPresenter {
	
	//TODO: auto-add these model comments
	//====Models====
	private ObjectProperty<StaffArrangement> theArrangement;
	private ObjectProperty<ProgramState> programState;
	private IntegerProperty arrangementListSelectedIndex;
	private ObjectProperty<StaffSequence> theSequence;
	
	private ListView<String> arrangementList;
	
	private ArrangementReattacher arrangementReattacher;

	ListProperty<String> listProperty = new SimpleListProperty<>();

	public ArrangementListPresenter(ListView<String> arrangementList) {
		this.arrangementList = arrangementList;
		
		this.theArrangement = Variables.theArrangement;
		this.theSequence = Variables.theSequence;
		this.arrangementListSelectedIndex = Variables.arrangementListSelectedIndex;
		this.programState = StateMachine.getState();
		this.arrangementReattacher = new ArrangementReattacher(this.theArrangement);
		initializeArrangementList();
		setupViewUpdater();
	}

	/**
	 * Adds in the listener behaviour for the arrangement list.
	 */
	private void initializeArrangementList() {
		arrangementList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (programState.get() == ProgramState.ARR_PLAYING)
					return;
				int x = arrangementList.getSelectionModel().getSelectedIndex();
				if (x != -1) {
					ObservableList<StaffSequence> s = theArrangement.get().getTheSequences();
					ObservableList<File> f = theArrangement.get().getTheSequenceFiles();
					theSequence.set(Utilities.loadSequenceFromArrangement(f.get(x)));
					s.set(x, theSequence.get());
				}
			}
		});
	}
	
	private void setupViewUpdater() {
		this.arrangementList.setEditable(true);
		this.arrangementList.setStyle("-fx-font: 8pt \"Arial\";");
		this.arrangementList.setVisible(false);

		this.arrangementReattacher.setOnReattachListener(new ChangeListener<StaffArrangement>() {

			@Override
			public void changed(ObservableValue<? extends StaffArrangement> observable, StaffArrangement oldValue,
					StaffArrangement newValue) {
				listProperty.set(theArrangement.get().getTheSequenceNames());
				arrangementList.itemsProperty().unbind();
				arrangementList.itemsProperty().bind(listProperty);
				
				if(theArrangement.get().getTheSequenceNames().size() > 0)
					arrangementList.getSelectionModel().select(0);
			}
		});
		
		arrangementList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				arrangementListSelectedIndex.set(newValue.intValue());
			}
		});
		this.arrangementListSelectedIndex.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				arrangementList.getSelectionModel().select(newValue.intValue());
			}
		});

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
