package smp;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

import smp.components.Constants;
import smp.components.InstrumentIndex;
import smp.components.staff.sequences.Note;
import smp.components.staff.sounds.SMPSynthesizer;
import smp.stateMachine.Settings;

/**
 * Loads the soundfonts that will be used to play sounds.
 * Also holds a Synthesizer and Soundbank that will be used
 * to play more sounds.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class SoundfontLoader implements Loader {

    /**
     * A number between 0 and 1 that indicates the
     * completion of the loading Thread's tasks.
     */
    private double loadStatus = 0.0;

    /**
     * The sound synthesizer used to hold as many instruments as needed.
     */
    private static SMPSynthesizer theSynthesizer;

    /**
     * The MIDI channels associated with the MultiSynthsizer.
     */
    private static MidiChannel [] chan;

    /**
     * The soundbank that will hold the sounds that we're trying to play.
     */
    private static Soundbank bank;

    /**
     * The Settings that define what this class does.
     */
    private static Settings settings;

    /**
     * The default soundset name.
     */
    private String soundset = "soundset3.sf2";

    /**
     * Initializes a MultiSynthesizer with the soundfont.
     */
    @Override
    public void run() {
        try {
            File f = new File("./" + soundset);
            bank = MidiSystem.getSoundbank(f);
            theSynthesizer = new SMPSynthesizer();
            theSynthesizer.open();
            setLoadStatus(0.1);
            /* if (advanced mode on)
             *     theSynthesizer.ensureCapacity(50);
             * else
			 bv*/

            theSynthesizer.ensureCapacity(19);
            for (Instrument i : theSynthesizer.getLoadedInstruments()) {
                theSynthesizer.unloadInstrument(i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            setLoadStatus(0.2);
            theSynthesizer.loadAllInstruments(bank);
            setLoadStatus(0.3);

            if (settings.debug){
                System.out.println("Loaded Instruments: ");
                for (Instrument j : theSynthesizer.getLoadedInstruments())
                    System.out.println(j.getName());
            }

            int ordinal = 0;
            chan = theSynthesizer.getChannels();
            for (InstrumentIndex i : InstrumentIndex.values()) {
                setLoadStatus(0.3 + 0.7
                        * ordinal / InstrumentIndex.values().length);
                chan[ordinal].programChange(ordinal);
                ordinal++;
                System.out.println("Initialized Instrument: "
                        + i.toString());
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Synth Latency: " + theSynthesizer.getLatency());
            setLoadStatus(1);
        } catch (MidiUnavailableException e) {
            // Can't recover.
            e.printStackTrace();
            System.exit(0);
        } catch (InvalidMidiDataException e) {
            // Can't recover.
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            // Can't recover.
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * @return The MultiSynthesizer that holds a list of Synthesizers.
     */
    public static Synthesizer getSynth() {
        return theSynthesizer;
    }

    /**
     * @return An Array of references for MidiChannel objects needed to
     * play sounds.
     */
    public static MidiChannel[] getChannels() {
        return chan;
    }

    /**
     * Closes the synthesizers.
     */
    public void close() {
        theSynthesizer.close();
    }

    /**
     * @return A double value between 0 and 1, representing the
     * load status of this class.
     */
    @Override
    public double getLoadStatus() {
        return loadStatus;
    }

    /**
     * Set the load status of the SoundfontLoader.
     * @param d A double value between 0 and 1 that represents the
     * load state of this class.
     */
    @Override
    public void setLoadStatus(double d) {
        if (d >= 0 && d <= 1)
            loadStatus = d;
    }

    /**
     * Plays a certain sound given a Note and some instrument.
     * @param n The Note to play
     * @param i The Instrument to play it with.
     * @param acc The offset from this note, in half-steps.
     */
    public static void playSound(Note n, InstrumentIndex i, int acc) {
        chan[i.getChannel() - 1].noteOn(n.getKeyNum() + acc,
                Constants.MAX_VELOCITY);
    }

    public static void setSettings(Settings s) {
        settings = s;
    }

}
