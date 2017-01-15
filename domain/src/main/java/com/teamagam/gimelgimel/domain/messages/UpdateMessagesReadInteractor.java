package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import java.util.Arrays;
import java.util.Date;

import rx.Observable;

@AutoFactory
public class UpdateMessagesReadInteractor extends BaseDataInteractor {

    private final MessagesRepository mMessagesRepository;

    public UpdateMessagesReadInteractor(@Provided ThreadExecutor threadExecutor,
                                        @Provided MessagesRepository messagesRepository) {
        super(threadExecutor);
        mMessagesRepository = messagesRepository;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest readAllMessages = factory.create(
                Observable.just(new Date()).doOnNext(mMessagesRepository::readAllUntil)
        );

        return Arrays.asList(readAllMessages);
    }

}
