package smp.components.controls;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import smp.components.Values;
import smp.components.general.ImagePushButton;

/**
 * This is the options button. It currently doesn't do anything.
 * @author RehdBlob
 * @since 2013.12.25
 */
public class OptionsButton extends ImagePushButton {

    /** This is the text that labels the slider. */
    private String txt = "Default Note Volume";

    /** A slider that changes the default volume of placed notes. */
    private Slider defaultVolume;

    /**
     * Default constructor.
     * @param i The image that we want to link this to.
     */
    public OptionsButton(ImageView i) {
        super(i);
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        options();
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        // do nothing.
    }

    /** Opens up an options dialog. */
    private void options() {
        final Stage dialog = new Stage();
        dialog.setHeight(150);
        dialog.setWidth(250);
        dialog.setResizable(false);
        dialog.setTitle("Options");
        dialog.initStyle(StageStyle.UTILITY);
        Label label = new Label(txt);
        defaultVolume = makeVolumeSlider();
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
        vBox.getChildren().addAll(label, defaultVolume, pane);
        defaultVolume.autosize();
        Scene scene1 = new Scene(vBox);
        dialog.setScene(scene1);
        dialog.showAndWait();
        while (dialog.isShowing()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // Do nothing
            }
        }
        updateValues();
    }

    /**
     * @return A slider that edits the default volume of the notes that
     * one places.
     */
    private Slider makeVolumeSlider() {
        Slider dV = new Slider();
        dV.setMax(Values.MAX_VELOCITY + 1);
        dV.setMin(0);
        dV.setValue(Values.DEFAULT_VELOCITY);
        dV.setShowTickMarks(true);
        dV.setShowTickLabels(true);
        dV.setSnapToTicks(true);
        dV.setMajorTickUnit(16);
        return dV;
    }

    /** Updates the different values of this program. */
    private void updateValues() {
        int vol = (int) defaultVolume.getValue();
        Values.DEFAULT_VELOCITY = vol >= 128 ? 127 : vol;
    }

}
