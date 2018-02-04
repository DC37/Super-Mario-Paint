package smp.clipboard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.InstrumentIndex;

public class InstrumentFilter extends HashSet<InstrumentIndex> {

	/**
	 * wot
	 */
	private static final long serialVersionUID = 1L;
	
	private HBox instrumentLine;
	private ImageLoader il;
	private List<ImageView> filterImages = new ArrayList<>();
	private List<FadeTransition> filterImagesFades = new ArrayList<>();
	
	//the instrument that will be toggled when entering an instrumentimage
	private InstrumentIndex instInFocus;
	
	public InstrumentFilter(HBox instLine, ImageLoader im){
		super();
		instrumentLine = instLine;
		il = im;

		/*
		 * wait for the scene to get initialized then add a keyevent handler
		 * that listens for pressing 'f' to filter. this way we can avoid coding
		 * requestfocus logic
		 */
		instLine.sceneProperty().addListener(new ChangeListener<Scene>() {

			@Override
			public void changed(ObservableValue<? extends Scene> observable, Scene oldScene, Scene newScene) {
				if (oldScene == null && newScene != null) {
					newScene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
						@Override
						public void handle(KeyEvent event) {
							if (event.getCode() == KeyCode.F) {
								if(instInFocus != null)
									toggleInstrumentNoImage(instInFocus);
							}
						}
					});
				}

			}
		});
		
		ObservableList<Node> instrumentImages = instLine.getChildren();
		for (int i = 0; i < instrumentImages.size(); i++) {
			final int index = i;
			final Node instrumentImage = instrumentImages.get(i);
			addFilterImage(instrumentImage);
			
			instrumentImage.setOnMouseEntered(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					instInFocus = indexToInst(index);
					fadeFilterImages(false);
					for(ImageView filterImage : filterImages)
						filterImage.setOpacity(1.0);
				}});
			instrumentImage.setOnMouseExited(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					instInFocus = null;
					fadeFilterImages(true);
				}});
		}
	}
	
	/**
	 * Add a filter icon on top of the instrument icon.
	 * @param instrumentImage
	 */
	private void addFilterImage(final Node instrumentImage) {
		ImageView filterImage = new ImageView();
		filterImages.add(filterImage);
		Pane instLinePane = (Pane) instrumentLine.getParent();
		instLinePane.getChildren().add(filterImage);
		
		Bounds instrumentImageBounds = instrumentImage.localToScene(instrumentImage.getBoundsInLocal());
		// the bounds are off by 2 for some reason
		filterImage.setTranslateX(instrumentImageBounds.getMinX() - 2);
		filterImage.setTranslateY(instrumentImageBounds.getMinY() - 2);
		filterImage.setOpacity(0.0);
		
		FadeTransition ft = new FadeTransition(Duration.millis(2000), filterImage);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		filterImagesFades.add(ft);
		
		// filterImage consumes events so pass the events off to the instrumentImage
		filterImage.addEventHandler(InputEvent.ANY, new EventHandler() {

			@Override
			public void handle(Event event) {
				instrumentImage.fireEvent(event);
			}
		});
	}
	
	private void fadeFilterImages(boolean fadeThem) {
		for (FadeTransition fade : filterImagesFades)
			if(fadeThem)
				fade.playFromStart();
			else
				fade.pause();
	}
	
	/**
	 * @param ind
	 *            the instrument
	 * @return if instrument is allowed copying, deleting, etc.
	 */
	public boolean isFiltered(InstrumentIndex ind) {
		return this.isEmpty() || this.contains(ind);
	}
	
	/**
	 * turn instrument on/off in filter, display and fade the filter image
	 * 
	 * @param ind
	 *            instrument to filter
	 * @return true if it now contains ind, false if it doesn't
	 */
	public boolean toggleInstrument(InstrumentIndex ind) {
		if(toggleInstrumentNoImage(ind)) {
			fadeFilterImages(true);
			return true;
		} else {
			fadeFilterImages(true);
			return false;
		}
	}
	
	/**
	 * toggleInstrument but don't display image
	 * 
	 * @param ind
	 *            instrument to filter
	 * @return true if it now contains ind, false if it doesn't
	 */
	public boolean toggleInstrumentNoImage(InstrumentIndex ind) {
		int index = instToIndex(ind);
		if(!this.contains(ind)){
			this.add(ind);
			filterImages.get(index).setImage(il.getSpriteFX(ImageIndex.FILTER));
			return true;
		}
		else{
			this.remove(ind);
			filterImages.get(index).setImage(null);
			return false;
		}
	}
	
	/**
	 * switch COIN and PIRANHA
	 * @param ind
	 * @return
	 */
	private int instToIndex(InstrumentIndex ind) {
		switch(ind.imageIndex()) {
		case COIN:
			return (ind.getChannel() - 1) + 1;
		case PIRANHA:
			return (ind.getChannel() - 1) - 1;
		default:
			return (ind.getChannel() - 1);
		}
	}

	/**
	 * switch COIN AND PIRANHA
	 * @param index
	 * @return
	 */
	private InstrumentIndex indexToInst(int index) {
		if(index == InstrumentIndex.COIN.ordinal())
			return InstrumentIndex.PIRANHA;
		if(index == InstrumentIndex.PIRANHA.ordinal())
			return InstrumentIndex.COIN;
		return InstrumentIndex.values()[index];
	}
}
