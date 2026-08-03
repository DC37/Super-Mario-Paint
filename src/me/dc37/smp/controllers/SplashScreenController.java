package me.dc37.smp.controllers;

import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import me.dc37.smp.interactors.ResourceInteractor;
import me.dc37.smp.interactors.SplashScreenInteractor;
import me.dc37.smp.models.ResourceModel;
import me.dc37.smp.models.SplashScreenModel;
import me.dc37.smp.views.SplashScreenViewBuilder;

public class SplashScreenController {

    private final ResourceModel resModel;
    private final ResourceInteractor resInteractor;
    
	private final SplashScreenModel model;
	private final SplashScreenInteractor interactor;
	private final SplashScreenViewBuilder viewBuilder;
	
	public SplashScreenController() {
	    resModel = ResourceModel.getInstance();
	    resInteractor = new ResourceInteractor(resModel);
	    
		model = new SplashScreenModel();
		interactor = new SplashScreenInteractor(model);
		
		viewBuilder = new SplashScreenViewBuilder(model);
	}
	
	public Region getView() {
		return viewBuilder.build();
	}
	
	public Image getHeaderIcon() {
		resInteractor.setHeaderIcon();
		return resModel.getHeaderIcon();
	}
	
	public String getTitle() {
		return interactor.getTitle();
	}
	
	public void setProgress(double progress) {
		model.setProgress(progress);
	}
	
	public boolean isNoLoadingProgress() {
		return model.isNoLoadingProgress();
	}
	
	public void setNoLoadingProgress(boolean isNoLoadingProgress) {
		model.setNoLoadingProgress(isNoLoadingProgress);
	}
	
}
