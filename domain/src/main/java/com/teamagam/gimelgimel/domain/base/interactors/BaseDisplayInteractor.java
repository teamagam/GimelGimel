package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;

public abstract class BaseDisplayInteractor extends BaseInteractor {

  private final ThreadExecutor mThreadExecutor;
  private final PostExecutionThread mPostExecutionThread;

  public BaseDisplayInteractor(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    mThreadExecutor = threadExecutor;
    mPostExecutionThread = postExecutionThread;
  }

  @Override
  protected final Iterable<SubscriptionRequest> buildSubscriptionRequests() {
    return buildSubscriptionRequests(
        new DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory(mThreadExecutor,
            mPostExecutionThread));
  }

  protected abstract Iterable<SubscriptionRequest> buildSubscriptionRequests(
      DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory);
}
