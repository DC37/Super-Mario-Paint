package smp.components.topPanel;

import smp.ImageIndex;
import smp.ImageLoader;
import javafx.scene.image.ImageView;

/**
 * The picture for the selected instrument.
 * @author RehdBlob
 * @since 2012.08.20
 */
public class SelectedInstrument extends ImageView {
	
	public void setPicture(ImageIndex i) {
		setImage(ImageLoader.getSpriteFX(i));
	}
	
}
