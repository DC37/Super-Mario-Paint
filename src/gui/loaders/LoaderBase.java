package gui.loaders;

public abstract class LoaderBase<V> implements Loader<V> {
	
	protected double loadStatus = 0.0;

	@Override
	public double getLoadStatus() {
		return loadStatus;
	}

	protected void setLoadStatus(double d) {
		loadStatus = d;
	}

}
