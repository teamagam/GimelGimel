package com.teamagam.gimelgimel.domain.timeplay;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import io.reactivex.Observable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@AutoFactory
public class AutoTimeplayInteractor extends TimeplayInteractor {

  private static final int DISPLAY_INTERVAL_MILLIS = 100;

  public AutoTimeplayInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided GeoSnapshoter geoSnapshoter,
      long startTimeStamp,
      long endTimeStamp,
      Displayer displayer,
      int intervalCount,
      long initialTimestamp) {
    super(threadExecutor, postExecutionThread, geoSnapshoter,
        new CustomDatesTimespan(new Date(startTimeStamp), new Date(endTimeStamp)), displayer,
        intervalCount, initialTimestamp);
  }

  @Override
  protected Observable<Long> createSourceObservable() {
    return Observable.interval(0, (long) DISPLAY_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
  }

  public static class CustomDatesTimespan implements GeoTimespanCalculator {

    private Date mStartDate;
    private Date mEndDate;

    public CustomDatesTimespan(Date startDate, Date endDate) {
      mStartDate = startDate;
      mEndDate = endDate;
    }

    @Override
    public Date getMinimumGeoItemDate() {
      return mStartDate;
    }

    @Override
    public Date getMaximumGeoItemDate() {
      return mEndDate;
    }
  }
}
