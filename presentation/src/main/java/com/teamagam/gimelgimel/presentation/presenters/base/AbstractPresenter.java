package com.teamagam.gimelgimel.presentation.presenters.base;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * This is a base class for all presenters which are communicating with interactors. This base class will hold a
 * reference to the Executor and MainThread objects that are needed for running interactors in a background thread.
 */
public abstract class AbstractPresenter<T> extends Subscriber<T> implements BasePresenter{

    public AbstractPresenter() {

    }

    @Override
    public void onCompleted() {
        //no-op
    }

    @Override
    public void onError(Throwable e) {
        //no-op
    }

    protected boolean isNotNull(Object o) {
        return o != null;
    }

    protected <V extends BaseView> Observable<V> getObservableViews(List<WeakReference<V>> list){
        return Observable.from(list)
                .filter(this::isNotNull)
                .map(WeakReference::get)
                .filter(this::isNotNull);
    }
}
