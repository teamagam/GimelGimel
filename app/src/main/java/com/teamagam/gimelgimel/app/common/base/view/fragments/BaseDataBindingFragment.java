package com.teamagam.gimelgimel.app.common.base.view.fragments;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;

/**
 * used for VMMV design.
 */
public abstract class BaseDataBindingFragment<VM extends BaseViewModel>
    extends BaseViewModelFragment<VM> {

  /**
   * if the data fragment needs to add specific view functionality (e.g. OnClick).
   */
  @SuppressWarnings("unused")
  protected void createSpecificViews(View rootView) {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);

    ViewDataBinding bind = bindViewModel(rootView);

    createSpecificViews(rootView);
    return bind.getRoot();
  }

  protected abstract ViewDataBinding bindViewModel(View rootView);
}
