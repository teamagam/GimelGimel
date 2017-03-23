package com.teamagam.gimelgimel.app.common.base.view.fragments;

import android.os.Bundle;

import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;

public abstract class BaseViewModelFragment<VM extends BaseViewModel>
        extends BaseFragment<GGApplication> {

    protected abstract VM getSpecificViewModel();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSpecificViewModel() != null) {
            getSpecificViewModel().init();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getSpecificViewModel() != null) {
            getSpecificViewModel().start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getSpecificViewModel() != null) {
            getSpecificViewModel().stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getSpecificViewModel() != null) {
            getSpecificViewModel().destroy();
        }
    }
}