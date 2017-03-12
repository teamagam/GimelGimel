package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;
import com.teamagam.gimelgimel.domain.utils.MessagesUtil;

import java.util.Collections;
import java.util.Date;

import rx.Observable;

@AutoFactory
public class AddPolledMessageToRepositoryInteractor extends BaseDataInteractor {

    private final MessagesRepository mMessagesRepository;
    private final UnreadMessagesCountRepository mUnreadMessagesCountRepository;
    private final MessagesUtil mMessagesUtil;
    private final Message mMessage;

    public AddPolledMessageToRepositoryInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided MessagesRepository messagesRepository,
            @Provided UnreadMessagesCountRepository unreadMessagesCountRepository,
            @Provided MessagesUtil messagesUtil,
            Message message) {
        super(threadExecutor);
        mMessagesRepository = messagesRepository;
        mUnreadMessagesCountRepository = unreadMessagesCountRepository;
        mMessagesUtil = messagesUtil;
        mMessage = message;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest<?> addNewMessage = factory.create(
                Observable.just(mMessage),
                this::buildObservable);
        return Collections.singletonList(addNewMessage);
    }

    private Observable<?> buildObservable(Observable<Message> observable) {
        observable = putMessageToRepository(observable);
        return updateUnreadCountRepository(observable);
    }

    private Observable<Message> putMessageToRepository(Observable<Message> observable) {
        return observable.doOnNext(mMessagesRepository::putMessage);
    }

    private Observable<?> updateUnreadCountRepository(Observable<Message> observable) {
        return observable
                .doOnNext(message -> {
                    if (shouldHandleMessageAsUnread(message)) {
                        mUnreadMessagesCountRepository.addNewUnreadMessage(message.getCreatedAt());
                    }
                });
    }

    private boolean shouldHandleMessageAsUnread(Message message) {
        return !isFromSelf(message) && !alreadyRead(message);
    }

    private boolean alreadyRead(Message message) {
        Date currentTimestamp = mUnreadMessagesCountRepository.getLastVisitTimestamp();
        Date messageDate = message.getCreatedAt();
        return currentTimestamp.after(messageDate) || currentTimestamp.equals(messageDate);
    }

    private boolean isFromSelf(Message message) {
        return mMessagesUtil.isMessageFromSelf(message);
    }
}
