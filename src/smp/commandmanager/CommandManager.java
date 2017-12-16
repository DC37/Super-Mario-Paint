package smp.commandmanager;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class CommandManager {

	public static final int MAX_SIZE = 1000;

	protected Deque<CommandInterface> undoStack;
	protected Deque<CommandInterface> redoStack;

	public CommandManager() {
		undoStack = new LinkedBlockingDeque<>(MAX_SIZE);
		redoStack = new LinkedBlockingDeque<>(MAX_SIZE);
	}

	public void undo() {
		if (undoStack.isEmpty())
			return;

		CommandInterface command = undoStack.pop();
		command.undo();

		redoStack.push(command);
	}

	public void redo() {
		if (redoStack.isEmpty())
			return;

		CommandInterface command = redoStack.pop();
		command.redo();

		undoStack.push(command);
	}

	public void execute(CommandInterface command) {
		if (undoStack.size() == MAX_SIZE)
			undoStack.removeLast();
		undoStack.push(command);
		redoStack.clear();
	}
	
	public void reset() {
		undoStack.clear();
		redoStack.clear();
	}
}
