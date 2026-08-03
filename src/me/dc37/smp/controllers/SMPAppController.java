package me.dc37.smp.controllers;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import backend.sound.SoundPlayer;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;
import gui.loaders.SMPCursorType;
import gui.loaders.SoundfontLoader;
import javafx.application.Preloader.ErrorNotification;
import javafx.application.Preloader.PreloaderNotification;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import me.dc37.smp.interactors.LoaderWorker;
import me.dc37.smp.interactors.SMPAppInteractor;
import me.dc37.smp.models.ResourceModel;
import me.dc37.smp.models.SMPAppModel;
import me.dc37.smp.views.SMPAppViewBuilder;

@Slf4j
public class SMPAppController {

    private final SMPAppModel model;
    private final ResourceModel resModel;
	
	@SuppressWarnings("unused")
	private final SMPAppInteractor interactor;
	
	private final SMPAppViewBuilder viewBuilder;
	
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
		resModel = ResourceModel.getInstance();
		
		interactor = new SMPAppInteractor(model);
		viewBuilder = new SMPAppViewBuilder(resModel, model);
	}
	
	public void prepareLoaders() {
		imageLoader = new LoaderWorker<>(new ImageLoader());
		soundfontLoader = new LoaderWorker<>(new SoundfontLoader());
	}
	
	public void triggerLoad(Stage stage,
	        Consumer<PreloaderNotification> fnNotifyPreloader,
	        BiConsumer<Stage, SMPAppModel> fnPrepareView) {
	    
		preloaderTask = new PreloaderTask(
				resModel, imageLoader, soundfontLoader, fnNotifyPreloader::accept);
		
		preloaderTask.setOnSucceeded(event -> fnPrepareView.accept(stage, model));
        preloaderTask.setOnFailed(event -> manageLoadFailure(fnNotifyPreloader));
        
        new Thread(preloaderTask).start();
	}
	
	private void manageLoadFailure(Consumer<PreloaderNotification> fnNotifyPreloader) {
        if (preloaderTask == null) {
            return;
        }
        
        fnNotifyPreloader.accept(new ErrorNotification("Unknown", "Unknown", preloaderTask.getException()));
    }
	
	public Region getView() throws IllegalStateException {
	    return viewBuilder.build();
	}
	
	public <T> T getFxController() {
	    return viewBuilder.getFxController();
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
