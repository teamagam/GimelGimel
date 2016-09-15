package com.teamagam.gimelgimel.presentation.presenters.base;

import com.teamagam.gimelgimel.presentation.presenters.SendGeoMessagePresenter;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;

/**
 * This is a base class for all presenters which are communicating with interactors. This base class will hold a
 * reference to the Executor and MainThread objects that are needed for running interactors in a background thread.
 */
public abstract class AbstractPresenter<V extends BaseView, T> implements BasePresenter<T>{

    protected List<WeakReference<V>> mViewWRList;

    public AbstractPresenter() {

        mViewWRList = new LinkedList<>();
    }

    protected boolean isNotNull(Object o) {
        return o != null;
    }

    protected Observable<V> getObservableViews(){
        return Observable.from(mViewWRList)
                .map(WeakReference::get)
                .filter(this::isNotNull);
    }

    public void addView(V view) {
        mViewWRList.add(new WeakReference<>(view));
    }

    public void removeView(SendGeoMessagePresenter.View view) {
        for (WeakReference<V> viewWR : mViewWRList) {
            if (view.equals(viewWR.get())) {
                mViewWRList.remove(viewWR);
                return;
            }
        }
    }
}
