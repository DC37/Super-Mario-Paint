package smp.presenters.staff.VolumeBarsPresenter;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.TestMain;
import smp.models.staff.StaffNoteLine;
import smp.models.stateMachine.Variables;
import smp.models.stateMachine.Variables.WindowLines;
import smp.presenters.api.reattachers.NoteLineReattacher;
import smp.presenters.staff.VolumeBarsPresenter.StaffVolumeEventHandler;

public class VolumeBarsPresenter {

	// TODO: auto-add these model comments
	// ====Models====
	private WindowLines windowLines;

	private ArrayList<NoteLineReattacher> noteLineReattachers;

	private HBox volumeBars;
	
    /** The ImageView objects for each volume bar. */
    //j574y923..model-view
    private ArrayList<ImageView> theVolBarIVs = new ArrayList<>();
    
    /** The ImageLoader class. */
    //TODO:
    //j574y923..model-view
    private ImageLoader il = (ImageLoader) TestMain.imgLoader;
	
	/**
	 * This is the list of volume bar handlers on the staff.
	 * 
	 * @see smp.components.staff.NoteMatrix.volumeBarHandlers
	 */
	private ArrayList<StaffVolumeEventHandler> volumeBarHandlers;

	/**
	 * This is the list of volume bars on the staff.
	 * 
	 * @see smp.components.staff.NoteMatrix.volumeBars
	 */
	private ArrayList<StackPane> volumeBarsSP;

	public VolumeBarsPresenter(HBox volumeBars) {
		this.volumeBars = volumeBars;
        volumeBarHandlers = new ArrayList<StaffVolumeEventHandler>();

		this.windowLines = Variables.windowLines;
		this.noteLineReattachers = new ArrayList<NoteLineReattacher>();
		initializeVolumeBars(this.volumeBars);
		initializeVolumeBarIVs(this.volumeBars);
		setupViewUpdater();
	}

	private void initializeVolumeBarIVs(HBox volumeBars) {
		for (Node v : volumeBars.getChildren()) {
			StackPane st = (StackPane) v;
	        ImageView theVolBar = (ImageView) st.getChildren().get(0);
	        theVolBar.setImage(il.getSpriteFX(ImageIndex.VOL_BAR));
	        theVolBar.setVisible(false);
			theVolBarIVs.add(theVolBar);
		}
	}

	/**
	 * Initializes the volume bars in the program.
	 *
	 * @param volumeBars
	 *            This is the HBox that holds all of the volume bar
	 *            <code>StackPane</code> objects.
	 */
	private void initializeVolumeBars(HBox volumeBars) {
		ArrayList<StackPane> vol = new ArrayList<StackPane>();
		ObservableList<Node> volumeBarsChildren = volumeBars.getChildren();
		for (int i = 0; i < volumeBarsChildren.size(); i++) {
			Node v = volumeBarsChildren.get(i);
			StackPane volBar = (StackPane) v;
			vol.add(volBar);
			StaffVolumeEventHandler sveh = new StaffVolumeEventHandler(volBar, windowLines.get(i));
			volBar.addEventHandler(Event.ANY, sveh);
			volumeBarHandlers.add(sveh);
		}
		volumeBarsSP = vol;
	}

	private void setupViewUpdater() {
		for (int i = 0; i < windowLines.size(); i++) {
			final int index = i;
			NoteLineReattacher nlr = new NoteLineReattacher(windowLines.get(i));			
			nlr.setNewNotesSizeListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldSize, Number newSize) {
					// either old size or new size is empty
					// we are observing whether its empty
					// we only want to update visibility depending on whether its empty
					if((oldSize.intValue() & newSize.intValue()) == 0)
						updateVolume(index);
				}
			});
			nlr.setNewVolumeListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					updateVolume(index);
				}
			});
			nlr.setOnReattachListener(new ChangeListener<Object>() {

				@Override
				public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
					updateVolume(index);
				}
			});
			noteLineReattachers.add(nlr);
		}
	}
	
    /**
     * Displays the volume of this note line.
     * @param y The volume that we want to show.
     */
    public void setVolumeDisplay(int line, double y) {
    	ImageView theVolBar = theVolBarIVs.get(line);
        if (y <= 0) {
            theVolBar.setVisible(false);
            return;
        }
        theVolBar.setVisible(true);
        theVolBar.setFitHeight(y);
    }

	/**
	 * Updates the volume display on this volume displayer.
	 */
	public void updateVolume(int line) {
		StaffNoteLine theLine = this.windowLines.get(line).get();
		StackPane stp = volumeBarsSP.get(line);
    	ImageView theVolBarIV = theVolBarIVs.get(line);
		setVolumeDisplay(line, theLine.getVolumePercent() * stp.getHeight());
		if (theLine.getVolume().get() == 0 || theLine.isEmpty()) {
			theVolBarIV.setVisible(false);
		}
    }
}
