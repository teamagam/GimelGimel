package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesContainerStateRepository;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;
import com.teamagam.gimelgimel.domain.utils.MessagesUtil;

import java.util.Collections;

import rx.Observable;

@AutoFactory
public class AddPolledMessageToRepositoryInteractor extends BaseDataInteractor {

    private final MessagesRepository mMessagesRepository;
    private final UnreadMessagesCountRepository mUnreadMessagesCountRepository;
    private final MessagesContainerStateRepository mMessagesContainerStateRepository;
    private final MessagesUtil mMessagesUtil;
    private final Message mMessage;

    public AddPolledMessageToRepositoryInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided MessagesRepository messagesRepository,
            @Provided UnreadMessagesCountRepository unreadMessagesCountRepository,
            @Provided MessagesContainerStateRepository messagesContainerStateRepository,
            @Provided MessagesUtil messagesUtil,
            Message message) {
        super(threadExecutor);
        mMessagesRepository = messagesRepository;
        mUnreadMessagesCountRepository = unreadMessagesCountRepository;
        mMessagesContainerStateRepository = messagesContainerStateRepository;
        mMessagesUtil = messagesUtil;
        mMessage = message;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest addNewMessage = factory.create(buildObservable());
        return Collections.singletonList(addNewMessage);
    }

    private Observable<Boolean> buildObservable() {
        Observable<Message> observable = Observable.just(mMessage);
        observable = putMessageToRepository(observable);
        return updateUnreadCountRepository(observable);
    }

    private Observable<Message> putMessageToRepository(Observable<Message> observable) {
        return observable.doOnNext(mMessagesRepository::putMessage);
    }

    private Observable<Boolean> updateUnreadCountRepository(Observable<Message> observable) {
        return observable.flatMap(this::shouldHandleMessageAsUnread)
                .filter(shouldHandleAsUnread -> shouldHandleAsUnread)
                .doOnNext(shouldHandleAsUnread -> mUnreadMessagesCountRepository.addNewUnreadMessage());
    }

    private Observable<Boolean> shouldHandleMessageAsUnread(Message message) {
        return mMessagesContainerStateRepository.getState()
                .map(state -> state == MessagesContainerStateRepository.ContainerState.INVISIBLE
                        && !mMessagesUtil.isMessageFromSelf(message));
    }
}
