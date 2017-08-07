package com.teamagam.gimelgimel.app.map.actions;

import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseViewModelFragment;

public abstract class BaseDrawActionFragment<T extends BaseViewModel>
    extends BaseViewModelFragment<T> {

  public void finish() {
    getActivity().finish();
  }

  protected abstract String getToolbarTitle();
}
