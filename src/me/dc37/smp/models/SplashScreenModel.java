package me.dc37.smp.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class SplashScreenModel {

	private final DoubleProperty progress = new SimpleDoubleProperty(0);
	private final BooleanProperty noLoadingProgress = new SimpleBooleanProperty(true);
	
	public double getProgress() {
		return progress.get();
	}
	
	public DoubleProperty getProgressProperty() {
		return progress;
	}
	
	public void setProgress(double progress) {
		this.progress.set(progress);
	}
	
	public boolean isNoLoadingProgress() {
		return noLoadingProgress.get();
	}
	
	public BooleanProperty getNoLoadingProgressProperty() {
		return noLoadingProgress;
	}
	
	public void setNoLoadingProgress(boolean noLoadingProgress) {
		this.noLoadingProgress.set(noLoadingProgress);
	}
	
}
