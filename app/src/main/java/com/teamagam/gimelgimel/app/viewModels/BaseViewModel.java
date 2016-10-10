package com.teamagam.gimelgimel.app.viewModels;

import android.databinding.BaseObservable;

/**
 * Created on 10/10/2016.
 */

public abstract class BaseViewModel<V> extends BaseObservable implements
        ViewModel<V> {

    protected V mView;

    @Override
    public void setView(V v){
        mView = v;
    }

    @Override
    public void stop() {
        //no-op
    }

    @Override
    public void start() {
        //no-op
    }
}
