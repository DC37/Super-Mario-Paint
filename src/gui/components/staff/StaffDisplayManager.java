package gui.components.staff;

import java.util.ArrayList;
import java.util.Arrays;

import backend.editing.ModifySongManager;
import backend.songs.Accidental;
import backend.songs.StaffNote;
import backend.songs.StaffNoteLine;
import backend.songs.StaffSequence;
import gui.Staff;
import gui.Values;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * This class manages the nodes on the staff.  It provides methods for updating
 * the scene graph dynamically.  Typically it will only be called (by
 * {@link Staff}) to update the display whenever a change occurs.
 *
 * @author RehdBlob
 * @since 2012.09.17
 */
public class StaffDisplayManager {
    
    final private HBox staffInstruments;
    final private HBox staffAccidentals;
    final private HBox staffMeasureLines;
    final private HBox staffMeasureNums;
    final private HBox[] staffLedgerLines;
    final private HBox staffVolumeBars;
    
    final private NoteMatrix matrix;
    
    final private ModifySongManager commandManager;

    /**
     * The ArrayList that holds the ImageView objects that the measure lines
     * object holds.
     */
    private ArrayList<ImageView> measureLines;

    /**
     * The ArrayList that holds the Text objects that will hold the measure line
     * numbers.
     */
    private ArrayList<Text> measureNums;

    /** This is the ImageLoader class. */
    private ImageLoader il;

    /** The ledger lines at the high C of the staff. */
    private ArrayList<Node> highC;

    /** The ledger lines at the high A of the staff. */
    private ArrayList<Node> highA;

    /** The ledger lines at the middle C of the staff. */
    private ArrayList<Node> middleC;

    /** The ledger lines at the low C of the staff. */
    private ArrayList<Node> lowC;

    /** The ledger lines at the low A of the staff. */
    private ArrayList<Node> lowA;

    /** This is the list of volume bar handlers on the staff. */
    private ArrayList<StaffVolumeEventHandler> volumeBarHandlers;

    /**
     * Constructor that also sets up the staff ledger lines.
     */
    public StaffDisplayManager(ImageLoader i, HBox staffInstruments, HBox staffAccidentals, HBox staffMeasureLines, HBox staffMeasureNums, HBox[] staffLedgerLines, HBox staffVolumeBars, ModifySongManager commandManager) {
        il = i;
        this.staffInstruments = staffInstruments;
        this.staffAccidentals = staffAccidentals;
        this.staffMeasureLines = staffMeasureLines;
        this.staffMeasureNums = staffMeasureNums;
        this.staffLedgerLines = staffLedgerLines;
        this.staffVolumeBars = staffVolumeBars;
        this.matrix = new NoteMatrix(i);
        this.commandManager = commandManager;
    }
    
    public StaffVolumeEventHandler getVolHandler(int index) {
        return volumeBarHandlers.get(index);
    }

    /**
     * Instantiates this wrapper class with the correct HBox objects such that
     * it can begin keeping track of whatever's happening on the staff, at least
     * on the measure lines side.
     */
    public void initialize() {

        initializeStaffMeasureLines();
        initializeStaffMeasureNums();
        initializeStaffLedgerLines();
        initializeStaffInstruments();
        initializeVolumeBars();
    }

    /**
     * Initializes the volume bars in the program.
     */
    private void initializeVolumeBars() {
        volumeBarHandlers = new ArrayList<StaffVolumeEventHandler>();
        for (Node v : staffVolumeBars.getChildren()) {
            StackPane volBar = (StackPane) v;
            StaffVolumeEventHandler sveh = new StaffVolumeEventHandler(volBar, il, commandManager);
            sveh.setStaffNoteLine(new StaffNoteLine());
            volBar.addEventHandler(Event.ANY, sveh);
            volumeBarHandlers.add(sveh);
        }
    }
    
    public void updateVolumeBars(StaffSequence seq, int currLine) {
        for (int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++) {
            StaffVolumeEventHandler sveh = volumeBarHandlers.get(i);
            StaffNoteLine stl = seq.getLineSafe(currLine + i);
            
            sveh.setStaffNoteLine(stl);
            sveh.updateVolume();
        }
    }

    /**
     * Sets up the various note lines of the staff. These are the notes that can
     * appear on the staff. This method also sets up sharps, flats, etc.
     */
    private void initializeStaffInstruments() {
        matrix.initializeNoteDisplay(staffInstruments, staffAccidentals);
    }
    
    public void updateNoteDisplay(StaffSequence seq, int currLine) {
        matrix.clearNoteDisplay();
        matrix.populateNoteDisplay(seq, currLine);
        matrix.refreshSilhouette();
    }

    /**
     * These are the lines that divide up the staff.
     */
    private void initializeStaffMeasureLines() {
        measureLines = new ArrayList<ImageView>();
        
        for (int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++) {
            ImageView iv = new ImageView();
            iv.setFitHeight(448.0);
            iv.setFitWidth(2.0);
            iv.setPickOnBounds(true);
            iv.setPreserveRatio(true);
            
            staffMeasureLines.getChildren().add(iv);
            measureLines.add(iv);
        }
    }
    
    private void initializeStaffMeasureNums() {
        measureNums = new ArrayList<Text>();
        
        for (int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++) {
            HBox box = new HBox();
            box.setAlignment(Pos.CENTER);
            box.setPrefHeight(9.0);
            box.setPrefWidth(42.0);
            
            staffMeasureNums.getChildren().add(box);
            Text t = new Text();
            box.getChildren().add(t);
            measureNums.add(t);
        }
    }

    /**
     * Redraws the staff measure lines and numbers.
     *
     * @param currLine
     *            The current line that we are on.
     */
    public void updateStaffMeasureLines(int currLine, int[] barDivs) {
        int barLength = Arrays.stream(barDivs).sum();

        int[] cumulativeSubLengths = new int[barDivs.length];
        cumulativeSubLengths[0] = 0;
        for (int i = 1; i < barDivs.length; i++)
            cumulativeSubLengths[i] = cumulativeSubLengths[i - 1] + barDivs[i - 1];

        for (int i = 0; i < measureLines.size(); i++) {
            ImageView currImage = measureLines.get(i);
            Text currText = measureNums.get(i);

            int currentIndex = currLine + i;
            int currentBarIndex = currentIndex / barLength;
            int relativeIndex = currentIndex % barLength;
            
            int k = Arrays.binarySearch(cumulativeSubLengths, relativeIndex);
            
            if (k == 0) {
                currImage.setImage(il.getSpriteFX(ImageIndex.STAFF_MLINE));
                String txt = String.valueOf(currentBarIndex + 1);
                double fontSize = Font.getDefault().getSize();
                currText.setText(txt);
                currText.setFont(new Font(fontSize));

                
            } else if (k > 0) {
                currImage.setImage(il.getSpriteFX(ImageIndex.STAFF_SLINE));
                String txt = String.valueOf(currentBarIndex + 1) + "." + String.valueOf(k);
                double fontSize = Font.getDefault().getSize() * .75;
                currText.setText(txt);
                currText.setFont(new Font(fontSize));
                
            } else {
                currImage.setImage(il.getSpriteFX(ImageIndex.STAFF_LINE));
                currText.setText("");
            }
        }
    }

    /**
     * Sets up the staff expansion lines, which are to hold notes that are
     * higher than or lower than the regular lines of the staff.
     */
    private void initializeStaffLedgerLines() {
        highC = new ArrayList<Node>();
        highA = new ArrayList<Node>();
        middleC = new ArrayList<Node>();
        lowC = new ArrayList<Node>();
        lowA = new ArrayList<Node>();
        highC.addAll(staffLedgerLines[0].getChildren());
        highA.addAll(staffLedgerLines[1].getChildren());
        middleC.addAll(staffLedgerLines[2].getChildren());
        lowC.addAll(staffLedgerLines[3].getChildren());
        lowA.addAll(staffLedgerLines[4].getChildren());
    }
    
    public void updateStaffLedgerLines(StaffSequence seq, int currLine) {
        for (int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++) {
            StaffNoteLine stl = seq.getLineSafe(currLine + i);

            int high = 0;
            int low = Values.NOTES_IN_A_LINE;
            boolean middleCPresent = false;
            for (StaffNote n : stl.getNotes()) {
                int nt = n.getPosition();
                if (nt >= high)
                    high = nt;
                if (nt <= low)
                    low = nt;
                if (nt == Values.middleC) {
                    middleCPresent = true;
                }
            }
            
            if (high >= Values.highC) {
                highC.get(i).setVisible(true);
                highA.get(i).setVisible(true);
            } else if (high >= Values.highA) {
                highC.get(i).setVisible(false);
                highA.get(i).setVisible(true);
            } else {
                highC.get(i).setVisible(false);
                highA.get(i).setVisible(false);
            }

            if (low <= Values.lowA) {
                lowC.get(i).setVisible(true);
                lowA.get(i).setVisible(true);
            } else if (low <= Values.lowC) {
                lowC.get(i).setVisible(true);
                lowA.get(i).setVisible(false);
            } else {
                lowC.get(i).setVisible(false);
                lowA.get(i).setVisible(false);
            }

            if (middleCPresent) {
                middleC.get(i).setVisible(true);
            } else{
                middleC.get(i).setVisible(false);
            }
        }
    }
    
    public void resetSilhouette() {
        matrix.resetSilhouette();
    }
    
    public void updateSilhouette(int line, StaffNote sil) {
        matrix.updateSilhouette(line, sil);
    }
    
    public void refreshSilhouette() {
        matrix.refreshSilhouette();
    }
    
    public void refreshSilhouette(Accidental acc) {
        matrix.refreshSilhouette(acc);
    }

}
