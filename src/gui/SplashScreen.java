package gui;

import java.io.File;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
 * @author seymour
 * @since 2014.12.30
 */
public class SplashScreen extends Preloader {

    /** Basic progress bar. */
    ProgressBar bar;
    
    /** Loading image */
    ImageView imageview;

    /** The stage to display on. */
    Stage stage;

    /** */
    boolean noLoadingProgress = true;

    /**
     * @return The created scene.
     * @throws  
     */
    private Scene createPreloaderScene() {
        bar = new ProgressBar(0);
        BorderPane p = new BorderPane();
        p.setCenter(bar);
        /* @since 1.4, to spice up the load screen. why not? - seymour */
        File loadingGif;
        try {
            loadingGif = Utilities.getResourceFile("LOADING_ANIM.gif", Values.SPRITES_FOLDER);
            imageview = new ImageView();
            imageview.setImage(new Image(loadingGif.toURI().toURL().toString()));
            imageview.setFitWidth(236);
            imageview.setFitHeight(36);
            imageview.setTranslateX(32);
            imageview.setTranslateY(32);
            p.setTop(imageview);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } else {
            stage.setTitle("Loading...");
        }
        stage.getIcons().add(new Image("file:./sprites/ICON.png"));
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        /* application loading progress is rescaled to be first 50%
           Even if there is nothing to load 0% and 100% events can be
           delivered */
        if (pn.getProgress() != 1.0 || !noLoadingProgress) {
            bar.setProgress(pn.getProgress() / 2);
            if (pn.getProgress() > 0) {
                noLoadingProgress = false;
            }
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        /* ignore, hide after application signals it is ready */
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification pn) {
        if (pn instanceof ProgressNotification) {
            /* expect application to send us progress notifications
               with progress ranging from 0 to 1.0 */
            double v = ((ProgressNotification) pn).getProgress();
            bar.setProgress(v);
            
        } else if (pn instanceof StateChangeNotification) {
            /* hide after get any state update from application */
            stage.hide();
            
        } else if (pn instanceof ErrorNotification) {
        	if (!handleErrorNotification((ErrorNotification)pn)) {
        		stage.close();
        	}
        }
    }
    
    @Override
    public boolean handleErrorNotification(ErrorNotification en) {
    	Dialog.showDialog("Super Mario Paint has encountered the following error:\n" + en.getCause().getMessage());
    	en.getCause().printStackTrace();
    	return false;
    }
}
