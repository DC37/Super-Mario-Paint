package me.dc37.smp.views;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

import gui.Values;
import gui.resources.FetchStrategy;
import gui.resources.SMPResourceType;
import gui.resources.SMPResourceUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import me.dc37.smp.domain.SaveSongParameters;
import me.dc37.smp.models.ResourceModel;
import me.dc37.smp.models.SMPAppModel;

public class SMPAppViewBuilder implements Builder<Region> {

    private final FXMLLoader loader = new FXMLLoader();
    
    private final ResourceModel resModel;
    private final SMPAppModel model;
    private final Consumer<SaveSongParameters> fnSaveSong;
    
    public SMPAppViewBuilder(
            ResourceModel resModel, SMPAppModel model,
            Consumer<SaveSongParameters> fnSaveSong) {
        
        this.resModel = resModel;
        this.model = model;
        this.fnSaveSong = fnSaveSong;
    }
    
    @Override
    public Region build() {
        SMPAppViewFXController fxCtrl = new SMPAppViewFXController(resModel, model, fnSaveSong);
        loader.setController(fxCtrl);
        
        // We have to copy the FXML onto the user file system
        // because it expects a "sprites" folder.
        // But also, we may update it regularly as we develop,
        // so we want to always use our internal version.
        URL fxml = SMPResourceUtil.get(Values.FXML, SMPResourceType.UI,
                FetchStrategy.COPY_INTERNAL, Values.SMP_FOLDER);
        loader.setLocation(fxml);
        
        try {
            return loader.load();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public <T> T getFxController() {
        return loader.getController();
    }
    
}
