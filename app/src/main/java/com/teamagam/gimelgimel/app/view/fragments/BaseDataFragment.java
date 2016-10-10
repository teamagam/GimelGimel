package com.teamagam.gimelgimel.app.view.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.DataChangedObserver;
import com.teamagam.gimelgimel.app.viewModels.ViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * used for VMMV design.
 */
public abstract class BaseDataFragment<VM extends ViewModel>
        extends BaseFragment<GGApplication> {

    private VM mViewModel;

    /**
     * if the data fragment needs to add specific view functionality (e.g. OnClick).
     */
    @SuppressWarnings("unused")
    protected void createSpecificViews(View rootView) {}

    protected abstract VM getSpecificViewModel();

    @NotNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mViewModel =  getSpecificViewModel();
        createSpecificViews(rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewModel.stop();
    }

}
