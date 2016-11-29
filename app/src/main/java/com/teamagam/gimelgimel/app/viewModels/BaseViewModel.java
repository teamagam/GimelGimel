package com.teamagam.gimelgimel.app.viewModels;

import android.databinding.BaseObservable;

import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;

/**
 * Created on 10/10/2016.
 */

public abstract class BaseViewModel<V> extends BaseObservable implements
        ViewModel<V> {

    protected AppLogger sLogger = AppLoggerFactory.create(((Object) this).getClass());

    protected V mView;

    @Override
    public void setView(V v) {
        mView = v;
    }

    @Override
    public void init() {
        //no-op
    }

    @Override
    public void start() {
        //no-op
    }

    @Override
    public void stop() {
        //no-op
    }

    @Override
    public void destroy() {
        //no-op
    }
}
