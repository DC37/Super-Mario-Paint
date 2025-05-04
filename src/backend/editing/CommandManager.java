package smp.commandmanager;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import smp.components.Values;

/**
 * A command manager responsible for recording commands, undoing, and redoing
 * commands.
 * 
 * Several commands can be executed then recorded into a list and, when undoing
 * or redoing, will undo or redo all those commands together.
 * 
 * @author J
 *
 */
public class CommandManager {

	protected Deque<List<CommandInterface>> undoStack;
	protected Deque<List<CommandInterface>> redoStack;

	private List<CommandInterface> nextCommands;
	
	public CommandManager() {
		undoStack = new LinkedBlockingDeque<>(Values.MAX_UNDO_REDO_SIZE);
		redoStack = new LinkedBlockingDeque<>(Values.MAX_UNDO_REDO_SIZE);
	}

	public void undo() {
		/* make sure any current action is recorded before undoing */
		record();
		
		if (undoStack.isEmpty())
			return;

		List<CommandInterface> commands = undoStack.pop();
		/* undo needs reverse order on the commands */
		for(int i = commands.size() - 1; i >= 0; i--) {
			CommandInterface command = commands.get(i);
			command.undo();
		}

		redoStack.push(commands);
		System.out.println("...undo..." + undoStack.size() + "/" + (undoStack.size() + redoStack.size()));
	}

	public void redo() {
		if (redoStack.isEmpty())
			return;

		List<CommandInterface> commands = redoStack.pop();
		for(CommandInterface command : commands) {
			command.redo();
		}

		undoStack.push(commands);
		System.out.println("...redo..." + undoStack.size() + "/" + (undoStack.size() + redoStack.size()));
	}

	/**
	 * This does not literally execute the command. It will put the command into
	 * a list then you can call record() to put the command list onto the
	 * undoStack.
	 * 
	 * @param command
	 */
	public void execute(CommandInterface command) {

		if (nextCommands == null)
			nextCommands = new ArrayList<>();
		
		nextCommands.add(command);
	}
	
	/**
	 * record the commands given to execute()
	 */
	public void record() {		
		if (nextCommands == null)
			return;
		if (undoStack.size() >= Values.MAX_UNDO_REDO_SIZE)
			undoStack.removeLast();
		
		undoStack.push(nextCommands);
		redoStack.clear();
		
		nextCommands = null;
	}
	
	public void reset() {
		undoStack.clear();
		redoStack.clear();
	}
}
