package com.teamagam.gimelgimel.app.common;

/**
 * A simple implementation of an observable, that supports a single observer
 * and a notifying method
 */
public class NotifyingDataChangedObservable implements DataChangedObservable {

    private DataChangedObserver mObserver;

    @Override
    public void setObserver(DataChangedObserver observer) {
        mObserver = observer;
    }

    public void notifyObserver(){
        if(mObserver != null){
            mObserver.onDataChanged();
        }
    }
}
