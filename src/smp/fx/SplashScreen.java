package smp.fx;

import smp.stateMachine.Settings;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Splash screen for Super Mario Paint. For some reason, Mac OSX doesn't like it
 * when programs use both Swing and JavaFX, so we'll use a full JavaFX
 * implementation of this. This re-write replaces the terrible swing
 * implementation that persisted from 2012.08.17 to 2014.12.30. The basic
 * portion of this is taken directly from
 * http://docs.oracle.com/javafx/2/deployment/preloaders.htm
 *
 * @author RehdBlob
 * @since 2014.12.30
 */
public class SplashScreen extends Preloader {

    /** Basic progress bar. */
    ProgressBar bar;

    /** The stage to display on. */
    Stage stage;

    /** */
    boolean noLoadingProgress = true;

    /**
     * @return The created scene.
     */
    private Scene createPreloaderScene() {
        bar = new ProgressBar(0);
        BorderPane p = new BorderPane();
        p.setCenter(bar);
        return new Scene(p, 300, 150);
    }

    /** Creates and starts the preloader window. */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setScene(createPreloaderScene());
        double c = java.lang.Math.random();
        if (c < 0.2) {
            stage.setTitle("Loading...");
        } else if (c < 0.5) {
            stage.setTitle("SMP" + Settings.version);
        } else if (c < 0.8) {
            stage.setTitle("Made by: RehdBlob");
        } else {
            stage.setTitle("Loading...");
        }
        stage.show();
    }

    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        // application loading progress is rescaled to be first 50%
        // Even if there is nothing to load 0% and 100% events can be
        // delivered
        if (pn.getProgress() != 1.0 || !noLoadingProgress) {
            bar.setProgress(pn.getProgress() / 2);
            if (pn.getProgress() > 0) {
                noLoadingProgress = false;
            }
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        // ignore, hide after application signals it is ready
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification pn) {
        if (pn instanceof ProgressNotification) {
            // expect application to send us progress notifications
            // with progress ranging from 0 to 1.0
            double v = ((ProgressNotification) pn).getProgress();
            bar.setProgress(v);
        } else if (pn instanceof StateChangeNotification) {
            // hide after get any state update from application
            stage.hide();
        }
    }
}
