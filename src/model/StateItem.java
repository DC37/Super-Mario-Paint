package model;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.UnaryOperator;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.util.StringConverter;

public abstract class StateItem<T, V, P extends Property<V>> {

	private final P view;
	
	protected StateItem(T value) {
		initializeSource();
		set(value);
		
		view = createView();
		view.setValue(mapToView(value));
	}
	
	protected abstract void initializeSource();
	protected abstract P createView();
	
	@SuppressWarnings("unchecked")
	protected V mapToView(T value) {
		return (V) value;
	}
	
	@SuppressWarnings("unchecked")
	protected T mapFromView(V value) {
		return (T) value;
	}
	
	public P viewProperty() {
		return view;
	}
	
	public abstract T get();
	public abstract void set(T value);
	
	public void sync() {
		V s = mapToView(get());
		
		if (!Objects.equals(s, view.getValue()))
			view.setValue(s);
	}
	
	public abstract ObservableValue<V> createBinding(Callable<T> calc, Observable... deps);
	
	public void bind(Property<V> prop) {
		prop.bind(view);
	}
	
	public void bind(Property<V> prop, UnaryOperator<T> calc) {
		prop.bind(createBinding(
				() -> calc.apply(mapFromView(view.getValue())),
				view));
	}
	
	public void bindBidirectional(Property<V> prop) {
		prop.bindBidirectional(view);
		prop.addListener((obs, oldV, newV) -> set(mapFromView(newV)));
	}
	
	public void bindBidirectional(StringProperty prop, StringConverter<V> fmt) {
		prop.bindBidirectional(view, fmt);
		prop.addListener((obs, oldV, newV) -> set(mapFromView(fmt.fromString(newV))));
	}
	
	public void addListener(InvalidationListener listener) {
		view.addListener(listener);
	}
	
}
