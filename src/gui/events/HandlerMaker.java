package gui.events;

import java.util.function.Predicate;

import javafx.event.Event;
import javafx.scene.Node;

public abstract class HandlerMaker<T> {

	protected T source;
	
	protected HandlerMaker(T source) {
		this.source = source;
	}
	
	public abstract void initializeIn(Node n);
	protected abstract boolean canTryPerformFirstMatchingSubaction();
	
	@SafeVarargs
	protected final <E extends Event> void tryPerformFirstMatchingSubaction(
			E ke, Subaction<E>... actions) {
    	
		if (!canTryPerformFirstMatchingSubaction())
			return;
		
    	for (Subaction<E> ksa: actions) {
    		if (ksa.tryTrigger(ke))
    			return;
    	}
    }
	
	protected class Subaction<E extends Event> {

		private Predicate<E> fnMatches;
		private Runnable fnExecute;
		
		public Subaction(
				Predicate<E> fnMatches,
				Runnable fnExecute) {
			
			this.fnMatches = fnMatches;
			this.fnExecute = fnExecute;
		}
		
		public boolean tryTrigger(E ev) {
			if (fnMatches.test(ev)) {
				fnExecute.run();
				return true;
			}
			
			return false;
		}
		
	}
	
}
