package me.dc37.smp.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

import backend.sound.SoundPlayer;
import gui.Values;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;
import gui.loaders.SMPCursorType;
import gui.loaders.SoundfontLoader;
import gui.resources.FetchStrategy;
import gui.resources.SMPResourceType;
import gui.resources.SMPResourceUtil;
import javafx.application.Preloader.ErrorNotification;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import me.dc37.smp.interactors.LoaderWorker;
import me.dc37.smp.interactors.SMPAppInteractor;
import me.dc37.smp.models.SMPAppModel;
import me.dc37.smp.views.SMPAppViewFXController;
import me.dc37.smp.views.SuperMarioPaintApplication;

@Slf4j
public class SMPAppController {

	private final SuperMarioPaintApplication app;
	private final SMPAppModel model;
	
	@SuppressWarnings("unused")
	private final SMPAppInteractor interactor;
	
	private PreloaderTask preloaderTask;
	
	/**
     * Loads all the sprites that will be used in Super Mario Paint.
     */
	private LoaderWorker<Map<ImageIndex, Image>, ImageLoader> imageLoader;
	
	/**
     * Loads the soundfonts that will be used in Super Mario Paint.
     */
	private LoaderWorker<SoundPlayer, SoundfontLoader> soundfontLoader;
	
	public SMPAppController(SuperMarioPaintApplication app) {
		this.app = app;
		
		model = SMPAppModel.getInstance();
		interactor = new SMPAppInteractor(model);
	}
	
	public void prepareLoaders() {
		imageLoader = new LoaderWorker<>(new ImageLoader());
		soundfontLoader = new LoaderWorker<>(new SoundfontLoader());
	}
	
	public void triggerLoad(Stage stage) {
		preloaderTask = new PreloaderTask(
				model, imageLoader, soundfontLoader, app::notifyPreloader);
		
		preloaderTask.setOnSucceeded(event -> app.prepareView(stage));
        preloaderTask.setOnFailed(event -> manageLoadFailure());
        
        new Thread(preloaderTask).start();
	}
	
	private void manageLoadFailure() {
        if (preloaderTask == null) {
            return;
        }
        
        app.notifyPreloader(new ErrorNotification("Unknown", "Unknown", preloaderTask.getException()));
    }
	
	public Region getView(SMPAppViewFXController ctrl) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setController(ctrl);
		
		// We have to copy the FXML onto the user file system
		// because it expects a "sprites" folder.
		// But also, we may update it regularly as we develop,
		// so we want to always use our internal version.
		URL fxml = SMPResourceUtil.get(Values.FXML, SMPResourceType.UI,
				FetchStrategy.COPY_INTERNAL, Values.SMP_FOLDER);
		loader.setLocation(fxml);
		
		return loader.load();
	}
	
	public Map<ImageIndex, Image> getIcons() {
		return model.getIcons();
	}
	
	public Optional<Image> getIcon(ImageIndex imgIdx) {
		return model.getIcon(imgIdx);
	}
	
	public SoundPlayer getSoundPlayer() {
		return model.getSoundPlayer();
	}
	
	public Image getHeaderIcon() {
		return model.getHeaderIcon();
	}
	
	public Optional<ImageCursor> getCursor(SMPCursorType type) {
		return model.getCursor(type);
	}
	
}
