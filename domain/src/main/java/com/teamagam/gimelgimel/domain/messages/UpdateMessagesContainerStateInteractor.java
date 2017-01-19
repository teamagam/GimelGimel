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
public class UpdateMessagesContainerStateInteractor extends BaseDataInteractor {

    private final MessagesContainerStateRepository mMessagesContainerStateRepository;
    private final MessagesContainerStateRepository.ContainerState mState;

    public UpdateMessagesContainerStateInteractor(@Provided ThreadExecutor threadExecutor,
                                                  @Provided MessagesContainerStateRepository messagesContainerStateRepository,
                                                  MessagesContainerStateRepository.ContainerState state) {
        super(threadExecutor);
        mMessagesContainerStateRepository = messagesContainerStateRepository;
        mState = state;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest toggleContainerState = factory.create(
                Observable.just(mState).doOnNext(mMessagesContainerStateRepository::updateState)
        );

        return Collections.singletonList(toggleContainerState);
    }

}
