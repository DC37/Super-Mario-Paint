package me.dc37.smp.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

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
import javafx.application.Preloader.PreloaderNotification;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import me.dc37.smp.interactors.LoaderWorker;
import me.dc37.smp.interactors.SMPAppInteractor;
import me.dc37.smp.models.ResourceModel;
import me.dc37.smp.models.SMPAppModel;
import me.dc37.smp.views.SMPAppViewFXController;

@Slf4j
public class SMPAppController {

    private final SMPAppModel model;
	
	@SuppressWarnings("unused")
	private final SMPAppInteractor interactor;
	
	private final ResourceModel resModel;
	
	private PreloaderTask preloaderTask;
	
	/**
     * Loads all the sprites that will be used in Super Mario Paint.
     */
	private LoaderWorker<Map<ImageIndex, Image>, ImageLoader> imageLoader;
	
	/**
     * Loads the soundfonts that will be used in Super Mario Paint.
     */
	private LoaderWorker<SoundPlayer, SoundfontLoader> soundfontLoader;
	
	public SMPAppController() {
		model = SMPAppModel.getInstance();
		interactor = new SMPAppInteractor(model);
		
		resModel = ResourceModel.getInstance();
	}
	
	public void prepareLoaders() {
		imageLoader = new LoaderWorker<>(new ImageLoader());
		soundfontLoader = new LoaderWorker<>(new SoundfontLoader());
	}
	
	public void triggerLoad(Stage stage,
	        Consumer<PreloaderNotification> fnNotifyPreloader,
	        Consumer<Stage> fnPrepareView) {
	    
		preloaderTask = new PreloaderTask(
				resModel, imageLoader, soundfontLoader, fnNotifyPreloader::accept);
		
		preloaderTask.setOnSucceeded(event -> fnPrepareView.accept(stage));
        preloaderTask.setOnFailed(event -> manageLoadFailure(fnNotifyPreloader));
        
        new Thread(preloaderTask).start();
	}
	
	private void manageLoadFailure(Consumer<PreloaderNotification> fnNotifyPreloader) {
        if (preloaderTask == null) {
            return;
        }
        
        fnNotifyPreloader.accept(new ErrorNotification("Unknown", "Unknown", preloaderTask.getException()));
    }
	
	public Region getView(FXMLLoader loader) throws IOException {
	    SMPAppViewFXController fxCtrl = new SMPAppViewFXController(resModel, model);
	    loader.setController(fxCtrl);
		
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
		return resModel.getIcons();
	}
	
	public Optional<Image> getIcon(ImageIndex imgIdx) {
		return resModel.getIcon(imgIdx);
	}
	
	public SoundPlayer getSoundPlayer() {
		return resModel.getSoundPlayer();
	}
	
	public Image getHeaderIcon() {
		return resModel.getHeaderIcon();
	}
	
	public Optional<ImageCursor> getCursor(SMPCursorType type) {
		return resModel.getCursor(type);
	}
	
}
