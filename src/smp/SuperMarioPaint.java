package smp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import smp.fx.SMPFXController;
import smp.fx.SplashScreen;
import smp.stateMachine.Settings;
import smp.stateMachine.StateMachine;


/**
 * Super Mario Paint <br>
 * Based on the old SNES game from 1992, Mario Paint <br>
 * Inspired by:<br>
 * MarioSequencer (2002) <br>
 * TrioSequencer <br>
 * Robby Mulvany's Mario Paint Composer 1.0 / 2.0 (2007-2008) <br>
 * FordPrefect's Advanced Mario Sequencer (2009) <br>
 * The GUI is primarily written with JavaFX2.2. <br>
 * @author RehdBlob
 * @since 2012.08.16
 * @version 1.00
 */
public class SuperMarioPaint extends Application {

    /**
     * Location of the Main Window fxml file.
     */
    private String mainFxml = "./MainWindow.fxml";

    /**
     * Location of the Advanced Mode (super secret!!)
     * fxml file.
     */
    private String advFxml = "./AdvWindow.fxml";

    /**
     * The number of threads that are running to load things for Super Mario
     * Paint.
     */
    private static final int NUM_THREADS = 2;

    /**
     * Until I figure out the mysteries of the Preloader class in
     * JavaFX, I will stick to what I know, which is swing, unfortunately.
     */
    private SplashScreen dummyPreloader = new SplashScreen();

    /**
     * Loads all the sprites that will be used in Super Mario Paint.
     */
    private Loader imgLoader = new ImageLoader();

    /**
     * Loads the soundfonts that will be used in Super Mario Paint.
     */
    private Loader sfLoader = new SoundfontLoader();

    /**
     * Starts three <code>Thread</code>s. One of them is currently
     * a dummy splash screen, the second an <code>ImageLoader</code>,
     * and the third one a <code>SoundfontLoader</code>.
     * @see ImageLoader
     * @see SoundfontLoader
     * @see SplashScreen
     */
    @Override
    public void init() {
        Thread splash = new Thread(dummyPreloader);
        splash.start();
        Thread imgLd = new Thread(imgLoader);
        Thread sfLd = new Thread(sfLoader);
        sfLd.start();
        imgLd.start();
        while (imgLd.isAlive() || sfLd.isAlive()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double imgStatus = imgLoader.getLoadStatus();
            double sfStatus = sfLoader.getLoadStatus();
            dummyPreloader.updateStatus((imgStatus + sfStatus) * 100, NUM_THREADS);
        }

    }

    /**
     * Starts the application and loads the FXML file that contains
     * a lot of the class hierarchy.
     * @param primaryStage The primary stage that will be showing the
     * main window of Super Mario Paint.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root =
                    (Parent) FXMLLoader.load(
                            new File(mainFxml).toURI().toURL());
            primaryStage.setTitle("Super Mario Paint");
            primaryStage.setResizable(true);
            Scene primaryScene = new Scene(root, 800, 600);
            primaryStage.setScene(primaryScene);
            SMPFXController.initializeHandlers();
            makeKeyboardListeners(primaryScene);
            dummyPreloader.updateStatus(NUM_THREADS * 100, NUM_THREADS);
            primaryStage.show();
            dummyPreloader.dispose();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates the keyboard listeners that we will be using for various
     * other portions of the program. Ctrl, alt, and shift are of interest
     * here, but the arrow keys will also be considered.
     * @param primaryScene The main window.
     */
    private void makeKeyboardListeners(Scene primaryScene) {
        primaryScene.addEventHandler(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent ke) {
                KeyCode n = ke.getCode();
                if (n == KeyCode.CONTROL) {
                    StateMachine.setCtrlPressed();
                } else if (n == KeyCode.ALT || n == KeyCode.ALT_GRAPH) {
                    StateMachine.setAltPressed();
                } else if (n == KeyCode.SHIFT) {
                    StateMachine.setShiftPressed();
                }
                StateMachine.updateFocusPane();
                ke.consume();
            }
        });

        primaryScene.addEventHandler(KeyEvent.KEY_RELEASED,
                new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent ke) {
                KeyCode n = ke.getCode();
                if (n == KeyCode.CONTROL) {
                    StateMachine.resetCtrlPressed();
                } else if (n == KeyCode.ALT || n == KeyCode.ALT_GRAPH) {
                    StateMachine.resetAltPressed();
                } else if (n == KeyCode.SHIFT) {
                    StateMachine.resetShiftPressed();
                }
                StateMachine.updateFocusPane();
                ke.consume();
            }
        });

    }

    /**
     * Launches the application.
     * @param args Sets debug options on or off.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            try {
                launch(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (args.length > 0 && (args[0].equals("--debug")
                || args[0].equals("--d"))) {
            if (args[1] != null) {
                try {
                    Settings.setDebug(Integer.parseInt(args[1]));
                } catch (NumberFormatException e) {
                    Settings.setDebug(1);
                }
            } else {
                Settings.setDebug(1);
            }
            try {
                launch(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}


