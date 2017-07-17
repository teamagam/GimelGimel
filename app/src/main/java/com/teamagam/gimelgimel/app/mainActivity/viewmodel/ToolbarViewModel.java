package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.mainActivity.view.ToolbarFragment;
import javax.inject.Inject;

public class ToolbarViewModel extends BaseViewModel<ToolbarFragment> {

  private Navigator mNavigator;

  @Inject
  public ToolbarViewModel(Navigator navigator) {
    mNavigator = navigator;
  }

  public void onSendPolygonClicked() {
    mNavigator.openSendQuadrilateralAction();
  }

  public void onDrawGeometryClicked() {
    mNavigator.openSendGeometryAction();
  }

  public void onMeasureDistanceClicked() {
    mNavigator.openMeasureDistanceAction();
  }

  public void onGoToLocationClicked() {
    mNavigator.openGoToDialog();
  }

  public void onDynamicLayerClicked() {
    mNavigator.openDynamicLayerEditAction();
  }
}