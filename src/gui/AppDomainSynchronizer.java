package gui;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;

public class AppDomainSynchronizer {

	private final AnimationTimer timer;
	private boolean running = false;
	
	public AppDomainSynchronizer(Runnable onFrame) {
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				onFrame.run();
			}
		};
	}
	
	public void start() {
		if (!Platform.isFxApplicationThread())
			throw new IllegalStateException("Cannot start playback sync outside JavaFX context!");
		
		if (!running) {
			timer.start();
			running = true;
		}
	}
	
	public void stop() {
		if (!Platform.isFxApplicationThread())
			throw new IllegalStateException("Cannot stop playback sync outside JavaFX context!");
		
		if (running) {
			timer.stop();
			running = false;
		}
	}
	
}
