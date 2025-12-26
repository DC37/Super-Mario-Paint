package gui.components.staff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
import javafx.scene.layout.VBox;
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
    
    final private HBox staffVolumeBars;
    final private Pane staffFrame;
    
    final private NoteMatrix matrix;
    
    final private ModifySongManager commandManager;
    
    final protected int width;
    final protected int height;
    final protected int depth;
    
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
    
    private Node[] playbars;
    private int activePlaybar;

    /**
     * Constructor that also sets up the staff ledger lines.
     */
    public StaffDisplayManager(Pane staffFrame, ImageLoader il, HBox staffVolumeBars, ModifySongManager commandManager, int width, int height, int depth) {
        this.staffVolumeBars = staffVolumeBars;
        this.staffFrame = staffFrame;

        this.il = il;
        this.width = width;
        this.height = height;
        this.depth = depth;
        
        this.matrix = new NoteMatrix(il, this);
        this.commandManager = commandManager;
        
        this.playbars = new Node[width];
        this.activePlaybar = -1;
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
        
        Node staffMeasureLines = initializeStaffMeasureLines();
        Node staffMeasureNums = initializeStaffMeasureNums();
        Node[] staffLedgerLines = initializeStaffLedgerLines();
        Node[] staffInstrumentsAccidentals = initializeStaffInstruments();
        initializeVolumeBars();
        Node staffPlayBars = initializeStaffPlayBars();
        
        staffFrame.getChildren().addAll(instBackground, staffPane, staffMeasureNums, staffMeasureLines, staffLedgerLines[0], staffLedgerLines[1], staffLedgerLines[2], staffLedgerLines[3], staffLedgerLines[4], staffPlayBars, staffInstrumentsAccidentals[0], staffInstrumentsAccidentals[1]);
        staffFrame.setPadding(new Insets(2));
    }

    /**
     * Initializes the volume bars in the program.
     */
    private void initializeVolumeBars() {
        volumeBarHandlers = new ArrayList<StaffVolumeEventHandler>();
        
        for (int i = 0; i < width; i++) {
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
        for (int i = 0; i < width; i++) {
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
    private Node[] initializeStaffInstruments() {
    	Map<StaffNoteCoordinate, ImageView> map = new HashMap<>();
    	Map<StaffNoteCoordinate, ImageView> mapAcc = new HashMap<>();
    	
        HBox staffInstruments = new HBox();
        staffInstruments.setPrefHeight(504);
        staffInstruments.setPrefWidth(982);
        staffInstruments.setAlignment(Pos.CENTER_LEFT);
        staffInstruments.setLayoutX(70);
        staffInstruments.setLayoutY(-5);
        staffInstruments.setPadding(new Insets(0, 0, 0, 113));
        
        for (int col = 0; col < width; col++) {
            VBox vBox = new VBox();
            vBox.setSpacing(-20.0);
            vBox.setAlignment(Pos.CENTER);
                        
            for (int row = 0; row < height; row++) {
                StackPane stack = new StackPane();
                stack.setDisable(true);
                stack.setPrefHeight(36.0);
                stack.setPrefWidth(64.0);
                
                for (int d = 0; d < depth; d++) {
                    ImageView iv = makeNoteImageView();
                    stack.getChildren().add(iv);
                    StaffNoteCoordinate coord = new StaffNoteCoordinate(col, row, d);
                    map.put(coord, iv);
                }
                
                ImageView silIv = makeNoteImageView();
                stack.getChildren().add(silIv);
                StaffNoteCoordinate coord = new StaffNoteCoordinate(col, row, -1);
                map.put(coord, silIv);
                
                vBox.getChildren().add(0, stack);
            }
            
            staffInstruments.getChildren().add(vBox);
        }
        
        HBox staffAccidentals = new HBox(32);
        staffAccidentals.setPrefHeight(504);
        staffAccidentals.setPrefWidth(982);
        staffAccidentals.setAlignment(Pos.CENTER_LEFT);
        staffAccidentals.setLayoutX(70);
        staffAccidentals.setLayoutY(-5);
        staffAccidentals.setPadding(new Insets(0, 0, 0, 105));
        
        for (int col = 0; col < width; col++) {
            VBox vBox = new VBox();
            vBox.setSpacing(-20.0);
            vBox.setAlignment(Pos.CENTER);
            vBox.setPrefHeight(472.0);
            vBox.setPrefWidth(32.0);
                        
            for (int row = 0; row < height; row++) {
                StackPane stack = new StackPane();
                stack.setDisable(true);
                stack.setPrefHeight(36.0);
                stack.setPrefWidth(32.0);
                
                for (int d = 0; d < depth; d++) {
                    ImageView iv = makeAccidentalImageView();
                    stack.getChildren().add(iv);
                    StaffNoteCoordinate coord = new StaffNoteCoordinate(col, row, d);
                    mapAcc.put(coord, iv);
                }
                
                ImageView silIv = makeAccidentalImageView();
                stack.getChildren().add(silIv);
                StaffNoteCoordinate coord = new StaffNoteCoordinate(col, row, -1);
                mapAcc.put(coord, silIv);
                
                vBox.getChildren().add(0, stack);
            }
            
            staffAccidentals.getChildren().add(vBox);
        }
        
        matrix.initializeNoteDisplay(map, mapAcc);
        
        Node[] ret = { staffInstruments, staffAccidentals };
        return ret;
    }
    
    private static ImageView makeNoteImageView() {
        ImageView iv = new ImageView();
        iv.setVisible(false);
        iv.setManaged(false);
        iv.setFitHeight(36);
        iv.setFitWidth(32);
        return iv;
    }
    
    private static ImageView makeAccidentalImageView() {
        ImageView iv = new ImageView();
        iv.setVisible(false);
        iv.setManaged(false);
        iv.setFitHeight(32);
        iv.setFitWidth(32);
        iv.setTranslateX(-18);
        return iv;
    }
    
    public void updateNoteDisplay(StaffSequence seq, int currLine) {
        matrix.clearNoteDisplay();
        matrix.populateNoteDisplay(seq, currLine);
    }

    /**
     * These are the lines that divide up the staff.
     */
    private Node initializeStaffMeasureLines() {
        HBox staffMeasureLines = new HBox(62);
        staffMeasureLines.setPrefHeight(276);
        staffMeasureLines.setPrefWidth(982);
        staffMeasureLines.setAlignment(Pos.CENTER_LEFT);
        staffMeasureLines.setLayoutX(54);
        staffMeasureLines.setLayoutY(24);
        staffMeasureLines.setPadding(new Insets(0, 0, 0, 144));
        
        measureLines = new ArrayList<ImageView>();
        
        for (int i = 0; i < width; i++) {
            ImageView iv = new ImageView();
            iv.setFitHeight(448.0);
            iv.setFitWidth(2.0);
            iv.setPickOnBounds(true);
            iv.setPreserveRatio(true);
            
            staffMeasureLines.getChildren().add(iv);
            measureLines.add(iv);
        }
        
        return staffMeasureLines;
    }
    
    private Node initializeStaffMeasureNums() {
        HBox staffMeasureNums = new HBox(22);
        staffMeasureNums.setPrefHeight(12);
        staffMeasureNums.setPrefWidth(982);
        staffMeasureNums.setAlignment(Pos.CENTER_LEFT);
        staffMeasureNums.setLayoutX(54);
        staffMeasureNums.setLayoutY(5);
        staffMeasureNums.setPadding(new Insets(0, 0, 0, 124));
        
        measureNums = new ArrayList<Text>();
        
        for (int i = 0; i < width; i++) {
            HBox box = new HBox();
            box.setAlignment(Pos.CENTER);
            box.setPrefHeight(9.0);
            box.setPrefWidth(42.0);
            
            staffMeasureNums.getChildren().add(box);
            Text t = new Text();
            box.getChildren().add(t);
            measureNums.add(t);
        }
        
        return staffMeasureNums;
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
    private Node[] initializeStaffLedgerLines() {
        HBox[] staffLedgerLines = new HBox[5];

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
            
            for (int i = 0; i < width; i++) {
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
        
        return staffLedgerLines;
    }
    
    public void updateStaffLedgerLines(StaffSequence seq, int currLine) {
        for (int i = 0; i < width; i++) {
            StaffNoteLine stl = seq.getLineSafe(currLine + i);

            int high = 0;
            int low = height;
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
    
    public Node initializeStaffPlayBars() {
        HBox staffPlayBars = new HBox(8);
        staffPlayBars.setPrefHeight(448);
        staffPlayBars.setPrefWidth(982);
        staffPlayBars.setAlignment(Pos.CENTER_LEFT);
        staffPlayBars.setLayoutX(54);
        staffPlayBars.setLayoutY(23);
        staffPlayBars.setPadding(new Insets(0, 0, 0, 117));
        
        for (int i = 0; i < width; i++) {
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
        
        return staffPlayBars;
    }
    
    private void doResetPlayBars() {
        if (activePlaybar >= 0 && activePlaybar < width)
            playbars[activePlaybar].setVisible(false);
    }
    
    public void resetPlayBars() {
        doResetPlayBars();
        activePlaybar = -1;
    }
    
    public void updatePlayBars(int position) {
        doResetPlayBars();
        
        if (position < 0 || position >= width)
            return;
        
        playbars[position].setVisible(true);
        activePlaybar = position;
    }
    
    /**
     * Immutable object providing the representation for a coordinate on the staff: column, row, and depth
     * For given maximum values of row and depth, we provide converters to and from int.
     */
    protected class StaffNoteCoordinate {
    	
    	final public int col;
    	final public int row;
    	final public int dep; // special value -1 refers to the layer for silhouettes (dep is irrelevant)
    	
    	public StaffNoteCoordinate(int col, int row, int dep) {
    		this.col = col;
    		this.row = row;
    		this.dep = dep;
    	}
    	
    	public StaffNoteCoordinate(StaffNoteCoordinate oth) {
    		this.col = oth.col;
    		this.row = oth.row;
    		this.dep = oth.dep;
    	}
    	
    	public boolean equals(StaffNoteCoordinate oth) {
    		return this.col == oth.col && this.row == oth.row && this.dep == oth.dep;
    	}
    	
    	public int lin() {
    		return (dep == -1) ? (height * col) + row : (depth * ((height * col) + row)) + dep;
    	}
    	
    }

}
