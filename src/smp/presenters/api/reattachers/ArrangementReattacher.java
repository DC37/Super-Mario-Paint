package smp.presenters.api.reattachers;

import java.io.File;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import smp.models.staff.StaffArrangement;
import smp.models.staff.StaffSequence;

/**
 * This will reattach listeners to the StaffSequence's member properties when a
 * new sequence gets set.
 * 
 * @author J
 *
 */
public class ArrangementReattacher {

	// ====(not a Model)====
	private ObjectProperty<StaffArrangement> theArrangement;

	public ListChangeListener<StaffSequence> theSeqeuncesListener;
	
	public ListChangeListener<File> theSequenceFilesListener;
	
	public ListChangeListener<String> theSequenceNamesListener;

	/**
	 * The listener for when a new arrangement is set.
	 */
	public ChangeListener<StaffArrangement> onReattachListener;

	public ArrangementReattacher(ObjectProperty<StaffArrangement> theArrangement) {
		this.theArrangement = theArrangement;
		setupReattacher();
	}

	private void setupReattacher() {
		this.theArrangement.addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				StaffArrangement oldArr = (StaffArrangement) oldValue;
				if (theSeqeuncesListener != null)
					oldArr.getTheSequences().removeListener(theSeqeuncesListener);
				if (theSequenceFilesListener != null)
					oldArr.getTheSequenceFiles().removeListener(theSequenceFilesListener);
				if (theSequenceNamesListener != null)
					oldArr.getTheSequenceNames().removeListener(theSequenceNamesListener);

				StaffArrangement newArr = (StaffArrangement) newValue;
				if (theSeqeuncesListener != null)
					newArr.getTheSequences().addListener(theSeqeuncesListener);
				if (theSequenceFilesListener != null)
					newArr.getTheSequenceFiles().addListener(theSequenceFilesListener);
				if (theSequenceNamesListener != null)
					newArr.getTheSequenceNames().addListener(theSequenceNamesListener);
			}
		});
	}

	public void setNewTheSequencesListener(ListChangeListener<StaffSequence> theSequencesListener) {
		if (this.theSeqeuncesListener != null)
			this.theArrangement.get().getTheSequences().removeListener(this.theSeqeuncesListener);
		this.theSeqeuncesListener = theSequencesListener;
		this.theArrangement.get().getTheSequences().addListener(this.theSeqeuncesListener);
	}

	public void setNewTheSequenceFilesListener(ListChangeListener<File> theSequenceFilesListener) {
		if (this.theSequenceFilesListener != null)
			this.theArrangement.get().getTheSequenceFiles().removeListener(this.theSequenceFilesListener);
		this.theSequenceFilesListener = theSequenceFilesListener;
		this.theArrangement.get().getTheSequenceFiles().addListener(this.theSequenceFilesListener);
	}

	public void setNewTheSequenceNamesListener(ListChangeListener<String> theSequenceNamesListener) {
		if (this.theSequenceNamesListener != null)
			this.theArrangement.get().getTheSequenceNames().removeListener(this.theSequenceNamesListener);
		this.theSequenceNamesListener = theSequenceNamesListener;
		this.theArrangement.get().getTheSequenceNames().addListener(this.theSequenceNamesListener);
	}

	public void setOnReattachListener(ChangeListener<StaffArrangement> onReattachListener) {
		this.onReattachListener = onReattachListener;
		this.theArrangement.addListener(onReattachListener);
	}
}
