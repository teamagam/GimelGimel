package com.teamagam.gimelgimel.app.view.fragments;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.viewModels.BaseViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * used for VMMV design.
 */
public abstract class BaseDataFragment<VM extends BaseViewModel>
        extends BaseFragment<GGApplication>{

    private VM mViewModel;

    /**
     * if the data fragment needs to add specific view functionality (e.g. OnClick).
     */
    @SuppressWarnings("unused")
    protected void createSpecificViews(View rootView) {}

    protected abstract VM getSpecificViewModel();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NotNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mViewModel = getSpecificViewModel();
        ViewDataBinding bind = bindViewModel(rootView);

        createSpecificViews(rootView);
        return bind.getRoot();
    }

    protected abstract ViewDataBinding bindViewModel(View rootView);

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
