package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;

import java.util.Collections;

import rx.Observable;

@AutoFactory
public class AddPolledMessageToRepositoryInteractor extends BaseDataInteractor {

    private final MessagesRepository mMessagesRepository;
    private final UnreadMessagesCountRepository mUnreadMessagesCountRepository;
    private final Message mMessage;

    public AddPolledMessageToRepositoryInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided MessagesRepository messagesRepository,
            @Provided UnreadMessagesCountRepository unreadMessagesCountRepository,
            Message message) {
        super(threadExecutor);
        mMessagesRepository = messagesRepository;
        mUnreadMessagesCountRepository = unreadMessagesCountRepository;
        mMessage = message;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest addNewMessage = factory.create(
                Observable.just(mMessage)
                        .doOnNext(mMessagesRepository::putMessage)
                        .filter(message -> !isMessagesContainerVisible())
                        .doOnNext(message -> mUnreadMessagesCountRepository.addNewUnreadMessage())
        );

        return Collections.singletonList(addNewMessage);

    }

    private boolean isMessagesContainerVisible() {
        return false;
    }
}
