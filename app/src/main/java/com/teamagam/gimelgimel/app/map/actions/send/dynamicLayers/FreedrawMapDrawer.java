package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.actions.freedraw.FreeDrawViewModel;
import com.teamagam.gimelgimel.domain.base.subscribers.ErrorLoggingObserver;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import java.util.Collection;

class FreedrawMapDrawer extends AbsMapAction {

  private final FreeDrawViewModel mFreeDrawViewModel;
  private OnEditingStartListener mListener;

  FreedrawMapDrawer(FreeDrawViewModel freeDrawViewModel, OnEditingStartListener listener) {
    mFreeDrawViewModel = freeDrawViewModel;
    mListener = listener;
  }

  @Override
  public void setupSymbologyPanel(EditDynamicLayerViewModel.SymbologyPanelVisibilitySetter setter) {
    super.setupSymbologyPanel(setter);
    setter.showFreeDrawPanel();
  }

  @Override
  public void setupAction(GGMapView ggMapView, Symbol symbol) {

  }

  @Override
  public void stop() {
    mFreeDrawViewModel.stop();
    mFreeDrawViewModel.clearFreeDrawer();
  }

  @Override
  public void start() {
    mFreeDrawViewModel.start();
    mFreeDrawViewModel.getSignalOnStartDrawingObservable()
        .subscribeWith(new EditingNotifyingObserver());
  }

  @Override
  public void updateSymbol(Symbol symbol) {
  }

  @Override
  public Collection<GeoEntity> getEntities() {
    return mFreeDrawViewModel.getEntities();
  }

  private class EditingNotifyingObserver extends ErrorLoggingObserver<Object> {
    private boolean mIsFirst = true;

    @Override
    public void onNext(Object o) {
      super.onNext(o);
      notifyEditingStartedOnce();
    }

    private void notifyEditingStartedOnce() {
      if (mIsFirst) {
        mListener.onEditingStarted();
        mIsFirst = false;
      }
    }
  }
}
