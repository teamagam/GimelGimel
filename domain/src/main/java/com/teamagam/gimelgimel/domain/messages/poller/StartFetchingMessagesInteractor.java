package com.teamagam.gimelgimel.domain.messages.poller;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.poller.strategy.RepeatedBackoffTaskRunner;
import io.reactivex.Observable;
import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Named;

public class StartFetchingMessagesInteractor extends BaseDataInteractor {

  private final RepeatedBackoffTaskRunner mMessagesTaskRunner;

  @Inject
  public StartFetchingMessagesInteractor(ThreadExecutor threadExecutor,
      @Named("message poller") RepeatedBackoffTaskRunner taskRunner) {
    super(threadExecutor);
    mMessagesTaskRunner = taskRunner;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest request = factory.create(Observable.just(mMessagesTaskRunner),
        observable -> observable.doOnNext(RepeatedBackoffTaskRunner::start));
    return Collections.singletonList(request);
  }
}
