package com.teamagam.gimelgimel.domain.timeplay;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;

@AutoFactory
public class AutoTimeplayInteractor extends TimeplayInteractor {

  private static final int DISPLAY_INTERVAL_MILLIS = 100;

  public AutoTimeplayInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided GeoSnapshoter geoSnapshoter,
      @Provided GeoTimespanCalculator geoTimespanCalculator,
      Displayer displayer,
      int intervalCount,
      long initialTimestamp) {
    super(threadExecutor, postExecutionThread, geoSnapshoter, geoTimespanCalculator, displayer,
        intervalCount, initialTimestamp);
  }

  @Override
  protected Observable<Long> createSourceObservable() {
    return Observable.interval(0, (long) DISPLAY_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
  }
}