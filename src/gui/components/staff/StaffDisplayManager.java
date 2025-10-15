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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
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
    final private HBox staffPlayBars;
    final private Pane staffFrame;
    
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
    
    private Node[] playbars = new Node[Values.NOTELINES_IN_THE_WINDOW];
    private int activePlaybar = -1;

    /**
     * Constructor that also sets up the staff ledger lines.
     */
    public StaffDisplayManager(Pane staffFrame, ImageLoader il, HBox staffVolumeBars, ModifySongManager commandManager) {
        this.staffInstruments = new HBox();
        this.staffAccidentals = new HBox(32);
        this.staffMeasureLines = new HBox(62);
        this.staffMeasureNums = new HBox(22);
        this.staffLedgerLines = new HBox[5];
        this.staffVolumeBars = staffVolumeBars;
        this.staffPlayBars = new HBox(8);
        this.staffFrame = staffFrame;

        this.il = il;
        this.matrix = new NoteMatrix(il, Values.NOTELINES_IN_THE_WINDOW, Values.NOTES_IN_A_LINE, Values.MAX_STACKABLE_NOTES);
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
        ImageView instBackground = new ImageView(il.getSpriteFX(ImageIndex.INST_BACKGROUND));
        instBackground.setFitHeight(720);
        instBackground.setFitWidth(1024);
        instBackground.setLayoutY(-52);
        instBackground.setTranslateX(-0.5);
        instBackground.setTranslateY(1);
        instBackground.setPickOnBounds(true);
        
        StackPane staffPane = new StackPane();
        staffPane.setPrefHeight(479);
        staffPane.setPrefWidth(1022);
        Paint staffBgFill = Paint.valueOf("rgb(93.3%, 93.3%, 93.3%)");
        Rectangle staffRect = new Rectangle(1016, 472, staffBgFill);
        staffRect.setArcHeight(5);
        staffRect.setArcWidth(5);
        staffRect.setBlendMode(BlendMode.SRC_OVER);
        staffRect.setStroke(Paint.valueOf("BLACK"));
        staffRect.setStrokeType(StrokeType.INSIDE);
        staffRect.setStrokeWidth(2);
        ImageView staffImageView = new ImageView(il.getSpriteFX(ImageIndex.STAFF_BG_TREBLEBASS));
        staffImageView.setFitHeight(452);
        staffImageView.setFitWidth(983);
        staffImageView.setMouseTransparent(true);
        staffImageView.setPickOnBounds(true);
        staffImageView.setPreserveRatio(true);
        StackPane.setMargin(staffImageView, new Insets(14, 0, 0, 0));
        staffPane.getChildren().addAll(staffRect, staffImageView);
        
        initializeStaffMeasureLines();
        initializeStaffMeasureNums();
        initializeStaffLedgerLines();
        initializeStaffInstruments();
        initializeVolumeBars();
        initializeStaffPlayBars();
        
        staffFrame.getChildren().addAll(instBackground, staffPane, staffMeasureNums, staffMeasureLines, staffLedgerLines[0], staffLedgerLines[1], staffLedgerLines[2], staffLedgerLines[3], staffLedgerLines[4], staffPlayBars, staffAccidentals, staffInstruments);
        staffFrame.setPadding(new Insets(2));
    }

    /**
     * Initializes the volume bars in the program.
     */
    private void initializeVolumeBars() {
        volumeBarHandlers = new ArrayList<StaffVolumeEventHandler>();
        
        for (int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++) {
            StackPane stack = new StackPane();
            stack.setAlignment(Pos.BOTTOM_CENTER);
            stack.setPrefHeight(64.0);
            stack.setPrefWidth(64.0);
            
            ImageView iv = new ImageView();
            iv.setFitHeight(64.0);
            iv.setFitWidth(4.0);
            iv.setPickOnBounds(true);
            
            stack.getChildren().add(iv);
            
            StaffVolumeEventHandler handler = new StaffVolumeEventHandler(stack, il, commandManager);
            handler.setStaffNoteLine(new StaffNoteLine());
            stack.addEventHandler(Event.ANY, handler);
            
            staffVolumeBars.getChildren().add(stack);
            volumeBarHandlers.add(handler);
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
        staffInstruments.setPrefHeight(504);
        staffInstruments.setPrefWidth(982);
        staffInstruments.setAlignment(Pos.CENTER_LEFT);
        staffInstruments.setLayoutX(70);
        staffInstruments.setLayoutY(-5);
        staffInstruments.setPadding(new Insets(0, 0, 0, 113));
        
        staffAccidentals.setPrefHeight(504);
        staffAccidentals.setPrefWidth(982);
        staffAccidentals.setAlignment(Pos.CENTER_LEFT);
        staffAccidentals.setLayoutX(70);
        staffAccidentals.setLayoutY(-5);
        staffAccidentals.setPadding(new Insets(0, 0, 0, 105));
        
        matrix.initializeNoteDisplay(staffInstruments, staffAccidentals);
    }
    
    public void updateNoteDisplay(StaffSequence seq, int currLine) {
        matrix.clearNoteDisplay();
        matrix.populateNoteDisplay(seq, currLine);
    }

    /**
     * These are the lines that divide up the staff.
     */
    private void initializeStaffMeasureLines() {
        staffMeasureLines.setPrefHeight(276);
        staffMeasureLines.setPrefWidth(982);
        staffMeasureLines.setAlignment(Pos.CENTER_LEFT);
        staffMeasureLines.setLayoutX(54);
        staffMeasureLines.setLayoutY(24);
        staffMeasureLines.setPadding(new Insets(0, 0, 0, 144));
        
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
        staffMeasureNums.setPrefHeight(12);
        staffMeasureNums.setPrefWidth(982);
        staffMeasureNums.setAlignment(Pos.CENTER_LEFT);
        staffMeasureNums.setLayoutX(54);
        staffMeasureNums.setLayoutY(5);
        staffMeasureNums.setPadding(new Insets(0, 0, 0, 124));
        
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

        int[] layoutY = {-2, 31, 223, 415, 447};
        ArrayList<?>[] all = { highC, highA, middleC, lowC, lowA };
        
        for (int k = 0; k < 5; k++) {
            HBox staffLedgerLine = new HBox(22);
            staffLedgerLine.setPrefHeight(48);
            staffLedgerLine.setPrefWidth(982);
            staffLedgerLine.setAlignment(Pos.CENTER_LEFT);
            staffLedgerLine.setLayoutX(54);
            staffLedgerLine.setLayoutY(layoutY[k]);
            staffLedgerLine.setPadding(new Insets(0, 0, 0, 124));
            
            staffLedgerLines[k] = staffLedgerLine;

            @SuppressWarnings("unchecked")
            ArrayList<Node> arr = (ArrayList<Node>) all[k];
            
            for (int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++) {
                Rectangle ledger = new Rectangle(42.0, 4.0);
                ledger.setStroke(Paint.valueOf("BLACK"));
                ledger.setStrokeType(StrokeType.INSIDE);
                ledger.setStrokeWidth(0.0);
                ledger.setSmooth(false);
                ledger.setVisible(false);
                
                staffLedgerLines[k].getChildren().add(ledger);
                arr.add(ledger);
            }
        }
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
    
    public void refreshSilhouette(Accidental acc) {
        matrix.refreshSilhouette(acc);
    }
    
    public void initializeStaffPlayBars() {
        staffPlayBars.setPrefHeight(448);
        staffPlayBars.setPrefWidth(982);
        staffPlayBars.setAlignment(Pos.CENTER_LEFT);
        staffPlayBars.setLayoutX(54);
        staffPlayBars.setLayoutY(23);
        staffPlayBars.setPadding(new Insets(0, 0, 0, 117));
        
        for (int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++) {
            ImageView iv = new ImageView(il.getSpriteFX(ImageIndex.PLAY_BAR1));
            iv.setFitHeight(448);
            iv.setFitWidth(56);
            iv.setVisible(false);
            iv.setPickOnBounds(true);
            iv.setPreserveRatio(true);
            iv.setSmooth(false);
            
            staffPlayBars.getChildren().add(iv);
            playbars[i] = iv;
        }
    }
    
    private void doResetPlayBars() {
        if (activePlaybar >= 0 && activePlaybar < Values.NOTELINES_IN_THE_WINDOW)
            playbars[activePlaybar].setVisible(false);
    }
    
    public void resetPlayBars() {
        doResetPlayBars();
        activePlaybar = -1;
    }
    
    public void updatePlayBars(int position) {
        doResetPlayBars();
        
        if (position < 0 || position >= Values.NOTELINES_IN_THE_WINDOW)
            return;
        
        playbars[position].setVisible(true);
        activePlaybar = position;
    }

}
