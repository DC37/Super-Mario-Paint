package me.dc37.smp.views;

import java.net.URL;

import gui.Values;
import gui.resources.SMPResourceType;
import gui.resources.SMPResourceUtil;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import me.dc37.smp.models.SplashScreenModel;

public class SplashScreenViewBuilder implements Builder<Region> {

	private final SplashScreenModel model;
	
	public SplashScreenViewBuilder(SplashScreenModel model) {
		this.model = model;
	}
	
	/**
     * @return The created scene.
     */
	@Override
	public Region build() {
		BorderPane p = new BorderPane();
		p.setCenter(createBoundProgressBar(model.getProgressProperty()));
        
        ImageView imageview = new ImageView();
        
        /* @since 1.4, to spice up the load screen. why not? - seymour */
        URL loadingGif = SMPResourceUtil.get("LOADING_ANIM.gif", SMPResourceType.UI, Values.SPRITES_FOLDER);
        imageview.setImage(new Image(loadingGif.toString()));
        
        imageview.setFitWidth(236);
        imageview.setFitHeight(36);
        imageview.setTranslateX(32);
        imageview.setTranslateY(32);
        
        p.setTop(imageview);
        
        return p;
	}
	
	private Node createBoundProgressBar(DoubleProperty boundProperty) {
		ProgressBar bar = new ProgressBar();
		bar.progressProperty().bind(boundProperty);
		return bar;
	}

}
