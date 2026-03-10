package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import backend.sound.SoundPlayer;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;
import gui.loaders.Loader;
import gui.loaders.SMPCursorType;
import gui.loaders.SoundfontLoader;
import javafx.application.Application;
import javafx.application.Preloader.ErrorNotification;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Super Mario Paint <br>
 * Based on the old SNES game from 1992, Mario Paint <br>
 * Inspired by:<br>
 * MarioSequencer (2002) <br>
 * TrioSequencer <br>
 * Robby Mulvany's Mario Paint Composer 1.0 / 2.0 (2007-2008) <br>
 * FordPrefect's Advanced Mario Sequencer (2009) <br>
 * The GUI is primarily written with JavaFX <br>
 *
 * Dev team: 
 * RehdBlob (2012 - current)
 * j574y923 (2017 - current)
 * CyanSMP64 (2019 - current)
 * seymour (2020 - current)
 * rozlynd (2024 - current)
 *
 * @author RehdBlob
 * @author j574y923
 * @author CyanSMP64
 * @author seymour
 * @author rozlynd
 * 
 * @since 2012.08.16
 * @version 1.4.4
 */
public class SuperMarioPaint extends Application  {

    /**
     * Loads all the sprites that will be used in Super Mario Paint.
     */
    private Loader<Map<ImageIndex, Image>> imgLoader;
    
    private RunnableFuture<Map<ImageIndex, Image>> imagesHolderFuture;
    
    private Map<SMPCursorType, ImageCursor> cursorImages = new HashMap<>();

    /**
     * Loads the soundfonts that will be used in Super Mario Paint.
     */
    private Loader<SoundPlayer> sfLoader;
    
    private RunnableFuture<SoundPlayer> soundPlayerFuture;

    /** Image Loader thread. */
    private Thread imgLd;

    /** Soundfont loader thread. */
    private Thread sfLd;

    /** This is the main application stage. */
    private Stage primaryStage;

    /** This is the primary Scene on the main application stage. */
    private Scene primaryScene;

    /** This is the loaded FXML file. */
    private Parent root;

    /**
     * The controller class for the FXML.
     */
    private SMPFXController controller = new SMPFXController();
    
    private Task<Void> preloaderTask;

    /**
     * This should hopefully get something up on the screen quickly. This is
     * taken from http://docs.oracle.com/javafx/2/deployment/preloaders.htm
     */
    private void longStart() throws Exception {
        sfLd.start();
        imgLd.start();
        
        do {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            double imgStatus = imgLoader.getLoadStatus();
            double sfStatus = sfLoader.getLoadStatus();
            double ld = (imgStatus + sfStatus) / 2.0;
            notifyPreloader(new ProgressNotification(ld));
            
        } while (imgLd.isAlive() || sfLd.isAlive());
        
        Map<ImageIndex, Image> imagesHolder = imagesHolderFuture.get();
        SoundPlayer soundPlayer = soundPlayerFuture.get();
        
        cursorImages.put(SMPCursorType.HAND_POINTING, new ImageCursor(imagesHolder.get(ImageIndex.CURSOR_0)));
        cursorImages.put(SMPCursorType.HAND_OPEN, new ImageCursor(imagesHolder.get(ImageIndex.CURSOR_1)));
        cursorImages.put(SMPCursorType.HAND_CLOSED, new ImageCursor(imagesHolder.get(ImageIndex.CURSOR_2)));
        cursorImages.put(SMPCursorType.ERASER, new ImageCursor(imagesHolder.get(ImageIndex.CURSOR_3)));
        
        FXMLLoader loader = new FXMLLoader();
        controller.setImagesHolder(imagesHolder);
        controller.setSoundPlayer(soundPlayer);
        loader.setController(controller);
        File fxml = FileUtils.getResourceFile(Values.FXML, Values.SMP_FOLDER, true);
        loader.setLocation(fxml.toURI().toURL());
        
        root = (Parent) loader.load();
        notifyPreloader(new ProgressNotification(0.75));
    }
    
    private void doStart() {
        try {
            primaryStage.setTitle("Super Mario Paint " + Settings.version);
            primaryStage.setOnCloseRequest(event -> {
                handleCloseRequest();
                event.consume(); // Keep the window from closing
            });
            primaryStage.setResizable(false);
            primaryScene = new Scene(root);
            primaryScene.getStylesheets().add(SuperMarioPaint.class.getResource("/resources/style.css").toString());
            
            primaryStage.setScene(primaryScene);
            
            primaryStage.focusedProperty().addListener(
                    new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean t, Boolean t1) {
                            StateMachine.clearKeyPresses();
                        }
                    });
            
            makeMouseEventHandlers();
            
            notifyPreloader(new ProgressNotification(1));
            notifyPreloader(new StateChangeNotification(
                    StateChangeNotification.Type.BEFORE_START));
            
            /* @since 2020.4.28 - seymour
             * Changes the cursor image */
            setCursor(SMPCursorType.HAND_POINTING);
            
            // 1 chance out of 10 to set a random instrument as icon image
            int randValue = (int) Math.floor(Math.random() * 10 * InstrumentIndex.values().length);
            String iconFile = (randValue < InstrumentIndex.values().length) ?
            	InstrumentIndex.values()[randValue].name() :
            	"ICON";
            Image iconImage = new Image(FileUtils.getResourceURL(iconFile + ".png").toString());
    		primaryStage.getIcons().add(iconImage);
            
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /** Explicitly create constructor without arguments. */
    public SuperMarioPaint() {

    }

    /**
     * Starts three <code>Thread</code>s. One of them is currently a dummy
     * splash screen, the second an <code>ImageLoader</code>, and the third one
     * a <code>SoundfontLoader</code>.
     *
     * @see ImageLoader
     * @see SoundfontLoader
     * @see SplashScreen
     */
    @Override
    public void init() {
    	imgLoader = new ImageLoader();
    	imagesHolderFuture = new FutureTask<>(imgLoader);
        imgLd = new Thread(imagesHolderFuture);
        
        sfLoader = new SoundfontLoader();
        soundPlayerFuture = new FutureTask<>(sfLoader);
        sfLd = new Thread(soundPlayerFuture);
    }
    
    /**
     * Starts the application and loads the FXML file that contains a lot of the
     * class hierarchy.
     *
     * @param primaryStage
     *            The primary stage that will be showing the main window of
     *            Super Mario Paint.
     */
    @Override
    public void start(Stage ps) {
        primaryStage = ps;

        preloaderTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                longStart();
                return null;
            }
        };
        
        preloaderTask.setOnSucceeded(event -> doStart());
        preloaderTask.setOnFailed(event -> manageLoadFailure());
        new Thread(preloaderTask).start();
    }

    private void manageLoadFailure() {        
        if (preloaderTask == null) {
            return;
        }
        
        notifyPreloader(new ErrorNotification("Unknown", "Unknown", preloaderTask.getException()));
    }

    /**
     * Protip for JavaFX users: Make sure you define your application close
     * behavior.
     */
    @Override
    public void stop() {
//    	Platform.exit();
        System.exit(0);
    }

    /**
     * Got this off of https://community.oracle.com/thread/2247058?tstart=0 This
     * appears quite useful as a 'really exit?' type thing. This dialog
     * currently needs some work, so we're not going to include it in the alpha
     * release.
     */
    private void handleCloseRequest() {
        String mssg;
        
        if (StateMachine.isSongModified()
                && StateMachine.isArrModified()) {
            mssg = "The song and arrangement have\n"
                    + "both not been saved! Really exit?";
        } else if (StateMachine.isSongModified()) {
            mssg = "The song has not been saved! "
                    + "Really exit?";
        } else if (StateMachine.isArrModified()) {
            mssg = "The arrangement has not been saved! "
                    + "Really exit?";
        } else {
            stop();
            return;
        }
    
        if (Dialog.showYesNoDialog(mssg, primaryStage))
            stop();
    }
    
    private void makeMouseEventHandlers() {
        primaryScene.addEventHandler(MouseEvent.ANY, controller.getStaffMouseEventHandler());
        
        ArrayList<MouseButton> mouseButtons = new ArrayList<MouseButton>();
        
        // Just a temporary thing to change mouse until i (or someone else) can find out where to put it =P -- seymour
        primaryScene.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    
                    @Override
                    public void handle(MouseEvent m) {
                        if (!mouseButtons.contains(m.getButton()))
                            mouseButtons.add(m.getButton());
                        
                        if (mouseButtons.contains(MouseButton.MIDDLE) || (StateMachine.isClipboardPressed() && m.getButton() != MouseButton.SECONDARY))
                            setCursor(SMPCursorType.HAND_CLOSED);
                        else if (mouseButtons.contains(MouseButton.PRIMARY))
                            setCursor(SMPCursorType.HAND_OPEN);
                        else if (mouseButtons.contains(MouseButton.SECONDARY) && !StateMachine.isClipboardPressed())
                            setCursor(SMPCursorType.ERASER);
                        
                        m.consume();
                    }
        });
        primaryScene.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
            
                    @Override
                    public void handle(MouseEvent m) {
                        // Added to remove the default cursor appearing while other mouse buttons are held
                        mouseButtons.remove(m.getButton());
                        
                        if (mouseButtons.contains(MouseButton.MIDDLE) || (StateMachine.isClipboardPressed() && m.getButton() != MouseButton.SECONDARY))
                            setCursor(SMPCursorType.HAND_CLOSED);
                        else if (mouseButtons.contains(MouseButton.PRIMARY))
                            setCursor(SMPCursorType.HAND_OPEN);
                        else if (mouseButtons.contains(MouseButton.SECONDARY) && !StateMachine.isClipboardPressed())
                            setCursor(SMPCursorType.ERASER);
                        
                        if (mouseButtons.isEmpty())
                            setCursor(SMPCursorType.HAND_POINTING);
                        
                        m.consume();
                    }
        });
    }

    /**
     * Launches the application.
     *
     * @param args
     *            Sets debug options on or off.
     */
    public static void main(String[] args) {
        if (args.length > 0 && (args[0].equals("--debug") || args[0].equals("--d"))) {
            if (args.length > 1) {
                try {
                    Integer i = Integer.parseInt(args[1]);
                    Settings.setDebug(i >= 0 ? i : -1);
                } catch (NumberFormatException e) {
                    Settings.setDebug(0);
                }
            } else {
                Settings.setDebug(Integer.MIN_VALUE);
            }   
        }
            
        try {
            System.setProperty("javafx.preloader", SplashScreen.class.getName());
            Application.launch(SuperMarioPaint.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
    
    public void setCursor(SMPCursorType type) {
    	primaryScene.setCursor(cursorImages.get(type));
    }
    
}
