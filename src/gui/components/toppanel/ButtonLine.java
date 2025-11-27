package gui.components.toppanel;

import java.util.ArrayList;

import javax.sound.midi.MidiChannel;

import gui.InstrumentIndex;
import gui.Staff;
import gui.StateMachine;
import gui.Values;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;
import gui.loaders.SoundfontLoader;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
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

    /**
     * Initializes the ImageView ArrayList.
     *
     * @param i
     *            An ArrayList of ImageView references intended to be displayed
     *            as an instrument line.
     */
    public ButtonLine(HBox instLine, Staff st) {
        for (Node i : instLine.getChildren())
            buttons.add((ImageView) i);
        
        // /!\ Assuming the order of instruments in fxml matches the order in InstrumentIndex
        for (final InstrumentIndex inst : InstrumentIndex.values()) {
            buttons.get(inst.ordinal()).setOnMousePressed(event -> {
                if (event.isShiftDown()) {
                    toggleNoteExtension(inst);
                    st.getSequence().setNoteExtensions(StateMachine.getNoteExtensions());
                    
                } else {
                    playSound(inst);
                    StateMachine.setSelectedInstrument(inst);
                }
            });
        }
    }

    private void playSound(InstrumentIndex i) {
        int ind = i.getChannel() - 1;
        MidiChannel[] chan = SoundfontLoader.getChannels();
        if (chan[ind] != null) {
            chan[ind].noteOn(Values.DEFAULT_NOTE, Values.DEFAULT_VELOCITY);
        }
    }

    private void toggleNoteExtension(InstrumentIndex i) {
        boolean b = StateMachine.getNoteExtension(i.ordinal());
        StateMachine.setNoteExtension(i.ordinal(), !b);
    }

    public void updatePortraitSustain(InstrumentIndex inst, ImageLoader il) {
    	int i = inst.ordinal();
    	ImageIndex image = (StateMachine.getNoteExtension(i)) ? inst.smaImageIndex() : inst.smImageIndex();
    	buttons.get(i).setImage(il.getSpriteFX(image));
    }

}
