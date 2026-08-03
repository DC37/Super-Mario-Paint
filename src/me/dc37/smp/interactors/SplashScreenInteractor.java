package me.dc37.smp.interactors;

import java.util.Random;

import gui.Settings;
import me.dc37.smp.models.SplashScreenModel;

public class SplashScreenInteractor {

	/**
     * Random number generator (RNG) for icon selection.
     */
    private static final Random RNG = new Random();
    
    @SuppressWarnings("unused")
    private final SplashScreenModel model;
    
    public SplashScreenInteractor(SplashScreenModel model) {
    	this.model = model;
    }
    
    public String getTitle() {
    	double c = RNG.nextDouble();
    	String title;
    	
    	if (c >= 0.2 && c < 0.5) {
    		title = String.format("SMP%s", Settings.VERSION);
    	} else {
    		title = "Loading...";
    	}
    	
        return title;
    }
	
}
