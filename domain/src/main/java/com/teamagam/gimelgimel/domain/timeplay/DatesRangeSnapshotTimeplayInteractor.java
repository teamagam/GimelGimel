package com.teamagam.gimelgimel.domain.timeplay;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import io.reactivex.Observable;
import java.util.Date;

@AutoFactory
public class DatesRangeSnapshotTimeplayInteractor extends TimeplayInteractor {
  public DatesRangeSnapshotTimeplayInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided GeoSnapshoter geoSnapshoter,
      long startTimeStamp,
      long endTimeStamp,
      Displayer displayer,
      long initialTimestamp) {
    super(threadExecutor, postExecutionThread, geoSnapshoter,
        new AutoTimeplayInteractor.CustomDatesTimespan(new Date(startTimeStamp),
            new Date(endTimeStamp)), displayer, 0, initialTimestamp);
  }

  @Override
  protected Observable<Long> createSourceObservable() {
    return Observable.just(0L);
  }
}