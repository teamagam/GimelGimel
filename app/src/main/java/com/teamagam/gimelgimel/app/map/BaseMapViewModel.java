package com.teamagam.gimelgimel.app.map;

import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;

public class BaseMapViewModel<V> extends BaseViewModel<V> {

  private final GGMapView mGGMapView;
  private final MapEntitiesDisplayer mMapEntitiesDisplayer;

  private boolean mIsSubscribedToOnReady;

  protected BaseMapViewModel(MapEntitiesDisplayerFactory mapEntitiesDisplayerFactory,
      GGMapView ggMapView) {
    mMapEntitiesDisplayer = mapEntitiesDisplayerFactory.create(ggMapView);
    mGGMapView = ggMapView;
    mIsSubscribedToOnReady = false;
  }

  @Override
  public void init() {
    super.init();
    subscribeToOnReady();
  }

  @Override
  public void start() {
    super.start();
    subscribeToOnReady();
  }

  @Override
  public void destroy() {
    super.destroy();
    mMapEntitiesDisplayer.stop();
    unsubscribeToOnReady();
  }

  private void subscribeToOnReady() {
    if (!mIsSubscribedToOnReady) {
      mGGMapView.setOnReadyListener(this::onMapReady);
      mIsSubscribedToOnReady = true;
    }
  }

  private void onMapReady() {
    mMapEntitiesDisplayer.start();
  }

  private void unsubscribeToOnReady() {
    mGGMapView.setOnReadyListener(null);
    mIsSubscribedToOnReady = false;
  }
}