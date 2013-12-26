package smp.fx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Generates a dialog box, depending on what we do.
 * @author RehdBlob
 * @since 2013.12.23
 *
 */
public class Dialog {

    /** A choice that we have made in some dialog box. */
    private static boolean choice = false;

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
     * @param primaryStage
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
            }

        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dialog.close();
                choice = false;
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

}
