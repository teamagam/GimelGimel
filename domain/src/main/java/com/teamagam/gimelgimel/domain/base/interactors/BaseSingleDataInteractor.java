package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import java.util.Collections;

public abstract class BaseSingleDataInteractor extends BaseDataInteractor {
  protected BaseSingleDataInteractor(ThreadExecutor threadExecutor) {
    super(threadExecutor);
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    SubscriptionRequest subscriptionRequest = buildSubscriptionRequest(factory);
    return Collections.singleton(subscriptionRequest);
  }

  protected abstract SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory);
}
