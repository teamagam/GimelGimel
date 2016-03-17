package com.teamagam.gimelgimel.app.view.viewer.cesium;

/**
 * A class that holds reference to some data object.
 * Exposes thread-safe set/get methods
 */
public class SynchronizedDataHolder<T> {

    private T mData;

    //Constructors aren't thread-safe, as it makes no sense.

    public SynchronizedDataHolder() {
        this(null);
    }

    public SynchronizedDataHolder(T initialData) {
        mData = initialData;
    }

    public synchronized void setCurrentLocation(T newData) {
        mData = newData;
    }

    public synchronized T getCurrentLocation() {
        return mData;
    }
}
