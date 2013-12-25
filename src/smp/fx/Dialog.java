package smp.fx;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Generates a dialog box, depending on what we do.
 * @author RehdBlob
 * @since 2013.12.23
 *
 */
public class Dialog {

    /**
     * Shows a dialog box with the text given to this method.
     * @param s The text to show.
     */
    public static void showDialog(String s) {
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        Scene scene = new Scene(new Group(new Text(50, 50, s)));
        dialog.setScene(scene);
        dialog.show();
    }

    /**
     * Shows an option dialog that asks you a yes/no question.
     * @param s The String to show.
     * @return A boolean based on your choice. Yes is true and
     * no is false.
     */
    public static boolean showYesNoDialog(String s) {
        // TODO Auto-generated method stub
        return false;
    }

}
