package com.teamagam.gimelgimel.app.map.view;

import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseViewModelFragment;
import com.teamagam.gimelgimel.app.map.viewModel.BaseMapViewModel;

public abstract class BaseDrawActionFragment<T extends BaseMapViewModel>
    extends BaseViewModelFragment<T> {

  protected abstract String getToolbarTitle();
}
