package model;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;

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
		T s = get();
		
		if (view.getValue() != s)
			view.setValue(mapToView(s));
	}
	
	public void bindBidirectional(Property<? extends V> prop) {
		// "prop" here is of type P (extends Property<V>).
		// prop.bindBidirectional(view);
		
		prop.addListener((obs, oldV, newV) -> set(mapFromView(newV)));
	}
	
	public void addListener(InvalidationListener listener) {
		view.addListener(listener);
	}
	
}
