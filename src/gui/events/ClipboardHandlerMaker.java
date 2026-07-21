package gui.events;

import backend.songs.NoteLine;
import gui.StateMachine;
import gui.Values;
import gui.clipboard.StaffRubberBandEventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClipboardHandlerMaker extends HandlerMaker<StaffRubberBandEventHandler> {
    
    protected ClipboardHandlerMaker(StaffRubberBandEventHandler eventHandler) {
		super(eventHandler);
	}
	
	public static ClipboardHandlerMaker of(StaffRubberBandEventHandler eventHandler) {
		return new ClipboardHandlerMaker(eventHandler);
	}
	
	@Override
	public void initializeIn(Node n) {
		n.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            switch (event.getCode()) {
            case A:
            	tryPerformFirstMatchingSubaction(event,
            			new Subaction<>(ev -> ev.isControlDown() && !ev.isShiftDown(), this::selectAllItems));
            	break;
            case C:
            	tryPerformFirstMatchingSubaction(event,
            			new Subaction<>(KeyEvent::isControlDown, this::copy));
                break;
            case N:
            	tryPerformFirstMatchingSubaction(event,
            			new Subaction<>(KeyEvent::isAltDown, this::toggleNoteSelection));
                break;
            case V:
            	tryPerformFirstMatchingSubaction(event,
            			new Subaction<>(KeyEvent::isAltDown, this::toggleVolumeSelection),
            			new Subaction<>(KeyEvent::isControlDown, this::paste));
                break;
            case X:
            	tryPerformFirstMatchingSubaction(event,
            			new Subaction<>(KeyEvent::isControlDown, this::cut));
                break;
            case DELETE:
            	tryPerformFirstMatchingSubaction(event,
            			new Subaction<>(d -> true, this::delete));
                break;
            default:
                break;
            }
        });
	}
	
	@Override
	protected boolean canTryPerformFirstMatchingSubaction() {
		return true;
	}
	
	/**
     * @relevant Values.NOTELINES_IN_THE_WINDOW,
     *           StateMachine.getMeasureLineNum()
     * 
     * @param x
     *            mouse pos for entire scene
     * @return -1 if out of bounds (x < 128 || x > 784), 0-9 otherwise
    */
   private int getLine(double x) {
       
       if (x < 160 || x > 1008) {
           return -1;
       }
       return (((int) x - 165) / 64);
   }
   
   private void selectAllItems() {
	   source.getTheStaffClipboard().getAPI().clearSelection();
       source.getTheStaffClipboard().getAPI().select(0, 0,
               (int) source.getController().getScrollbar().getMax() + Values.NOTELINES_IN_THE_WINDOW,
               Values.NOTES_IN_A_LINE);
   }
   
   private void toggleNoteSelection() {
	   source.getTheStaffClipboard().getAPI().selectNotesToggle(!source.getTheStaffClipboard().getAPI().isSelectNotesOn());
   }
   
   private void toggleVolumeSelection() {
	   source.getTheStaffClipboard().getAPI().selectVolumesToggle(!source.getTheStaffClipboard().getAPI().isSelectVolumesOn());
   }
   
   private void cut() {
	   source.getTheStaffClipboard().getAPI().cut();
   }
   
   private void copy() {
       log.info("COPY");
       source.getTheStaffClipboard().getAPI().copy();
       for (NoteLine line : source.getTheStaffClipboard().getCopiedData().values()) {
           if (!line.getNotes().isEmpty())
               log.info("{}", line);
       }
   }
   
   private void paste() {
	   int currentLine = getLine(source.getMouseX()) + StateMachine.getMeasureLineNum();
	   log.info("PASTE @ {}", currentLine);
       source.getTheStaffClipboard().getAPI().paste(currentLine);
   }
   
   private void delete() {
	   source.getTheStaffClipboard().getAPI().delete();
   }
	
}
