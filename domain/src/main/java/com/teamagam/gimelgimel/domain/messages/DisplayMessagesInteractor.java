package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import java.util.Arrays;

@AutoFactory
public class DisplayMessagesInteractor extends BaseInteractor {

    private final Displayer mDisplayer;
    private final MessagesRepository mMessagesRepository;


    protected DisplayMessagesInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided MessagesRepository messagesRepository,
            Displayer displayer) {
        super(threadExecutor, postExecutionThread);
        mMessagesRepository = messagesRepository;
        mDisplayer = displayer;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests() {
        SubscriptionRequest<Message> displayMessages = new SubscriptionRequest<>(
                mMessagesRepository.getMessagesObservable(),
                mDisplayer::show);

        SubscriptionRequest<Message> displayRead = new SubscriptionRequest<>(
                mMessagesRepository.getReadMessagesObservable(),
                mDisplayer::read
        );

        SubscriptionRequest<Message> displaySelected = new SubscriptionRequest<>(
                mMessagesRepository.getSelectedMessageObservable(),
                mDisplayer::select);

        return Arrays.asList(displayMessages, displayRead, displaySelected);
    }


    public interface Displayer {
        void show(Message message);

        void read(Message message);

        void select(Message message);
    }
}
