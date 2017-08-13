package com.teamagam.gimelgimel.domain.timeplay;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import io.reactivex.Observable;

@AutoFactory
public class SnapshotTimeplayInteractor extends TimeplayInteractor {
  public SnapshotTimeplayInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided GeoSnapshoter geoSnapshoter,
      @Provided GeoTimespanCalculator geoTimespanCalculator,
      Displayer displayer,
      long initialTimestamp) {
    super(threadExecutor, postExecutionThread, geoSnapshoter, geoTimespanCalculator, displayer, 0,
        initialTimestamp);
  }

  @Override
  protected Observable<Long> createSourceObservable() {
    return Observable.just(0L);
  }
}