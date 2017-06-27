package com.teamagam.gimelgimel.app.map.actions;

import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseViewModelFragment;
import com.teamagam.gimelgimel.app.map.BaseMapViewModel;

public abstract class BaseDrawActionFragment<T extends BaseMapViewModel>
    extends BaseViewModelFragment<T> {

  public void finish() {
    getActivity().finish();
  }

  protected abstract String getToolbarTitle();
}
