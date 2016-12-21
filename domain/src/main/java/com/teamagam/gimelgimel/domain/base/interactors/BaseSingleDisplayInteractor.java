package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;

import java.util.Collections;

public abstract class BaseSingleDisplayInteractor extends BaseDisplayInteractor {

    public BaseSingleDisplayInteractor(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    protected final Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
        return Collections.singletonList(buildSubscriptionRequest(factory));
    }

    protected abstract SubscriptionRequest buildSubscriptionRequest(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory);
}
