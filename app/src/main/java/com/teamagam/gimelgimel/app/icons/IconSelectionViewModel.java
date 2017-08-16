package com.teamagam.gimelgimel.app.icons;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.domain.icons.DisplayIconsInteractor;
import com.teamagam.gimelgimel.domain.icons.DisplayIconsInteractorFactory;

@AutoFactory
class IconSelectionViewModel extends BaseViewModel {

  private final DisplayIconsInteractorFactory mDisplayIconsInteractorFactory;
  private DisplayIconsInteractor.Displayer mDisplayer;

  public IconSelectionViewModel(
      @Provided DisplayIconsInteractorFactory displayIconsInteractorFactory,
      DisplayIconsInteractor.Displayer displayer) {
    mDisplayIconsInteractorFactory = displayIconsInteractorFactory;
    mDisplayer = displayer;
  }

  @Override
  public void start() {
    super.start();
    mDisplayIconsInteractorFactory.create(mDisplayer).execute();
  }
}
