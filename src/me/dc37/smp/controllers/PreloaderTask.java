package me.dc37.smp.controllers;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import backend.sound.SoundPlayer;
import gui.Utilities;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;
import gui.loaders.SMPCursorType;
import gui.loaders.SoundfontLoader;
import javafx.application.Preloader.PreloaderNotification;
import javafx.application.Preloader.ProgressNotification;
import javafx.concurrent.Task;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import me.dc37.smp.interactors.LoaderWorker;
import me.dc37.smp.models.ResourceModel;

public class PreloaderTask extends Task<Void> {
	
    private final ResourceModel model;
	private final LoaderWorker<Map<ImageIndex, Image>, ImageLoader> imageLoader;
	private final LoaderWorker<SoundPlayer, SoundfontLoader> soundfontLoader;
	private final Consumer<PreloaderNotification> fnNotifyPreloader;
	
	public PreloaderTask(
			ResourceModel model,
			LoaderWorker<Map<ImageIndex, Image>, ImageLoader> imageLoader,
			LoaderWorker<SoundPlayer, SoundfontLoader> soundfontLoader,
			Consumer<PreloaderNotification> fnNotifyPreloader) {
		
		this.model = model;
		this.imageLoader = imageLoader;
		this.soundfontLoader = soundfontLoader;
		this.fnNotifyPreloader = fnNotifyPreloader;
	}
	
	@Override
	protected Void call() throws Exception {
		longStart();
        return null;
	}
	
	/**
     * This should hopefully get something up on the screen quickly. This is
     * taken from http://docs.oracle.com/javafx/2/deployment/preloaders.htm
     */
    private void longStart() throws ExecutionException, InterruptedException {
    	executeLoaders();
    	
    	model.setIcons(imageLoader.getResult());
    	model.setSoundPlayer(soundfontLoader.getResult());
    	
    	addCursor(SMPCursorType.HAND_POINTING, ImageIndex.CURSOR_0);
    	addCursor(SMPCursorType.HAND_OPEN, ImageIndex.CURSOR_1);
    	addCursor(SMPCursorType.HAND_CLOSED, ImageIndex.CURSOR_2);
    	addCursor(SMPCursorType.ERASER, ImageIndex.CURSOR_3);
    	
    	fnNotifyPreloader.accept(new ProgressNotification(0.75));
    }
    
    private void executeLoaders() {
		imageLoader.start();
		soundfontLoader.start();
		
		do {
            Utilities.tryWait(1);
            
            double imgStatus = imageLoader.getLoadStatus();
            double sfStatus = soundfontLoader.getLoadStatus();
            double loaderAvg = (imgStatus + sfStatus) / 2.0;
            
            fnNotifyPreloader.accept(new ProgressNotification(loaderAvg));
        } while (imageLoader.isAlive() || soundfontLoader.isAlive());
	}
    
    private void addCursor(SMPCursorType type, ImageIndex imgIdx) {
    	Image img = model.getIcon(imgIdx).orElseThrow();
    	model.addCursor(type, new ImageCursor(img));
    }
	
}
