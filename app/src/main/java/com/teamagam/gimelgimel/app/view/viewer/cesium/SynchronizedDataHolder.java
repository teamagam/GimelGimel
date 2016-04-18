package com.teamagam.gimelgimel.app.view.viewer.cesium;

/**
 * A class that holds a synchronized reference to some data object.
 * Exposes thread-safe set/get methods
 */
public class SynchronizedDataHolder<T> {

    private T mData;

    public SynchronizedDataHolder(T initialData) {
        mData = initialData;
    }

    public synchronized void setData(T newData) {
        mData = newData;
    }

    public synchronized T getData() {
        return mData;
    }
}
