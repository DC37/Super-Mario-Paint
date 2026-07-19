package gui.events;

import java.util.function.Consumer;

import backend.songs.Accidental;
import gui.SMPFXController;
import gui.Staff;
import gui.StateMachine;
import gui.Utilities;
import gui.Values;
import gui.components.staff.StaffMouseEventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;

public class KeyboardHandlerMaker extends HandlerMaker<SMPFXController> {
	
	protected KeyboardHandlerMaker(SMPFXController controller) {
		super(controller);
	}
	
	public static KeyboardHandlerMaker of(SMPFXController controller) {
		return new KeyboardHandlerMaker(controller);
	}
	
	@Override
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
                trySetStaffLocation((int) source.getScrollbar().getMax(), ke);
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
            			new Subaction<>(KeyEvent::isShiftDown,
            					() -> StateMachine.setClipboardPressed(!StateMachine.isClipboardPressed())));
            	break;
                
            case S:
            	tryPerformFirstMatchingSubaction(ke,
            			new Subaction<>(KeyEvent::isControlDown,
            					() -> source.save(Utilities.getOwner(ke))));
                break;
                
            case M:
            	tryPerformFirstMatchingSubaction(ke,
            			new Subaction<>(d -> true,
            					() -> StateMachine.setMuteAPressed(!StateMachine.isMuteAPressed())));
                break;
                
            case N:
            	tryPerformFirstMatchingSubaction(ke,
            			new Subaction<>(ev -> !ev.isControlDown() && !ev.isAltDown(),
            					() -> StateMachine.setMutePressed(!StateMachine.isMutePressed())),
            			new Subaction<>(KeyEvent::isControlDown,
            					() -> source.newSongOrArrangement(Utilities.getOwner(ke))));
            	break;
                
            case O:
            	tryPerformFirstMatchingSubaction(ke,
            			new Subaction<>(KeyEvent::isControlDown,
            					() -> source.load(Utilities.getOwner(ke))));
                break;
                
            case COMMA:
            	tryPerformFirstMatchingSubaction(ke,
            			new Subaction<>(KeyEvent::isControlDown,
            					() -> source.options(Utilities.getOwner(ke))));
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
	
	@Override
    protected boolean canTryPerformFirstMatchingSubaction() {
    	return !source.getNameTextField().focusedProperty().get();
    }
	
	private void tryShiftStaff(int noLines) {
    	if (StateMachine.isPlaybackActive())
    		return;
    	
    	source.getStaff().shift(noLines);
    }
    
    private void trySetStaffLocation(int pos, KeyEvent ke) {
    	if (StateMachine.isPlaybackActive())
    		return;
    	
    	if (ke.isControlDown())
    		source.getStaff().setLocation(pos);
    }
    
    private void tryJumpStaffTo(Consumer<Staff> fnWhereToJump, KeyEvent ke) {
    	if (StateMachine.isPlaybackActive())
            return;
        
        if (source.getNameTextField().focusedProperty().get()) // Don't trigger while typing name
            return;
        
        if (ke.isControlDown() || ke.isShiftDown())
        	fnWhereToJump.accept(source.getStaff());
    }
    
    private void tryMoveOrJumpStaffTo(
    		Consumer<Staff> fnWhereToMove, Consumer<Staff> fnWhereToJump,
    		boolean shouldCheckCtrl, KeyEvent ke) {
    	
    	if (StateMachine.isPlaybackActive())
            return;
        
        if (!ke.isControlDown() && !ke.isShiftDown())
        	fnWhereToMove.accept(source.getStaff());
            
        if ((shouldCheckCtrl && ke.isControlDown()) || ke.isShiftDown())
        	fnWhereToJump.accept(source.getStaff());
    }
    
    private void tryStartOrStop(KeyEvent ke) {
    	if (source.getNameTextField().focusedProperty().get()) // Don't trigger while typing name
            return;

        if (ke.isControlDown() || ke.isShiftDown())
            source.getStaff().setLocation(0);
        
        if (StateMachine.isPlaybackActive()) {
            source.getStopButton().setSelected(true);
            source.getStaff().stop();
        } else {
            source.getPlayButton().setSelected(true);
            source.getStaff().play();
        }
    }
    
    private void refreshAndConsumeKeyEvent(KeyEvent ke) {
    	if (StateMachine.isCursorOnStaff()) {
            Accidental acc = StaffMouseEventHandler.computeAccidental();
            source.getStaff().getDisplayManager().refreshSilhouette(acc);
        }

        ke.consume();
    }
    
    private void tryScrollStaff(ScrollEvent se) {
    	if (StateMachine.isPlaybackActive())
            return;

        if (se.getDeltaY() < 0) {
            if (se.isControlDown())
            	source.getStaff().shift(4);
            else
            	source.getStaff().moveRight();
            
        } else if (se.getDeltaY() > 0) {
            if (se.isControlDown())
            	source.getStaff().shift(-4);
            else
            	source.getStaff().moveLeft();
        }

        se.consume();
    }
	
}
