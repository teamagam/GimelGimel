package com.teamagam.gimelgimel.domain.location;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import io.reactivex.Observable;
import java.util.Collections;
import javax.inject.Inject;

public class StartLocationUpdatesInteractor extends BaseDataInteractor {

  private LocationEventFetcher mLocationEventFetcher;

  @Inject
  public StartLocationUpdatesInteractor(LocationEventFetcher locationEventFetcher,
      ThreadExecutor threadExecutor) {
    super(threadExecutor);
    mLocationEventFetcher = locationEventFetcher;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return Collections.singletonList(factory.create(Observable.just(mLocationEventFetcher),
        observable -> observable.doOnNext(LocationEventFetcher::startFetching)));
  }
}
