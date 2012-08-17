package smp.fx;

import java.io.File;
import java.io.FileInputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
    	File f = new File("MainWindow.fxml");
    	FileInputStream fI = new FileInputStream(f);
    	FXMLLoader fL = new FXMLLoader();
    	fL.setLocation(getClass().getResource("."));
        Parent root = (Parent) fL.load(fI);
        
        stage.setTitle("FXML Welcome");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }
}