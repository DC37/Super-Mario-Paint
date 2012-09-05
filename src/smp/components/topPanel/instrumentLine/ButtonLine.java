package smp.components.topPanel.instrumentLine;

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
import smp.components.staff.sequences.Note;
import smp.stateMachine.Settings;

/**
 * The line of buttons that appear for the Instrument
 * Line at the top of the user interface for Super Mario
 * Paint. The user selects the instrument that they're using here.
 * Clicking on a portrait causes a sound to play while the active
 * instrument is changed.
 * @author RehdBlob
 * @since 2012.08.21
 */
public class ButtonLine {


    /**
     * The largest value that a note velocity can be;
     * a note played at this will be played as loudly
     * as possible.
     */
    private static final int MAX_VELOCITY = 127;

    /**
     * The smallest value that a note velocity can be;
     * a note will basically be silent if played at this.
     */
    private static final int MIN_VELOCITY = 0;

    private static int DEFAULT_NOTE;

    /**
     * An ArrayList of ImageView objects being used as buttons.
     */
    private ArrayList<ImageView> buttons = new ArrayList<ImageView>();

    /**
     * The InstrumentIndex of the selected instrument.
     */
    private InstrumentIndex selectedInstrument;

    /**
     * The picture of the currently-selected instrument.
     */
    private ImageView selectedInst;

    /**
     * The MidiChannel array objects that will be holding references for
     * sound-playing capabilities.
     */
    private MidiChannel [] chan;

    /**
     * Initializes the ImageView ArrayList.
     * @param i An ArrayList of ImageView references intended to be displayed
     * as an instrument line.
     */
    public ButtonLine(HBox instLine, ImageView selected) {
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
        for (final InstrumentIndex i : InstrumentIndex.values()) {
            buttons.get(i.ordinal()).setOnMouseClicked(
                    new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            playSound(i);
                            try {
                                selectedInst.setImage(
                                        ImageLoader.getSpriteFX(
                                                ImageIndex.valueOf(i.toString())));
                                setSelectedInstrument(i);
                            } catch (IllegalArgumentException e) {
                                // Do nothing.
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                // Do nothing.
                                e.printStackTrace();
                            }
                        }
                    });
        }
        try {
            chan = SoundfontLoader.getChannels();
        } catch (NullPointerException e) {
            // Can't recover
            System.exit(0);
        }
    }

    /**
     * Plays the default "A" sound when selecting an instrument.
     * @param i The InstrumentIndex for the instrument
     */
    public void playSound(InstrumentIndex i) {
        if (chan[i.ordinal()] != null) {
            chan[i.ordinal()].noteOn(DEFAULT_NOTE, MAX_VELOCITY);
            if (Settings.DEBUG)
                System.out.println(
                        "Channel " + (i.ordinal() + 1)
                        + " Instrument: " + i.name());
        }
    }

    private void setSelectedInstrument(InstrumentIndex i) {
        selectedInstrument = i;
    }

    public InstrumentIndex getSelectedInstrument() {
        return selectedInstrument;
    }

    /**
     * @param n The Note that all of the InstrumentLine instruments
     * will play when clicked on.
     */
    public void setDefaultNote(Note n) {
        DEFAULT_NOTE = n.getKeyNum();
    }

}
