package smp.components.topPanel;

import java.util.ArrayList;

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
import smp.stateMachine.Settings;
import smp.stateMachine.StateMachine;

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
     * The default note number.
     */
    private static int DEFAULT_NOTE;

    /**
     * An ArrayList of ImageView objects being used as buttons.
     */
    private ArrayList<ImageView> buttons = new ArrayList<ImageView>();

    /**
     * The InstrumentIndex of the selected instrument. Default is Mario.
     */
    private static InstrumentIndex selectedInstrument = InstrumentIndex.MARIO;

    /** This is a list of names for the different instrument line instruments. */
    private ArrayList<String> instrumentLineImages = new ArrayList<String>();

    /** The picture of the currently-selected instrument. */
    private ImageView selectedInst;

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
    public ButtonLine(HBox instLine, ImageView selected, ImageLoader im) {
        il = im;
        for (Node i : instLine.getChildren())
            buttons.add((ImageView) i);
        selectedInst = selected;
        setupButtons();
        DEFAULT_NOTE = Note.A3.getKeyNum();
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
                                try {
                                    selectedInst.setImage(il
                                            .getSpriteFX(ImageIndex.valueOf(i
                                                    .toString())));
                                    setSelectedInstrument(i);
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
     * Sets the selected instrument to extend mode.
     * 
     * @param i
     *            The instrument that we are trying to set to extend.
     */
    private void toggleNoteExtension(InstrumentIndex i) {
        boolean[] ext = StateMachine.getNoteExtensions();
        ext[i.getChannel() - 1] = !ext[i.getChannel() - 1];
        changePortrait(i.getChannel() - 1, ext[i.getChannel() - 1]);
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
     * Sets the currently selected instrument.
     * 
     * @param i
     *            The <code>InstrumentIndex</code> that one wants to set the
     *            currently selected instrument by.
     */
    private void setSelectedInstrument(InstrumentIndex i) {
        selectedInstrument = i;
    }

    /** @return The current selected instrument. */
    public static InstrumentIndex getSelectedInstrument() {
        return selectedInstrument;
    }

    /**
     * @param n
     *            The Note that all of the InstrumentLine instruments will play
     *            when clicked on.
     */
    public void setDefaultNote(Note n) {
        DEFAULT_NOTE = n.getKeyNum();
    }

}
