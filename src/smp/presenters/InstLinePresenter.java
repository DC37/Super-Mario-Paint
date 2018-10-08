package smp.presenters;

import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import javax.sound.midi.MidiChannel;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.SoundfontLoader;
import smp.components.Values;
import smp.components.InstrumentIndex;
import smp.components.staff.sequences.Note;
import smp.models.staff.StaffSequence;
import smp.models.stateMachine.Settings;
import smp.models.stateMachine.StateMachine;
import smp.models.stateMachine.Variables;

/**
 * The line of buttons that appear for the Instrument Line at the top of the
 * user interface for Super Mario Paint. The user selects the instrument that
 * they're using here. Clicking on a portrait causes a sound to play while the
 * active instrument is changed.
 *
 * @author RehdBlob
 * @since 2012.08.21
 */
public class InstLinePresenter {

	//TODO: auto-add these model comments
	//====Models====
	private BooleanProperty[] ext;
	private ObjectProperty<StaffSequence> theSequence;
	
    /**
     * The default note number.
     */
    private static int DEFAULT_NOTE;

    /**
     * An ArrayList of ImageView objects being used as buttons.
     */
    private ArrayList<ImageView> buttons = new ArrayList<ImageView>();

    /** This is a list of names for the different instrument line instruments. */
    private ArrayList<String> instrumentLineImages = new ArrayList<String>();

    /** This is the image loader class. */
    private ImageLoader il;

    /**
     * The MidiChannel array objects that will be holding references for
     * sound-playing capabilities.
     */
    private MidiChannel[] chan;

    /**
     * Initializes the ImageView ArrayList.
     *
     * @param i
     *            An ArrayList of ImageView references intended to be displayed
     *            as an instrument line.
     */
    public InstLinePresenter(HBox instLine) {
        for (Node i : instLine.getChildren())
            buttons.add((ImageView) i);
        setupButtons();
        DEFAULT_NOTE = Note.A3.getKeyNum();
        /*
         * For some reason, the piranha and coin are flipped in all soundfonts.
         * The solution here is unfortunately to just flip the images.
         */
        ObservableList<Node> n = instLine.getChildren();
        Node nd = n.remove(15);
        n.add(16, nd);
        
        this.ext = StateMachine.getNoteExtensions();
        this.theSequence = Variables.theSequence;
        setupViewUpdater();
    }

    /**
     * Initializes the buttons with event handlers.
     */
    public void setupButtons() {
        int ind = 0;
        for (final InstrumentIndex i : InstrumentIndex.values()) {
            if (ind > Values.NUMINSTRUMENTS - 1) {
                break;
            }
            instrumentLineImages.add(i.toString());
            buttons.get(i.getChannel() - 1).setOnMousePressed(
                    new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            if (event.isShiftDown()) {
                                toggleNoteExtension(i);
                                event.consume();
                            } else {
                                playSound(i);
                                event.consume();
                            }
                        }

                    });
            ind++;
        }
        try {
            chan = SoundfontLoader.getChannels();
        } catch (NullPointerException e) {
            System.err.println("Unable to load soundfont channels.");
            System.exit(1);
        }
    }

    /**
     * Plays the default "A" sound when selecting an instrument.
     *
     * @param i
     *            The InstrumentIndex for the instrument
     */
    public void playSound(InstrumentIndex i) {
        int ind = i.getChannel() - 1;
        if (chan[ind] != null) {
            chan[ind].noteOn(DEFAULT_NOTE, Values.MAX_VELOCITY);
            if (Settings.debug > 0)
                System.out.println("Channel " + (ind + 1) + " Instrument: "
                        + i.name());
        }
    }

    /**
     * Updates the note extensions display at the instrument selection line.
     */
    @Deprecated
    public void updateNoteExtensions() {
        BooleanProperty[] ext = StateMachine.getNoteExtensions();
        for (int j = 0; j < Values.NUMINSTRUMENTS; j++) {
                changePortrait(j, ext[j].get());
        }
    }

    /**
     * Sets the selected instrument to extend mode.
     *
     * @param i
     *            The instrument that we are trying to set to extend.
     */
    private void toggleNoteExtension(InstrumentIndex i) {
        this.ext[i.getChannel() - 1].set(!ext[i.getChannel() - 1].get());
        this.theSequence.get().setNoteExtensions(ext);
    }

    /**
     * Toggles the portrait display of the instrument index.
     *
     * @param i
     *            The index at which we want to modify.
     * @param b
     *            If we want note extensions, the box will be of a different
     *            color.
     */
    private void changePortrait(int i, boolean b) {
        if (!b) {
            buttons.get(i).setImage(
                    il.getSpriteFX(ImageIndex.valueOf(instrumentLineImages
                            .get(i) + "_SM")));
        } else {
            buttons.get(i).setImage(
                    il.getSpriteFX(ImageIndex.valueOf(
                            instrumentLineImages.get(i) + "_SM").alt()));
        }
    }

    /**
     * @param n
     *            The Note that all of the InstrumentLine instruments will play
     *            when clicked on.
     */
    public void setDefaultNote(Note n) {
        DEFAULT_NOTE = n.getKeyNum();
    }
    
    private void setupViewUpdater() {
		for (int i = 0; i < this.ext.length; i++) {
			final int i2 = i;
			this.ext[i].addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					changePortrait(i2, newValue.booleanValue());
				}
			});
		}
    }
}