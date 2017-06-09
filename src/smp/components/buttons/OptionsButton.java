package smp.components.buttons;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import smp.ImageLoader;
import smp.commandmanager.commands.MultiplyTempoCommand;
import smp.components.Values;
import smp.components.general.ImagePushButton;
import smp.components.staff.sequences.StaffNoteLine;
import smp.fx.SMPFXController;
import smp.stateMachine.ProgramState;
import smp.stateMachine.StateMachine;

/**
 * This is the options button. It currently doesn't do anything.
 *
 * @author RehdBlob
 * @since 2013.12.25
 */
public class OptionsButton extends ImagePushButton {

    /** This is the text that labels the slider. */
    private String txt = "Default Note Volume";

    /** This is the place where we type in the amount to adjust tempo by. */
    private TextField tempoField;

    /** A slider that changes the default volume of placed notes. */
    private Slider defaultVolume;
    
    /**
     * Default constructor.
     *
     * @param i
     *            The image that we want to link this to.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public OptionsButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState();
        if (curr == ProgramState.EDITING)
            options();
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        // do nothing.
    }

    /** Opens up an options dialog. */
    private void options() {
        final Stage dialog = new Stage();
        initializeDialog(dialog);
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
        Label tempoAdjustHack = new Label("Increase tempo by how many times?");
        tempoField = new TextField();
        
        vBox.getChildren().addAll(label, defaultVolume, tempoAdjustHack,
                tempoField, pane);
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
        theStaff.redraw();
    }

    /**
     * Sets the height, width, and other basic properties of this dialog box.
     *
     * @param dialog
     *            The dialog box that we are setting up.
     */
    private void initializeDialog(Stage dialog) {
        dialog.setHeight(250);
        dialog.setWidth(250);
        dialog.setResizable(false);
        dialog.setTitle("Options");
        dialog.initStyle(StageStyle.UTILITY);
    }

    /**
     * @return A slider that edits the default volume of the notes that one
     *         places.
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
        changeDefaultVol();
        multiplyTempo();
    }

    /** Updates the default volume of the program notes. */
    private void changeDefaultVol() {
        int vol = (int) defaultVolume.getValue();
        Values.DEFAULT_VELOCITY = vol >= 128 ? 127 : vol;
    }

    /** Updates the tempo from the options dialog. */
    private void multiplyTempo() {
        String txt = tempoField.getText();
        if (txt != null) {
            int num = 1;
            try {
                num = Integer.parseInt(txt);
            } catch (NumberFormatException e) {
                return;
            }
            if (num <= 1)
                return;
            ArrayList<StaffNoteLine> s = theStaff.getSequence().getTheLines();
            ArrayList<StaffNoteLine> n = new ArrayList<StaffNoteLine>();
            for (int i = 0; i < s.size(); i++) {
                n.add(s.get(i));
                for (int j = 0; j < num - 1; j++)
                    n.add(new StaffNoteLine());
            }
            s.clear();
            s.addAll(n);
            StateMachine.setTempo(theStaff.getSequence().getTempo() * num);
            theStaff.updateCurrTempo();
            theStaff.getControlPanel().getScrollbar()
                    .setMax(s.size() - Values.NOTELINES_IN_THE_WINDOW);
            
            controller.getModifySongManager().execute(new MultiplyTempoCommand(theStaff, num));
            controller.getModifySongManager().record();
        }
    }
}
