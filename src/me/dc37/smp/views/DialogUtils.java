package me.dc37.smp.views;

import java.util.Optional;

import gui.Values;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

public class DialogUtils {

    private DialogUtils() {}
    
    /**
     * Shows a dialog box with the title and text contents
     * given to this method.
     * 
     * @param title The title to show in the dialog.
     * @param body The text contents to present in the dialog.
     */
    public static void showInfo(String title, String body) {
        Alert dialog = new Alert(AlertType.INFORMATION);
        
        dialog.setTitle(title);
        dialog.setContentText(body);
        
        dialog.showAndWait();
    }
    
    /**
     * Shows a dialog box with the text contents
     * given to this method.
     * 
     * @param body The text contents to present in the dialog.
     */
    public static void showInfo(String body) {
        showInfo(Values.PROGRAM_NAME, body);
    }
    
    /**
     * Shows a dialog question with the title and text contents
     * given to this method.
     * 
     * @param title The title to show in the dialog.
     * @param body The text contents to present in the dialog.
     * @return A boolean indicating if the response was affirmative or not.
     */
    public static boolean showQuestion(String title, String body) {
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        
        dialog.setTitle(title);
        dialog.setContentText(body);
        
        Optional<ButtonType> result = dialog.showAndWait();
        
        if (result.isPresent()) {
            return result.get().getButtonData().isDefaultButton();
        } else {
            return false;
        }
    }
    
    /**
     * Shows a dialog question with the text contents
     * given to this method.
     * 
     * @param body The text contents to present in the dialog.
     * @return A boolean indicating if the response was affirmative or not.
     */
    public static boolean showQuestion(String body) {
        return showQuestion(Values.PROGRAM_NAME, body);
    }
    
    /**
     * Shows a dialog requesting the user to input some text,
     * with the specified title, body, and placeholder.
     * 
     * @param title The title to show in the dialog.
     * @param body The text contents to present in the dialog.
     * @param placeholder The initial hint displayed by the {@link TextField}.
     * @return A string representing the input given by the user.
     */
    public static String showInput(String title, String body, String placeholder) {
        TextInputDialog dialog = new TextInputDialog("");
        
        dialog.setTitle(title);
        dialog.setContentText(body);
        
        if (placeholder != null && !"".equals(placeholder))
            dialog.getEditor().setPromptText(placeholder);
        
        return dialog.showAndWait().orElse("");
    }
    
    /**
     * Shows a dialog requesting the user to input some text,
     * with the specified title and body.
     * 
     * @param title The title to show in the dialog.
     * @param body The text contents to present in the dialog.
     * @return A string representing the input given by the user.
     */
    public static String showInput(String title, String body) {
        return showInput(title, body, null);
    }
    
    /**
     * Shows a dialog requesting the user to input some text,
     * with the specified body.
     * 
     * @param body The text contents to present in the dialog.
     * @return A string representing the input given by the user.
     */
    public static String showInput(String body) {
        return showInput(Values.PROGRAM_NAME, body);
    }
    
}
