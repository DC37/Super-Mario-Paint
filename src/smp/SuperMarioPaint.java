package smp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import smp.fx.SMPFXController;
import smp.fx.SplashScreen;

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
     * Location of the Arrangement Window fxml file.
     */
    @SuppressWarnings("unused")
    private String arrFxml = "./ArrWindow.fxml";

    /**
     * Location of the Advanced Mode (super secret!!)
     * fxml file.
     */
    @SuppressWarnings("unused")
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
    private ImageLoader imgLoader = new ImageLoader();

    /**
     * Loads the soundfonts that will be used in Super Mario Paint.
     */
    private SoundfontLoader sfLoader = new SoundfontLoader();

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
        while (imgLd.isAlive() || sfLd.isAlive())
            try {
                Thread.sleep(1);
                double imgStatus = imgLoader.getLoadStatus();
                double sfStatus = sfLoader.getLoadStatus();
                dummyPreloader.updateStatus((imgStatus + sfStatus) * 100, NUM_THREADS);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
            primaryStage.setScene(new Scene(root, 800, 600));
            SMPFXController.initializeHandlers();
            dummyPreloader.updateStatus(NUM_THREADS * 100, NUM_THREADS);
            primaryStage.show();
            dummyPreloader.dispose();
        } catch (MalformedURLException e) {
            /* Can't recover. */
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            /* Can't recover. */
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Launches the application.
     * @param unused Currrently unused arguments for run configs.
     */
    public static void main(String... unused) {
        launch(unused);
    }

}


