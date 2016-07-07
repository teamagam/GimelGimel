package com.teamagam.gimelgimel.app.common;

/**
 * Defines data changes notifying behaviour through an {@link DataChangedObserver}
 */
public interface DataChangedObservable {
    void addObserver(DataChangedObserver observer);

    void removeObserver(DataChangedObserver observer);
}
