package model;

import javafx.beans.property.Property;

public abstract class StateItem<T, V, P extends Property<V>> {

	private final P view;
	
	protected StateItem(T value) {
		initializeSource();
		set(value);
		
		view = createView();
		view.setValue(map(value));
	}
	
	protected abstract void initializeSource();
	protected abstract P createView();
	
	@SuppressWarnings("unchecked")
	protected V map(T value) {
		return (V) value;
	}
	
	public P viewProperty() {
		return view;
	}
	
	public abstract T get();
	public abstract void set(T value);
	
	public void sync() {
		T s = get();
		
		if (view.getValue() != s)
			view.setValue(map(s));
	}
	
}
