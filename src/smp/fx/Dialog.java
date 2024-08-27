package smp.fx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * Generates a dialog box, depending on what we do.
 * @author RehdBlob
 * @since 2013.12.23
 */
public class Dialog {

    /** A choice that we have made in some dialog box. */
    private static boolean choice = false;

    /** Some text that we type into a field in a text dialog. */
    private static String info = "";
    
    private static Stage initDialogStage() {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        return stage;
    }
    
    private static Parent makeLayout(Node... elements) {
        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(elements);
        return layout;
    }
    
    private static Parent makeRow(Node... elements) {
        HBox layout = new HBox();
        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(elements);
        return layout;
    }

    /**
     * Shows a dialog box with the text given to this method.
     * @param txt The text to show.
     */
    public static void showDialog(String txt) {
        final Stage dialog = initDialogStage();
        
        Label label = new Label(txt);
        label.setTextAlignment(TextAlignment.CENTER);
        Button okButton = new Button("OK");

        okButton.setOnAction(event -> dialog.close());
        
        /* @since 1.4.1, ESC/Enter to close dialog */
        dialog.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.ESCAPE)
                dialog.close();
        });
        
        Parent buttons = makeRow(okButton);
        Parent layout = makeLayout(label, buttons);
        
        Scene scene = new Scene(layout);
        dialog.setScene(scene);
        dialog.showAndWait();

    }

    /**
     * Got this off of https://community.oracle.com/thread/2247058?tstart=0
     * Modified it to show an ok / cancel dialog.
     * @param txt The text to show.
     */
    public static boolean showYesNoDialog(String txt) {
        final Stage dialog = initDialogStage();
        
        Label label = new Label(txt);
        label.setTextAlignment(TextAlignment.CENTER);
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");

        okButton.setOnAction(event -> {
            dialog.close();
            choice = true;
        });

        cancelButton.setOnAction(event -> {
            dialog.close();
            choice = false;
        });
        
        /* @since 1.4.1, ESC to close dialog, Enter to accept */
        dialog.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
        	if (event.getCode() == KeyCode.ESCAPE) {
                choice = false;
                dialog.close();
        	} else if (event.getCode() == KeyCode.ENTER) {
                choice = true;
                dialog.close();
        	}
        });

        dialog.addEventHandler(WindowEvent.ANY, event -> {
            if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST
                    || event.getEventType() == WindowEvent.WINDOW_HIDDEN
                    || event.getEventType() == WindowEvent.WINDOW_HIDING)
                dialog.close();
                return;
        });
        
        Parent buttons = makeRow(okButton, cancelButton);
        Parent layout = makeLayout(label, buttons);
        
        Scene scene = new Scene(layout);
        dialog.setScene(scene);
        dialog.showAndWait();
        return choice;
    }

    /**
     * Got this off of https://community.oracle.com/thread/2247058?tstart=0
     * Modified it to show a text dialog.
     * @param txt The text to show.
     */
    public static String showTextDialog(String txt) {
        final Stage dialog = initDialogStage();
        
        Label label = new Label(txt);
        label.setTextAlignment(TextAlignment.CENTER);
        final TextField textfield = new TextField();
        textfield.setPrefColumnCount(8); // rather small; this is only for tempo afaik
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");

        okButton.setOnAction(event -> {
            info = textfield.getText();
            dialog.close();
        });

        cancelButton.setOnAction(event -> {
            info = "";
            dialog.close();
        });
        
        /* @since 1.4.1, ESC to close dialog, Enter to accept */
        dialog.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
        	if (event.getCode() == KeyCode.ESCAPE) {
                info = "";
                dialog.close();
        	} else if (event.getCode() == KeyCode.ENTER) {
                info = textfield.getText();
                dialog.close();
        	}
        });

        Parent buttons = makeRow(okButton, cancelButton);
        Parent layout = makeLayout(label, textfield, buttons);
        
        Scene scene = new Scene(layout);
        dialog.setScene(scene);
        dialog.showAndWait();
        return info;
    }

}
