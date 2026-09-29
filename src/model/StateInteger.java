package model;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;

public class StateInteger extends StateItem<Integer, Number, IntegerProperty> {
	
	private AtomicInteger source;

	public StateInteger(int value) {
		super(value);
	}
	
	@Override
	protected Integer mapFromView(Number value) {
		return value.intValue();
	}
	
	@Override
	protected void initializeSource() {
		source = new AtomicInteger();
	}
	
	@Override
	protected IntegerProperty createView() {
		return new SimpleIntegerProperty();
	}
	
	@Override
	public Integer get() {
		return source.get();
	}
	
	@Override
	public void set(Integer value) {
		source.set(value);
	}
	
	@Override
	public ObservableValue<Number> createBinding(Callable<Integer> calc, Observable... deps) {
		return Bindings.createIntegerBinding(calc, deps);
	}
	
}
