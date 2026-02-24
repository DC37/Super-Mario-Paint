package gui.loaders;

import java.util.concurrent.Callable;

/**
 * A Thread loader that loads some component of
 * Super Mario Paint.
 * @author RehdBlob
 * @since 2012.10.23
 */
public interface Loader<V> extends Callable<V> {

    /**
     * Gives the load status of the Loader.
     * @return A double between 0 and 1 representing the load
     * status of this thread.
     */
    public double getLoadStatus();

}
