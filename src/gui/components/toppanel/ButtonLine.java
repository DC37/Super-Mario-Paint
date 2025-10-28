package gui.components.toppanel;

import java.util.ArrayList;

import javax.sound.midi.MidiChannel;

import gui.InstrumentIndex;
import gui.Settings;
import gui.Staff;
import gui.StateMachine;
import gui.Values;
import gui.loaders.ImageLoader;
import gui.loaders.SoundfontLoader;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * The line of buttons that appear for the Instrument Line at the top of the
 * user interface for Super Mario Paint. The user selects the instrument that
 * they're using here. Clicking on a portrait causes a sound to play while the
 * active instrument is changed.
 *
 * @author RehdBlob
 * @since 2012.08.21
 */
public class ButtonLine {

    /**
     * An ArrayList of ImageView objects being used as buttons.
     */
    private ArrayList<ImageView> buttons = new ArrayList<ImageView>();

    /** This is the image loader class. */
    private ImageLoader il;

    /**
     * The MidiChannel array objects that will be holding references for
     * sound-playing capabilities.
     */
    private MidiChannel[] chan;

    /** This is the staff that we are editing. */
    private Staff theStaff;

    /**
     * Initializes the ImageView ArrayList.
     *
     * @param i
     *            An ArrayList of ImageView references intended to be displayed
     *            as an instrument line.
     */
    public ButtonLine(HBox instLine, ImageLoader im,
            Staff st) {
        il = im;
        for (Node i : instLine.getChildren())
            buttons.add((ImageView) i);
        theStaff = st;
        setupButtons();
        /*
         * For some reason, the piranha and coin are flipped in all soundfonts.
         * The solution here is unfortunately to just flip the images.
         */
        ObservableList<Node> n = instLine.getChildren();
        Node nd = n.remove(15);
        n.add(16, nd);
    }

    /**
     * Initializes the buttons with event handlers.
     */
    public void setupButtons() {
        for (final InstrumentIndex inst : InstrumentIndex.values()) {
            buttons.get(inst.getChannel() - 1).setOnMousePressed(
                    new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            if (event.isShiftDown()) {
                                toggleNoteExtension(inst);
                                event.consume();
                            } else {
                                playSound(inst);
                                try {
                                	StateMachine.setSelectedInstrument(inst);
                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                } finally {
                                    event.consume();
                                }
                            }
                        }

                    });
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
            chan[ind].noteOn(Values.DEFAULT_NOTE, Values.DEFAULT_VELOCITY);
            if ((Settings.debug & 0b10) != 0)
                System.out.println("Channel " + (ind + 1) + " Instrument: "
                        + i.name());
        }
    }

    /**
     * Updates the note extensions display at the instrument selection line.
     */
    public void updateNoteExtensions() {
        boolean[] ext = StateMachine.getNoteExtensions();
        for (InstrumentIndex inst : InstrumentIndex.values()) {
        	changePortrait(inst, ext[inst.getChannel() - 1]);
        }
    }

    /**
     * Sets the selected instrument to extend mode.
     *
     * @param i
     *            The instrument that we are trying to set to extend.
     */
    private void toggleNoteExtension(InstrumentIndex i) {
        boolean[] ext = StateMachine.getNoteExtensions();
        ext[i.getChannel() - 1] = !ext[i.getChannel() - 1];
        StateMachine.setNoteExtensions(ext);
        changePortrait(i, ext[i.getChannel() - 1]);
        boolean[] ext2 = theStaff.getSequence().getNoteExtensions();
        ext2 = ext;
        theStaff.getSequence().setNoteExtensions(ext2);
    }

    private void changePortrait(InstrumentIndex inst, boolean b) {
    	int i = inst.getChannel() - 1;
        if (!b) {
        	buttons.get(i).setImage(il.getSpriteFX(inst.smImageIndex()));
        } else {
        	buttons.get(i).setImage(il.getSpriteFX(inst.smaImageIndex()));
        }
    }

}
