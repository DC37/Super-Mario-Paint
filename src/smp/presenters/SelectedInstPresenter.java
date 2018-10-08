package smp.presenters;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.ImageView;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.InstrumentIndex;
import smp.models.stateMachine.Variables;

public class SelectedInstPresenter {

    
	//TODO: auto-add these model comments
	//====Models====
	private ObjectProperty<InstrumentIndex> selectedInstrument;

    /** The picture of the currently-selected instrument. */
    private ImageView selectedInst;
    
    /** This is the image loader class. */
    private ImageLoader il;
    
	public SelectedInstPresenter(ImageView selectedInst) {
		this.selectedInst = selectedInst;
		this.selectedInstrument = Variables.selectedInstrument;
		setupViewUpdater();
	}
	
    private void setupViewUpdater() {
    	this.selectedInstrument.addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				InstrumentIndex i = (InstrumentIndex) newValue;
				selectedInst.setImage(il.getSpriteFX(ImageIndex.valueOf(i.toString())));
			}
		});
	}
}