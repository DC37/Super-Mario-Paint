package smp;

/**
 * A Thread loader that loads some component of
 * Super Mario Paint.
 * @author RehdBlob
 * @since 2012.10.23
 */
public interface Loader extends Runnable {

    /**
     * Gives the load status of the Loader.
     * @return A double between 0 and 1 representing the load
     * status of this thread.
     */
    public double getLoadStatus();

    /**
     * Sets the load status of the Loader to some double
     * between 0 and 1.
     */
    public void setLoadStatus(double d);

}
