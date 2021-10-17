package smp.fx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
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

    /**
     * Shows a dialog box with the text given to this method.
     * @param txt The text to show.
     */
    public static void showDialog(String txt) {
        final Stage dialog = new Stage();
        dialog.setHeight(150);
        dialog.setWidth(250);
        dialog.setResizable(false);
        dialog.initStyle(StageStyle.UTILITY);
        Label label = new Label(txt);
        Button okButton = new Button("OK");

        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }

        });

        FlowPane pane = new FlowPane(10, 10);
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(okButton);
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(label, pane);
        Scene scene1 = new Scene(vBox);
        dialog.setScene(scene1);
        dialog.showAndWait();

    }

    /**
     * Got this off of https://community.oracle.com/thread/2247058?tstart=0
     * Modified it to show an ok / cancel dialog.
     * @param txt The text to show.
     */
    public static boolean showYesNoDialog(String txt) {
        final Stage dialog = new Stage();
        dialog.setHeight(150);
        dialog.setWidth(250);
        dialog.setResizable(false);
        dialog.initStyle(StageStyle.UTILITY);
        Label label = new Label(txt);
        Button okButton = new Button("OK");

        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dialog.close();
                choice = true;
                event.consume();
            }

        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dialog.close();
                choice = false;
                event.consume();
            }
        });
        
        // @since 1.4.1, ESC to close dialog, Enter to accept
        dialog.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
            	if (ke.getCode() == KeyCode.ESCAPE) {
                    choice = false;
                    dialog.close();
                    ke.consume();
            	} else if (ke.getCode() == KeyCode.ENTER) {
                    choice = true;
                    dialog.close();
                    ke.consume();
            	}
            }
        });

        dialog.addEventHandler(WindowEvent.ANY, new EventHandler<WindowEvent>()  {
            @Override
            public void handle(WindowEvent we) {
                if (we.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST
                        || we.getEventType() == WindowEvent.WINDOW_HIDDEN
                        || we.getEventType() == WindowEvent.WINDOW_HIDING)
                    dialog.close();
                    return;
            }
        });
        
        FlowPane pane = new FlowPane(10, 10);
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(okButton, cancelButton);
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(label, pane);
        Scene scene1 = new Scene(vBox);
        dialog.setScene(scene1);
        dialog.showAndWait();
        return choice;
    }

    /**
     * Got this off of https://community.oracle.com/thread/2247058?tstart=0
     * Modified it to show a text dialog.
     * @param txt The text to show.
     */
    public static String showTextDialog(String txt) {
        final Stage dialog = new Stage();
        dialog.setHeight(125);
        dialog.setWidth(150);
        dialog.setResizable(false);
        dialog.initStyle(StageStyle.UTILITY);
        Label label = new Label(txt);
        final TextField t = new TextField();
        Button okButton = new Button("OK");

        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                info = t.getText();
                dialog.close();
            }

        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                info = "";
                dialog.close();
                event.consume();
            }
        });
        
        // @since 1.4.1, ESC to close dialog, Enter to accept
        dialog.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
            	if (ke.getCode() == KeyCode.ESCAPE) {
                    info = "";
                    dialog.close();
                    ke.consume();
            	} else if (ke.getCode() == KeyCode.ENTER) {
                    info = t.getText();
                    dialog.close();
                    ke.consume();
            	}
            }
        });

        FlowPane pane = new FlowPane(10, 10);
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(okButton, cancelButton);
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(label, t, pane);
        Scene scene1 = new Scene(vBox);
        dialog.setScene(scene1);
        dialog.showAndWait();
        return info;
    }

}
