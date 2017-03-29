package com.teamagam.gimelgimel.app.map.view;

import android.graphics.drawable.Drawable;

import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseViewModelFragment;
import com.teamagam.gimelgimel.app.map.viewModel.BaseMapViewModel;

public abstract class BaseDrawActionFragment<T extends BaseMapViewModel>
        extends BaseViewModelFragment<T> {

    public abstract void onActionFabClick();

    public abstract Drawable getFabActionDrawable();
}
