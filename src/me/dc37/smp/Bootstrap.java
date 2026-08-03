package me.dc37.smp;

import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import me.dc37.smp.views.SplashScreen;
import me.dc37.smp.views.SuperMarioPaintApplication;

@Slf4j
public class Bootstrap {

	public static void main(String[] args) {
		try {
            System.setProperty("javafx.preloader", SplashScreen.class.getName());
            Application.launch(SuperMarioPaintApplication.class, args);
        } catch (Exception e) {
            log.error("General error:", e);
        }
	}
	
}
