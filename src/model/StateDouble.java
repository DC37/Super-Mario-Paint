package model;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;

public class StateDouble extends StateItem<Double, Number, DoubleProperty> {

	private AtomicReference<Double> source;

	public StateDouble(double value) {
		super(value);
	}
	
	@Override
	protected Double mapFromView(Number value) {
		return value.doubleValue();
	}
	
	@Override
	protected void initializeSource() {
		source = new AtomicReference<>();
	}
	
	@Override
	protected DoubleProperty createView() {
		return new SimpleDoubleProperty();
	}
	
	@Override
	public Double get() {
		return source.get();
	}
	
	@Override
	public void set(Double value) {
		source.set(value);
	}
	
	@Override
	public ObservableValue<Number> createBinding(Callable<Double> calc, Observable... deps) {
		return Bindings.createDoubleBinding(calc, deps);
	}
	
}
