package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesContainerStateRepository;

import java.util.Collections;

import rx.Observable;

@AutoFactory
public class ToggleMessagesContainerStateInteractor extends BaseDataInteractor {

    private final MessagesContainerStateRepository mMessagesContainerStateRepository;

    public ToggleMessagesContainerStateInteractor(@Provided ThreadExecutor threadExecutor,
                                                  @Provided MessagesContainerStateRepository messagesContainerStateRepository) {
        super(threadExecutor);
        mMessagesContainerStateRepository = messagesContainerStateRepository;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest toggleContainerState = factory.create(
                Observable.empty().doOnCompleted(mMessagesContainerStateRepository::toggleState)
        );

        return Collections.singletonList(toggleContainerState);
    }

}
