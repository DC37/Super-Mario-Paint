package model;

import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class StateInteger extends StateItem<Integer, Number, IntegerProperty> {
	
	private AtomicInteger source;

	public StateInteger(int value) {
		super(value);
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
	
}
