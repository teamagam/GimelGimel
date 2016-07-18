package com.teamagam.gimelgimel.app.common;

import java.util.HashSet;

/**
 * A simple implementation of an observable, that supports a single observer
 * and a notifying method
 */
public class NotifyingDataChangedObservable implements DataChangedObservable {

    private HashSet<DataChangedObserver> mObservers;

    public NotifyingDataChangedObservable() {
        mObservers = new HashSet<>();
    }

    @Override
    public void addObserver(DataChangedObserver observer) {
        mObservers.add(observer);
    }

    @Override
    public void removeObserver(DataChangedObserver observer) {
        mObservers.remove(observer);
    }

    protected void notifyObservers() {
        if (!mObservers.isEmpty()) {
            for (DataChangedObserver observer : mObservers) {
                observer.onDataChanged();
            }
        }
    }
}
