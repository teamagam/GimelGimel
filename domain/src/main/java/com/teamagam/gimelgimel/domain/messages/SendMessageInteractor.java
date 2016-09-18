package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.AbstractInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.interfaces.UserPreferences;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;

/**
 * Basic message sending interactor logic
 */
public abstract class SendMessageInteractor<T extends Message> extends AbstractInteractor<T> {

    private final UserPreferences mUserPreferences;
    private final MessagesRepository mMessagesRepository;

    protected SendMessageInteractor(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            UserPreferences userPreferences,
            MessagesRepository messagesRepository) {
        super(threadExecutor, postExecutionThread);
        mUserPreferences = userPreferences;
        mMessagesRepository = messagesRepository;
    }

    @Override
    protected Observable<T> buildUseCaseObservable() {
        T message = createMessage(mUserPreferences.getSenderName());

        return mMessagesRepository.sendMessage(message)
                .doOnNext(mMessagesRepository::putMessage)
                .map(m -> (T) m);
    }

    protected abstract T createMessage(String senderId);
}
