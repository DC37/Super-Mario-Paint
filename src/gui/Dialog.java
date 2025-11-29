package gui;

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
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 * Generates a dialog box, depending on what we do.
 * @author RehdBlob
 * @since 2013.12.23
 */
public class Dialog {
    
    private static <T> StageWithReturn<T> initDialogStage(Window owner) {
        StageWithReturn<T> stage = new StageWithReturn<T>();
        stage.setResizable(false);
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        // setting this style seems to be blocking everything
        // commenting out until someone finds why --rozlyn
//        stage.initStyle(StageStyle.UTILITY);
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
    public static void showDialog(String txt, Window owner) {
        final StageWithReturn<Void> dialog = initDialogStage(owner);
        
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
        dialog.showAndReturn(null);

    }
    
    public static void showDialog(String txt) {
        showDialog(txt, null);
    }

    /**
     * Got this off of https://community.oracle.com/thread/2247058?tstart=0
     * Modified it to show an ok / cancel dialog.
     * @param txt The text to show.
     */
    public static boolean showYesNoDialog(String txt, Window owner) {
        final StageWithReturn<Boolean> dialog = initDialogStage(owner);
        
        Label label = new Label(txt);
        label.setTextAlignment(TextAlignment.CENTER);
        Button okButton = new Button("Yes");
        Button cancelButton = new Button("No");

        okButton.setOnAction(event -> {
            dialog.returnValue = true;
            dialog.close();
        });

        cancelButton.setOnAction(event -> {
            dialog.returnValue = false;
            dialog.close();
        });
        
        /* @since 1.4.1, ESC to close dialog, Enter to accept */
        dialog.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
        	if (event.getCode() == KeyCode.ESCAPE) {
        	    dialog.returnValue = false;
                dialog.close();
        	} else if (event.getCode() == KeyCode.ENTER) {
                dialog.returnValue = true;
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
        return dialog.showAndReturn(false);
    }
    
    public static boolean showYesNoDialog(String txt) {
        return showYesNoDialog(txt, null);
    }
    
    public static String showTextDialog(String txt, Window owner) {
    	return showTextDialog(txt, null, owner);
    }

    /**
     * Got this off of https://community.oracle.com/thread/2247058?tstart=0
     * Modified it to show a text dialog.
     * @param txt The text to show.
     */
    public static String showTextDialog(String txt, String prompt, Window owner) {
        final StageWithReturn<String> dialog = initDialogStage(owner);
        
        Label label = new Label(txt);
        label.setTextAlignment(TextAlignment.CENTER);
        final TextField textfield = new TextField();
        textfield.setPrefColumnCount(12); // rather small; this is only for tempo afaik
        if (prompt != null) {
        	textfield.setPromptText(prompt);
        }
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");

        okButton.setOnAction(event -> {
            dialog.returnValue = textfield.getText();
            dialog.close();
        });

        cancelButton.setOnAction(event -> {
            dialog.returnValue = "";
            dialog.close();
        });
        
        /* @since 1.4.1, ESC to close dialog, Enter to accept */
        dialog.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
        	if (event.getCode() == KeyCode.ESCAPE) {
        	    dialog.returnValue = "";
                dialog.close();
        	} else if (event.getCode() == KeyCode.ENTER) {
                dialog.returnValue = textfield.getText();
                dialog.close();
        	}
        });

        Parent buttons = makeRow(okButton, cancelButton);
        Parent layout = makeLayout(label, textfield, buttons);
        
        Scene scene = new Scene(layout);
        dialog.setScene(scene);
        okButton.requestFocus(); // little trick to always display prompt
        return dialog.showAndReturn("");
    }
    
    public static String showTextDialog(String txt) {
        return showTextDialog(txt, null);
    }

}
