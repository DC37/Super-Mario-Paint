package smp.fx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX2.2 has been released. Testing some application development.
 * It might take a little longer to do this, but it promises to be 
 * faster than working with swing... at least in the maintenance
 * aspect...
 * @author RehdBlob
 * @since 2012.08.16
 */
public class SuperMarioPaintFX extends Application {

	private static Parent root;
	
	@SuppressWarnings("deprecation")
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Super Mario Paint");
		primaryStage.setResizable(false);
		primaryStage.setScene(new Scene(root, 800, 600));
		primaryStage.show();
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		File file = new File("MainWindow.fxml");
		// Convert the file object to a URL
		URL url = null;
		url = file.toURL();          // file:/d:/almanac1.4/java.io/filename
		System.out.println(url);
		root = (Parent) FXMLLoader.load(url);
		System.err.println("FXML resource: " + SuperMarioPaintFX.class.getResource("file:/.MainWindow.fxml"));
	    FXMLLoader.load(url);
		launch(args);
	}
}
