package smp;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;


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
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import smp.components.Values;
import smp.components.InstrumentIndex;
import smp.components.staff.StaffInstrumentEventHandler;
import smp.fx.SMPFXController;
import smp.fx.SplashScreen;
import smp.stateMachine.ProgramState;
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
 * Dev team: 
 * RehdBlob (2012 - current)
 * j574y923 (2017 - current)
 * CyanSMP64 (2019 - current)
 * seymour (2020 - current)
 * rozlynd (2024 - current)
 * 
 * @author RehdBlob
 * @author j574y923
 * @author CyanSMP64
 * @author seymour
 * @author rozlynd
 * 
 * @since 2012.08.16
 * @version 1.3.2
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
        controller.setSoundfontLoader((SoundfontLoader) sfLoader);
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
                                primaryScene = new Scene(root, 1024, 768);
                                primaryStage.setScene(primaryScene);
                                makeKeyboardListeners(primaryScene);
                                notifyPreloader(new ProgressNotification(1));
                                notifyPreloader(new StateChangeNotification(
                                        StateChangeNotification.Type.BEFORE_START));
                                
                                /* @since 2020.4.28 - seymour
                                 * Changes the cursor image */
                                setCursor(0);
                                
                                /* Changes the app icon 
                                 * (gives the option to use a given icon OR a random icon from all instruments) */
                                if (new File("./sprites/ICON.png").exists())
                                	setIcon("./sprites/ICON.png");
                                else {
	                                int randNum = (int) Math.floor(Math.random() * Values.NUMINSTRUMENTS);
	                                ArrayList<InstrumentIndex> instList = new ArrayList<InstrumentIndex>(
	                                		EnumSet.allOf(InstrumentIndex.class));
	                                String instName = instList.get(randNum).name();
	                                setIcon("./sprites/" + instName + "_SM.png");
                                }
                                
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
//    	Platform.exit();
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
    	
    	primaryScene.addEventHandler(MouseEvent.ANY, controller.getStaffInstrumentEventHandler());
    	
    	ArrayList<MouseButton> mouseButtons = new ArrayList<MouseButton>();
    	
    	// Just a temporary thing to change mouse until i (or someone else) can find out where to put it =P -- seymour
    	primaryScene.addEventHandler(MouseEvent.MOUSE_PRESSED,
    			new EventHandler<MouseEvent>() {
    				
    				@Override
    				public void handle(MouseEvent m) {
    					if (!mouseButtons.contains(m.getButton()))
    						mouseButtons.add(m.getButton());
    					
    					if (mouseButtons.contains(MouseButton.MIDDLE) || (StateMachine.isClipboardPressed() && m.getButton() != MouseButton.SECONDARY))
		    				setCursor(2);
		    			else if (mouseButtons.contains(MouseButton.PRIMARY))
		    				setCursor(1);
		    			else if (mouseButtons.contains(MouseButton.SECONDARY) && !StateMachine.isClipboardPressed())
		    				setCursor(3);
    					
    					m.consume();
    				}
    	});
    	primaryScene.addEventHandler(MouseEvent.MOUSE_RELEASED,
    			new EventHandler<MouseEvent>() {
    		
		    		@Override
					public void handle(MouseEvent m) {
		    			// Added to remove the default cursor appearing while other mouse buttons are held
		    			mouseButtons.remove(m.getButton());
		    			
		    			if (mouseButtons.contains(MouseButton.MIDDLE) || (StateMachine.isClipboardPressed() && m.getButton() != MouseButton.SECONDARY))
		    				setCursor(2);
		    			else if (mouseButtons.contains(MouseButton.PRIMARY))
		    				setCursor(1);
		    			else if (mouseButtons.contains(MouseButton.SECONDARY) && !StateMachine.isClipboardPressed())
		    				setCursor(3);
		    			
		    			if (mouseButtons.isEmpty())
							setCursor(0);
						
						m.consume();
					}
    	});
    	
    	// TODO: move to its own keyhandler
        primaryScene.addEventHandler(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {

                    @Override
                    public void handle(KeyEvent ke) {
                    	
                    	switch(ke.getCode()) {
                    	case PAGE_UP:
                    		controller.getStaff().shift(-Values.NOTELINES_IN_THE_WINDOW);
                    		break;
                    	case PAGE_DOWN:
                    		controller.getStaff().shift(Values.NOTELINES_IN_THE_WINDOW);
                    		break;
                    	case HOME:
                    		if(ke.isControlDown())
                    			controller.getStaff().setLocation(0);
                    		break;
                    	case END:
                    		if(ke.isControlDown())
                    			controller.getStaff().setLocation((int)controller.getScrollbar().getMax());
                    		break;
                    	// @since v1.4, adds A and D as controls. move to correct spot later if needed
                    	case A:
                    		if(!ke.isControlDown() && !ke.isShiftDown())
                    			controller.getStaff().moveLeft();
                    	// @since v1.1.2, requested by seymour
                    	case LEFT:
                    		if (controller.getNameTextField().focusedProperty().get()) // Don't trigger while typing name
                				break;
                    		if(ke.isControlDown() && ke.isShiftDown())
                    			controller.getStaff().shift(-4);
                    		if((ke.isControlDown() && ke.getCode() != KeyCode.A) || ke.isShiftDown())
                    			controller.getStaff().shift(-4);
                    		break;
                    	case D:
                    		if(!ke.isControlDown() && !ke.isShiftDown())
                    			controller.getStaff().moveRight();
                    	case RIGHT:
                    		if (controller.getNameTextField().focusedProperty().get()) // Don't trigger while typing name
                    			break;
                    		if(ke.isControlDown() && ke.isShiftDown())
                    			controller.getStaff().shift(4);
                    		if(ke.isControlDown() || ke.isShiftDown())
                    			controller.getStaff().shift(4);
                    		break;
                    		
                    	// @since 1.4, adds conventional spacebar functionality 
                    	// TODO: Make this better please =)
                    	case SPACE:
                    		if (controller.getNameTextField().focusedProperty().get()) // Don't trigger while typing name
                    			break;
                    		
                    		if (ke.isControlDown() || ke.isShiftDown())
                    			controller.getStaff().setLocation(0);
                    		
                			if (StateMachine.getState() == ProgramState.SONG_PLAYING) {
                	            StateMachine.setState(ProgramState.EDITING);
                        		controller.getStaff().stopSong();
                	        } else if (StateMachine.getState() == ProgramState.ARR_PLAYING) {
                	            StateMachine.setState(ProgramState.ARR_EDITING);
                	            controller.getStaff().stopSong();
                	        } else if (StateMachine.getState() == ProgramState.EDITING) {
                	            StateMachine.setState(ProgramState.SONG_PLAYING);
                	            controller.getStaff().startSong();
                	        } else if (StateMachine.getState() == ProgramState.ARR_EDITING) {
                	            StateMachine.setState(ProgramState.ARR_PLAYING);
                	            controller.getStaff().startArrangement();
                	        }
                    		break;
                    	default:
                    	}
                    	
                        StateMachine.getButtonsPressed().add(ke.getCode());
                        StaffInstrumentEventHandler.updateAccidental();
                        ke.consume();
                    }
                });

        primaryScene.addEventHandler(KeyEvent.KEY_RELEASED,
                new EventHandler<KeyEvent>() {

                    @Override
                    public void handle(KeyEvent ke) {
                        StateMachine.getButtonsPressed().remove(ke.getCode());
                        StaffInstrumentEventHandler.updateAccidental();
                        ke.consume();
                    }
                });

        //put this into StateMachine?
		primaryScene.addEventHandler(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent se) {
				if (se.getDeltaY() < 0) {
//					if (se.isControlDown())
//						controller.getStaff().setLocation((int) controller.getScrollbar().getValue() + 4);
//					else
						controller.getStaff().moveRight();
				} else if (se.getDeltaY() > 0) {
//					if (se.isControlDown())
//						controller.getStaff().setLocation((int) controller.getScrollbar().getValue() - 4);
//					else
						controller.getStaff().moveLeft();
				}
				se.consume();
			}
		});
        
        primaryStage.focusedProperty().addListener(
                new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> ov,
                            Boolean t, Boolean t1) {
                        StateMachine.clearKeyPresses();
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
                System.setProperty("javafx.preloader", SplashScreen.class.getName());
                Application.launch(SuperMarioPaint.class, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (args.length > 0
                && (args[0].equals("--debug") || args[0].equals("--d"))) {
            if (args.length > 1) {
                try {
                    Settings.setDebug(Integer.parseInt(args[1]));
                } catch (NumberFormatException e) {
                    Settings.setDebug(1);
                }
            } else {
                Settings.setDebug(1);
            }
            try {
                System.setProperty("javafx.preloader", SplashScreen.class.getName());
                Application.launch(SuperMarioPaint.class, args);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

	}
    
    /**
     * Changes the cursor image.
     * TYPES:
     * 0 - Default
     * 1 - Grab
     * 2 - Open (splayed)
     * 3 - Eraser
     * 
     * @param isPressed
     * 			Which mouse button is being pressed (if any).
     */
    public void setCursor(int type) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                ImageCursor im = null; //((ImageLoader) imgLoader).getCursor(type);
                if (im != null) {
                    primaryScene.setCursor(im);
                }
            }
            
        });
    }
    
    /**
     * Changes the icon to the given image location.
     * 
     * @param fileLocation
     * 			The location of the new icon image.
     */
    public void setIcon(String fileLocation) {
    	if (new File(fileLocation).exists())
    		primaryStage.getIcons().add(new Image("file:" + fileLocation));
    }

	/**
	 * Gets the soundfont Loader. This function will probably only be temporary
	 * if we plan to move the loader to another class.
	 * 
	 * @return the soundfont Loader
	 */
	public Loader getSoundfontLoader() {
    	return sfLoader;
    }
}
