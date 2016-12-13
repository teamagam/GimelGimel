package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;

abstract class BaseDataInteractor extends BaseInteractor {

    private final ThreadExecutor mThreadExecutor;

    public BaseDataInteractor(
            ThreadExecutor threadExecutor) {
        mThreadExecutor = threadExecutor;
    }

    @Override
    protected final Iterable<SubscriptionRequest> buildSubscriptionRequests() {
        return buildSubscriptionRequests(
                new DataSubscriptionRequest.SubscriptionRequestFactory(mThreadExecutor));
    }

    abstract Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory);
}
