package com.teamagam.gimelgimel.app.location;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.base.view.LatLongPicker;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;

@AutoFactory
public class GoToLocationViewModel {

  private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
  private final LatLongPicker mLatLongPicker;
  private GoToLocationDialogFragment mView;

  public GoToLocationViewModel(
      @Provided GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
      @Provided PreferencesUtils preferencesUtils, LatLongPicker latLongPicker,
      GoToLocationDialogFragment fragment) {
    mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
    mLatLongPicker = latLongPicker;
    mLatLongPicker.setCoordinateSystem(preferencesUtils.shouldUseUtm());
    mView = fragment;
  }

  public void start() {
    mLatLongPicker.setOnValidStateChangedListener(new LatLongPicker.OnValidStateChangedListener() {
      @Override
      public void onValid() {
        setPositiveButtonEnabled(true);
      }

      @Override
      public void onInvalid() {
        setPositiveButtonEnabled(false);
      }
    });
    setPositiveButtonEnabled(mLatLongPicker.hasPoint());
  }

  public void stop() {
    mLatLongPicker.setOnValidStateChangedListener(null);
  }

  public void onPositiveButtonClicked() {
    mGoToLocationMapInteractorFactory.create(mLatLongPicker.getPoint()).execute();
  }

  private void setPositiveButtonEnabled(boolean enabled) {
    mView.setPositiveButtonEnabled(enabled);
  }
}