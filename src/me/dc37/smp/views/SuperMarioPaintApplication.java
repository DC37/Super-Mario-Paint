package me.dc37.smp.views;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import gui.Dialog;
import gui.Settings;
import gui.StateMachine;
import gui.loaders.ImageLoader;
import gui.loaders.SMPCursorType;
import gui.loaders.SoundfontLoader;
import gui.resources.SMPResourceType;
import gui.resources.SMPResourceUtil;
import javafx.application.Application;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import me.dc37.smp.controllers.SMPAppController;
import me.dc37.smp.models.SMPAppModel;

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
 * Aura Lesse Programmer (2026 - current)
 *
 * @author RehdBlob
 * @author j574y923
 * @author CyanSMP64
 * @author seymour
 * @author rozlynd
 * @author aura-lsprog-86
 * 
 * @since 2012.08.16
 * @version 1.4.4
 */
@Slf4j
public class SuperMarioPaintApplication extends Application {

    private final SMPAppController controller = new SMPAppController();
	
    /**
     * Starts two <code>Thread</code>s: one is an <code>ImageLoader</code>,
     * and the other a <code>SoundfontLoader</code>.
     *
     * @see ImageLoader
     * @see SoundfontLoader
     */
	@Override
	public void init() throws Exception {
		controller.prepareLoaders();
	}
    
	/**
     * Starts the application and loads the FXML file
     * that contains a lot of the class hierarchy.
     *
     * @param stage The primary stage that will be
     *              showing the main window of
     *              Super Mario Paint.
     */
	@Override
	public void start(Stage stage) throws Exception {
		controller.triggerLoad(stage, this::notifyPreloader, this::prepareView);
	}
	
	@Override
	public void stop() {
		System.exit(0);
	}
	
	public void prepareView(Stage stage, SMPAppModel model) {
		try {
			stage.setTitle("Super Mario Paint " + Settings.VERSION);
			stage.setOnCloseRequest(event -> {
                handleCloseRequest(stage);
                event.consume(); // Keep the window from closing
            });
			stage.setResizable(false);
			
			Scene scene = new Scene(controller.getView());
			scene.getStylesheets().add(SMPResourceUtil.get("style.css", SMPResourceType.STYLE).toString());
			stage.setScene(scene);
            
			stage.focusedProperty().addListener(
                    (ov, t, t1) -> StateMachine.clearKeyPresses());
            
            makeMouseEventHandlers(scene, model);
            
            notifyPreloader(new ProgressNotification(1));
            notifyPreloader(new StateChangeNotification(
                    StateChangeNotification.Type.BEFORE_START));
            
            /* @since 2020.4.28 - seymour
             * Changes the cursor image */
            setCursor(scene, SMPCursorType.HAND_POINTING);
            
            stage.getIcons().add(controller.getHeaderIcon());
            stage.show();
            
        } catch (Exception e) {
            log.error("Error in doStart:", e);
            System.exit(1);
        }
	}
	
	/**
     * Got this off of https://community.oracle.com/thread/2247058?tstart=0 This
     * appears quite useful as a 'really exit?' type thing. This dialog
     * currently needs some work, so we're not going to include it in the alpha
     * release.
     */
    private void handleCloseRequest(Stage stage) {
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
    
        if (Dialog.showYesNoDialog("HOLD IT!", mssg, stage))
            stop();
    }
    
    private void makeMouseEventHandlers(Scene scene, SMPAppModel model) {
        SMPAppViewFXController fxCtrl = controller.getFxController();
        scene.addEventHandler(MouseEvent.ANY, fxCtrl.getStaffMouseEventHandler());
        
        List<MouseButton> mouseButtons = new ArrayList<>();
        
        Consumer<MouseEvent> fnSetSpecialCursors = (MouseEvent m) -> {
        	if (mouseButtons.contains(MouseButton.MIDDLE) || (model.isClipboardPressed() && m.getButton() != MouseButton.SECONDARY))
	            setCursor(scene, SMPCursorType.HAND_CLOSED);
	        else if (mouseButtons.contains(MouseButton.PRIMARY))
	            setCursor(scene, SMPCursorType.HAND_OPEN);
	        else if (mouseButtons.contains(MouseButton.SECONDARY) && !model.isClipboardPressed())
	            setCursor(scene, SMPCursorType.ERASER);
	    };
        
        // Just a temporary thing to change mouse until i (or someone else) can find out where to put it =P -- seymour
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent m) -> {
            if (!mouseButtons.contains(m.getButton()))
                mouseButtons.add(m.getButton());
            
            fnSetSpecialCursors.accept(m);
            
            m.consume();
        });
        scene.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent m) -> {
            // Added to remove the default cursor appearing while other mouse buttons are held
            mouseButtons.remove(m.getButton());
            
            fnSetSpecialCursors.accept(m);
            
            if (mouseButtons.isEmpty())
                setCursor(scene, SMPCursorType.HAND_POINTING);
            
            m.consume();
        });
    }
    
    private void setCursor(Scene scene, SMPCursorType type) {
    	ImageCursor imgCur = controller.getCursor(type).orElseThrow();
        scene.setCursor(imgCur);
    }
	
}
