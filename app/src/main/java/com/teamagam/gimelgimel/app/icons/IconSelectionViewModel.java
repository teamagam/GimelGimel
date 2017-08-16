package com.teamagam.gimelgimel.app.icons;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.domain.icons.DisplayIconsInteractor;
import com.teamagam.gimelgimel.domain.icons.DisplayIconsInteractorFactory;
import com.teamagam.gimelgimel.domain.icons.entities.Icon;

@AutoFactory
class IconSelectionViewModel extends BaseViewModel {

  private final DisplayIconsInteractorFactory mDisplayIconsInteractorFactory;
  private final DisplayIconsInteractor.Displayer mDisplayer;
  private final OnIconSelectionListener mOnIconSelectionListener;
  private Icon mSelectedIcon;

  public IconSelectionViewModel(
      @Provided DisplayIconsInteractorFactory displayIconsInteractorFactory,
      OnIconSelectionListener onIconSelectionListener,
      DisplayIconsInteractor.Displayer displayer) {
    mDisplayIconsInteractorFactory = displayIconsInteractorFactory;
    mOnIconSelectionListener = onIconSelectionListener;
    mDisplayer = displayer;
  }

  @Override
  public void start() {
    super.start();
    mDisplayIconsInteractorFactory.create(mDisplayer).execute();
  }

  public void onIconSelected(Icon icon) {
    mSelectedIcon = icon;
  }

  public void onPositiveButtonClicked() {
    if (mOnIconSelectionListener != null) {
      mOnIconSelectionListener.onIconSelected(mSelectedIcon);
    }
  }
}
