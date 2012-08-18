package smp.fx;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
	
	private String fxml = "./MainWindow.fxml";
	
	/**
	 * Starts the application and loads the FXML file that contains
	 * a lot of the class hierarchy.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = 
				(Parent) FXMLLoader.load(new File(fxml).toURI().toURL());
			primaryStage.setTitle("Super Mario Paint");
			primaryStage.setResizable(false);
			primaryStage.setScene(new Scene(root, 800, 600));
			primaryStage.show();
		} catch (MalformedURLException e) {
			// Can't recover.
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			// Can't recover.
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Launches the application.
	 * @param args String args.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
