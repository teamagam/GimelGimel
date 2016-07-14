package com.teamagam.gimelgimel.app.view.fragments;


import android.app.Application;

import com.teamagam.gimelgimel.app.common.DataChangedObserver;

/**
 * used for VMMV design.
 */
public abstract class BaseDataFragment<T extends Application> extends BaseFragment<T>
        implements DataChangedObserver {

    @Override
    public void onDataChanged() {
        if(isAdded()){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BaseDataFragment.this.updateViewsOnUiThread();
                }
            });

        }
    }

    protected abstract void updateViewsOnUiThread();

    //todo: add observer here?
}
