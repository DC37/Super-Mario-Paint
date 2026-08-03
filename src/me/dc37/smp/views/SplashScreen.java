package me.dc37.smp.views;

import gui.Dialog;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import me.dc37.smp.controllers.SplashScreenController;

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
@Slf4j
public class SplashScreen extends Preloader {

	/** The stage to display on. */
	private Stage stage;
	
	private final SplashScreenController controller = new SplashScreenController();
	
	/** Creates and starts the preloader window. */
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		stage.setScene(new Scene(controller.getView(), 300, 150));
		stage.setTitle(controller.getTitle());
		stage.getIcons().add(controller.getHeaderIcon());
		stage.setResizable(false);
		
		stage.show();
	}
	
	@Override
	public void handleProgressNotification(ProgressNotification pn) {
		/*
		 * Application loading progress is rescaled to be first 50%.
         * Even if there is nothing to load 0% and 100% events can be delivered.
         */
		double newProgress = pn.getProgress();
		boolean isNoLoadingProgress = controller.isNoLoadingProgress();
		
		if (newProgress != 1.0 || !isNoLoadingProgress) {
			controller.setProgress(newProgress / 2);
			if (newProgress > 0) {
				controller.setNoLoadingProgress(false);
			}
		}
	}
	
	@Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        /* ignore, hide after application signals it is ready */
    }
	
	@Override
	public void handleApplicationNotification(PreloaderNotification notif) {
		if (notif instanceof ProgressNotification pn) {
            /* expect application to send us progress notifications
               with progress ranging from 0 to 1.0 */
            double v = pn.getProgress();
            controller.setProgress(v);
            
        } else if (notif instanceof StateChangeNotification) {
            /* hide after get any state update from application */
            stage.hide();
            
        } else if (notif instanceof ErrorNotification en
        		&& !handleErrorNotification(en)) {
        	
            stage.close();
        }
	}
	
	@Override
	public boolean handleErrorNotification(ErrorNotification en) {
		Dialog.showDialog("Super Mario Paint has encountered the following error:\n" + en.getCause().getMessage());
        log.error("Exception cause:", en.getCause());
        return false;
	}

}
