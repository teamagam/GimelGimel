package com.teamagam.gimelgimel.app.view.fragments;


import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.app.common.DataChangedObservable;
import com.teamagam.gimelgimel.app.common.DataChangedObserver;

import org.jetbrains.annotations.NotNull;

/**
 * used for VMMV design.
 */
public abstract class BaseDataFragment<VM extends DataChangedObservable, T extends Application>
        extends BaseFragment<T>
        implements DataChangedObserver {

    protected VM mViewModel;

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

    /**
     * if the data fragment needs to add specific view functionality (e.g. OnClick).
     */
    @SuppressWarnings("unused")
    protected void createSpecificViews(View rootView) {}

    protected abstract void getSpecificViewModel();

    @NotNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        getSpecificViewModel();
        createSpecificViews(rootView);
        mViewModel.addObserver(this);
        setViews();
        return rootView;
    }

    private void setViews(){
        updateViewsOnUiThread();
    }

    protected abstract void updateViewsOnUiThread();

}
