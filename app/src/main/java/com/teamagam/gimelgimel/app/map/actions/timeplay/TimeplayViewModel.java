package com.teamagam.gimelgimel.app.map.actions.timeplay;

import android.support.annotation.NonNull;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.domain.timeplay.TimeplayInteractor;
import com.teamagam.gimelgimel.domain.timeplay.TimeplayInteractorFactory;

@AutoFactory
public class TimeplayViewModel extends BaseViewModel {

  private static final int INTERVAL_COUNT = 10;

  private final TimeplayInteractorFactory mTimeplayInteractorFactory;
  private final TimeplayInteractor.Displayer mMapDisplayer;
  private TimeplayInteractor mDisplayInteractor;

  public TimeplayViewModel(@Provided TimeplayInteractorFactory timeplayInteractorFactory,
      TimeplayInteractor.Displayer mapDisplayer) {
    mTimeplayInteractorFactory = timeplayInteractorFactory;
    mMapDisplayer = mapDisplayer;
  }

  @Override
  public void start() {
    super.start();
    mDisplayInteractor = createDisplayInteractor();
    execute(mDisplayInteractor);
  }

  @Override
  public void stop() {
    super.stop();
    unsubscribe(mDisplayInteractor);
  }

  @NonNull
  private TimeplayInteractor createDisplayInteractor() {
    return mTimeplayInteractorFactory.create(mMapDisplayer, INTERVAL_COUNT);
  }
}
