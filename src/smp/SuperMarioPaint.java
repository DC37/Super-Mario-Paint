package smp;

import java.io.File;

import com.sun.javafx.application.LauncherImpl;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import smp.components.Values;
import smp.fx.SMPFXController;
import smp.fx.SplashScreen;
import smp.stateMachine.Settings;
import smp.stateMachine.StateMachine;

/**
 * Super Mario Paint <br>
 * Based on the old SNES game from 1992, Mario Paint <br>
 * Inspired by:<br>
 * MarioSequencer (2002) <br>
 * TrioSequencer <br>
 * Robby Mulvany's Mario Paint Composer 1.0 / 2.0 (2007-2008) <br>
 * FordPrefect's Advanced Mario Sequencer (2009) <br>
 * The GUI is primarily written with JavaFX <br>
 *
 * @author RehdBlob
 * @since 2012.08.16
 * @version 1.0.5
 */
public class SuperMarioPaint extends Application {

    /**
     * Location of the Main Window fxml file.
     */
    private String mainFxml = "./MainWindow.fxml";

    /**
     * Location of the Advanced Mode (super secret!!) fxml file.
     */
    @SuppressWarnings("unused")
    private String advFxml = "./AdvWindow.fxml";

    /**
     * The number of threads that are running to load things for Super Mario
     * Paint.
     */
    private static final int NUM_THREADS = 2;

    /**
     * Loads all the sprites that will be used in Super Mario Paint.
     */
    private Loader imgLoader = new ImageLoader();

    /**
     * Loads the soundfonts that will be used in Super Mario Paint.
     */
    private Loader sfLoader = new SoundfontLoader();

    /** Image Loader thread. */
    private Thread imgLd;

    /** Soundfont loader thread. */
    private Thread sfLd;

    /** This is the main application stage. */
    private Stage primaryStage;

    /** This is the primary Scene on the main application stage. */
    private Scene primaryScene;

    /** This is the loaded FXML file. */
    private Parent root;

    /**
     * The controller class for the FXML.
     */
    private SMPFXController controller = new SMPFXController();

    /** Whether we are done loading the application or not. */
    private BooleanProperty ready = new SimpleBooleanProperty(false);

    /**
     * This should hopefully get something up on the screen quickly. This is
     * taken from http://docs.oracle.com/javafx/2/deployment/preloaders.htm
     */
    private void longStart() {

        // long init in background
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                sfLd.start();
                imgLd.start();
                do {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    double imgStatus = imgLoader.getLoadStatus();
                    double sfStatus = sfLoader.getLoadStatus();
                    double ld = (imgStatus + sfStatus) * 100 / NUM_THREADS
                            * 0.5;
                    notifyPreloader(new ProgressNotification(ld));
                } while (imgLd.isAlive() || sfLd.isAlive());
                FXMLLoader loader = new FXMLLoader();
                loader.setController(controller);
                loader.setLocation(new File(mainFxml).toURI().toURL());
                root = (Parent) loader.load();
                notifyPreloader(new ProgressNotification(0.75));
                ready.setValue(Boolean.TRUE);
                return null;
            }
        };
        new Thread(task).start();
    }

    /** Explicitly create constructor without arguments. */
    public SuperMarioPaint() {

    }

    /**
     * Starts three <code>Thread</code>s. One of them is currently a dummy
     * splash screen, the second an <code>ImageLoader</code>, and the third one
     * a <code>SoundfontLoader</code>.
     *
     * @see ImageLoader
     * @see SoundfontLoader
     * @see SplashScreen
     */
    @Override
    public void init() {
        imgLd = new Thread(imgLoader);
        sfLd = new Thread(sfLoader);
        controller.setImageLoader((ImageLoader) imgLoader);
    }

    /**
     * Starts the application and loads the FXML file that contains a lot of the
     * class hierarchy.
     *
     * @param primaryStage
     *            The primary stage that will be showing the main window of
     *            Super Mario Paint.
     */
    @Override
    public void start(Stage ps) {
        primaryStage = ps;

        longStart();

        ready.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean t, Boolean t1) {
                if (Boolean.TRUE.equals(t1)) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                primaryStage.setTitle("Super Mario Paint " + Settings.version);
                                setupCloseBehaviour(primaryStage);
                                primaryStage.setResizable(false);
                                primaryStage.setHeight(Values.DEFAULT_HEIGHT);
                                primaryStage.setWidth(Values.DEFAULT_WIDTH);
                                primaryScene = new Scene(root, 800, 600);
                                primaryStage.setScene(primaryScene);
                                makeKeyboardListeners(primaryScene);
                                notifyPreloader(new ProgressNotification(1));
                                notifyPreloader(new StateChangeNotification(
                                        StateChangeNotification.Type.BEFORE_START));
                                primaryStage.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.exit(1);
                            }
                        }
                    });
                }
            }
        });
        ;
    }

    /**
     * Protip for JavaFX users: Make sure you define your application close
     * behavior.
     */
    @Override
    public void stop() {
        System.exit(0);
    }

    /**
     * Got this off of https://community.oracle.com/thread/2247058?tstart=0 This
     * appears quite useful as a 'really exit?' type thing. This dialog
     * currently needs some work, so we're not going to include it in the alpha
     * release.
     *
     * @param primaryStage
     *            The main stage of interest.
     */
    private void setupCloseBehaviour(final Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                if (StateMachine.isSongModified()
                        || StateMachine.isArrModified()) {
                    final Stage dialog = new Stage();
                    dialog.setHeight(100);
                    dialog.setWidth(300);
                    dialog.setResizable(false);
                    dialog.initStyle(StageStyle.UTILITY);
                    Label label = new Label();
                    label.setMaxWidth(300);
                    label.setWrapText(true);
                    if (StateMachine.isSongModified()
                            && StateMachine.isArrModified()) {
                        label.setText("The song and arrangement have\n"
                                + "both not been saved! Really exit?");
                    } else if (StateMachine.isSongModified()) {
                        label.setText("The song has not been saved! "
                                + "Really exit?");
                    } else if (StateMachine.isArrModified()) {
                        label.setText("The arrangement has not been saved! "
                                + "Really exit?");
                    }
                    Button okButton = new Button("Yes");
                    okButton.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            dialog.close();
                            stop();

                        }
                    });
                    Button cancelButton = new Button("No");
                    cancelButton.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            dialog.close();
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
                    dialog.show();
                } else {
                    stop();
                }
                event.consume();
            }
        });
    }

    /**
     * Creates the keyboard listeners that we will be using for various other
     * portions of the program. Ctrl, alt, and shift are of interest here, but
     * the arrow keys will also be considered.
     *
     * @param primaryScene
     *            The main window.
     */
    private void makeKeyboardListeners(Scene primaryScene) {
        primaryScene.addEventHandler(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {

                    @Override
                    public void handle(KeyEvent ke) {
                        StateMachine.getButtonsPressed().add(ke.getCode());
                        StateMachine.updateFocusPane();
                        ke.consume();
                    }
                });

        primaryScene.addEventHandler(KeyEvent.KEY_RELEASED,
                new EventHandler<KeyEvent>() {

                    @Override
                    public void handle(KeyEvent ke) {
                        StateMachine.getButtonsPressed().remove(ke.getCode());
                        StateMachine.updateFocusPane();
                        ke.consume();
                    }
                });

        primaryStage.focusedProperty().addListener(
                new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> ov,
                            Boolean t, Boolean t1) {
                        StateMachine.clearKeyPresses();
                        StateMachine.updateFocusPane();
                    }
                });

    }

    /**
     * Launches the application.
     *
     * @param args
     *            Sets debug options on or off.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            try {
                LauncherImpl.launchApplication(SuperMarioPaint.class,
                        SplashScreen.class, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (args.length > 0
                && (args[0].equals("--debug") || args[0].equals("--d"))) {
            if (args[1] != null) {
                try {
                    Settings.setDebug(Integer.parseInt(args[1]));
                } catch (NumberFormatException e) {
                    Settings.setDebug(1);
                }
            } else {
                Settings.setDebug(1);
            }
            try {
                LauncherImpl.launchApplication(SuperMarioPaint.class,
                        SplashScreen.class, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
