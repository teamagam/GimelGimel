package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;

import java.util.Collections;
import java.util.Date;

import rx.Observable;

@AutoFactory
public class UpdateMessagesReadInteractor extends BaseDataInteractor {

    private final UnreadMessagesCountRepository mUnreadMessagesCountRepository;

    public UpdateMessagesReadInteractor(@Provided ThreadExecutor threadExecutor,
                                        @Provided UnreadMessagesCountRepository unreadMessagesCountRepository) {
        super(threadExecutor);
        mUnreadMessagesCountRepository = unreadMessagesCountRepository;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest readAllMessages = factory.create(
                Observable.just(new Date()).doOnNext(mUnreadMessagesCountRepository::readAllUntil)
        );

        return Collections.singletonList(readAllMessages);
    }

}
