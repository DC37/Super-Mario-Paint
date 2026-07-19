package gui;

import java.util.function.Consumer;
import java.util.function.Predicate;

import backend.songs.Accidental;
import gui.components.staff.StaffMouseEventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;

public class KeyboardHandlerMaker {
	
	private SMPFXController controller;

	private KeyboardHandlerMaker(
			SMPFXController controller) {
		
		this.controller = controller;
	}
	
	public static KeyboardHandlerMaker of(SMPFXController controller) {
		return new KeyboardHandlerMaker(controller);
	}
	
	public void initializeIn(Node n) {
        n.addEventHandler(KeyEvent.KEY_PRESSED, ke -> {
            switch(ke.getCode()) {
            case PAGE_UP:
            	tryShiftStaff(-Values.NOTELINES_IN_THE_WINDOW);
                break;

            case PAGE_DOWN:
                tryShiftStaff(Values.NOTELINES_IN_THE_WINDOW);
                break;

            case HOME:
            	trySetStaffLocation(0, ke);
                break;

            case END:
                trySetStaffLocation((int) controller.getScrollbar().getMax(), ke);
                break;

            case A:
            	tryMoveOrJumpStaffTo(Staff::moveLeft, Staff::jumpToNext, false, ke);
                break;

            case LEFT:
            	tryJumpStaffTo(Staff::jumpToPrevious, ke);
            	break;

            case D:
            	tryMoveOrJumpStaffTo(Staff::moveRight, Staff::jumpToNext, true, ke);
                break;

            case RIGHT:
            	tryJumpStaffTo(Staff::jumpToNext, ke);
                break;

            case SPACE:
                tryStartOrStop(ke);
                break;

            case R:
            	tryPerformFirstMatchingSubaction(ke,
            			new KeyboardSubaction(KeyEvent::isShiftDown,
            					() -> StateMachine.setClipboardPressed(!StateMachine.isClipboardPressed())));
            	break;
                
            case S:
            	tryPerformFirstMatchingSubaction(ke,
            			new KeyboardSubaction(KeyEvent::isControlDown,
            					() -> controller.save(Utilities.getOwner(ke))));
                break;
                
            case M:
            	tryPerformFirstMatchingSubaction(ke,
            			new KeyboardSubaction(d -> true,
            					() -> StateMachine.setMuteAPressed(!StateMachine.isMuteAPressed())));
                break;
                
            case N:
            	tryPerformFirstMatchingSubaction(ke,
            			new KeyboardSubaction(code -> !code.isControlDown() && !code.isAltDown(),
            					() -> StateMachine.setMutePressed(!StateMachine.isMutePressed())),
            			new KeyboardSubaction(KeyEvent::isControlDown,
            					() -> controller.newSongOrArrangement(Utilities.getOwner(ke))));
            	break;
                
            case O:
            	tryPerformFirstMatchingSubaction(ke,
            			new KeyboardSubaction(KeyEvent::isControlDown,
            					() -> controller.load(Utilities.getOwner(ke))));
                break;
                
            case COMMA:
            	tryPerformFirstMatchingSubaction(ke,
            			new KeyboardSubaction(KeyEvent::isControlDown,
            					() -> controller.options(Utilities.getOwner(ke))));
                break;
                
            default:
            }

            StateMachine.getButtonsPressed().add(ke.getCode());
            refreshAndConsumeKeyEvent(ke);
        });

        n.addEventHandler(KeyEvent.KEY_RELEASED, ke -> {
            StateMachine.getButtonsPressed().remove(ke.getCode());
            refreshAndConsumeKeyEvent(ke);
        });

        n.addEventHandler(ScrollEvent.ANY, this::tryScrollStaff);
    }
	
	private void tryShiftStaff(int noLines) {
    	if (StateMachine.isPlaybackActive())
    		return;
    	
    	controller.getStaff().shift(noLines);
    }
    
    private void trySetStaffLocation(int pos, KeyEvent ke) {
    	if (StateMachine.isPlaybackActive())
    		return;
    	
    	if (ke.isControlDown())
    		controller.getStaff().setLocation(pos);
    }
    
    private void tryJumpStaffTo(Consumer<Staff> fnWhereToJump, KeyEvent ke) {
    	if (StateMachine.isPlaybackActive())
            return;
        
        if (controller.getNameTextField().focusedProperty().get()) // Don't trigger while typing name
            return;
        
        if (ke.isControlDown() || ke.isShiftDown())
        	fnWhereToJump.accept(controller.getStaff());
    }
    
    private void tryMoveOrJumpStaffTo(
    		Consumer<Staff> fnWhereToMove, Consumer<Staff> fnWhereToJump,
    		boolean shouldCheckCtrl, KeyEvent ke) {
    	
    	if (StateMachine.isPlaybackActive())
            return;
        
        if (!ke.isControlDown() && !ke.isShiftDown())
        	fnWhereToMove.accept(controller.getStaff());
            
        if ((shouldCheckCtrl && ke.isControlDown()) || ke.isShiftDown())
        	fnWhereToJump.accept(controller.getStaff());
    }
    
    private void tryStartOrStop(KeyEvent ke) {
    	if (controller.getNameTextField().focusedProperty().get()) // Don't trigger while typing name
            return;

        if (ke.isControlDown() || ke.isShiftDown())
            controller.getStaff().setLocation(0);
        
        if (StateMachine.isPlaybackActive()) {
            controller.getStopButton().setSelected(true);
            controller.getStaff().stop();
            
        } else {
            controller.getPlayButton().setSelected(true);
            controller.getStaff().play();
        }
    }
    
    private void tryPerformFirstMatchingSubaction(
    		KeyEvent ke, KeyboardSubaction... actions) {
    	
    	if (controller.getNameTextField().focusedProperty().get())
            return;
    	
    	for (KeyboardSubaction ksa: actions) {
    		if (ksa.tryTrigger(ke))
    			return;
    	}
    }
    
    private void refreshAndConsumeKeyEvent(KeyEvent ke) {
    	if (StateMachine.isCursorOnStaff()) {
            Accidental acc = StaffMouseEventHandler.computeAccidental();
            controller.getStaff().getDisplayManager().refreshSilhouette(acc);
        }

        ke.consume();
    }
    
    private void tryScrollStaff(ScrollEvent se) {
    	if (StateMachine.isPlaybackActive())
            return;

        if (se.getDeltaY() < 0) {
            if (se.isControlDown())
                controller.getStaff().shift(4);
            else
            	controller.getStaff().moveRight();
            
        } else if (se.getDeltaY() > 0) {
            if (se.isControlDown())
            	controller.getStaff().shift(-4);
            else
            	controller.getStaff().moveLeft();
        }

        se.consume();
    }
    
    private static class KeyboardSubaction {
    	
    	private Predicate<KeyEvent> fnMatches;
    	private Runnable fnExecute;
    	
    	public KeyboardSubaction(
    			Predicate<KeyEvent> fnMatches,
    			Runnable fnExecute) {
    		
    		this.fnMatches = fnMatches;
    		this.fnExecute = fnExecute;
    	}
    	
    	public boolean tryTrigger(KeyEvent ke) {
    		if (fnMatches.test(ke)) {
    			fnExecute.run();
    			return true;
    		}
    		
    		return false;
    	}
    	
    }
	
}
