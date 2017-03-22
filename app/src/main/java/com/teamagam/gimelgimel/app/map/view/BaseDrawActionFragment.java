package com.teamagam.gimelgimel.app.map.view;

import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseFragment;

public abstract class BaseDrawActionFragment extends BaseFragment<GGApplication> {
    public abstract void onPositiveButtonClick();

    public abstract String getPositiveButtonText();
}
