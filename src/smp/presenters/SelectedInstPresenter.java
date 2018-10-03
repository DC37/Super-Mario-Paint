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
	
    /** The picture of the currently-selected instrument. */
    private ImageView selectedInst;

    /** This is the image loader class. */
    private ImageLoader il;
    
	public SelectedInstPresenter(ImageView selectedInst) {
		this.selectedInst = selectedInst;
		setupViewUpdater();
	}
	
    private void setupViewUpdater() {
    	ObjectProperty<InstrumentIndex> inst = Variables.selectedInstrument;
		inst.addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				InstrumentIndex i = (InstrumentIndex) newValue;
				selectedInst.setImage(il.getSpriteFX(ImageIndex.valueOf(i.toString())));
			}
		});
	}
}