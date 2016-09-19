package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import rx.Observable;

/**
 * Basic message sending interactor logic
 */
public abstract class SendMessageInteractor<T extends Message> extends CreateMessageInteractor<T> {

    private final MessagesRepository mMessagesRepository;

    protected SendMessageInteractor(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            UserPreferencesRepository userPreferences,
            MessagesRepository messagesRepository) {
        super(threadExecutor, postExecutionThread, userPreferences);
        mMessagesRepository = messagesRepository;
    }


    @Override
    protected Observable<T> buildUseCaseObservable() {
        return super.buildUseCaseObservable()
                .flatMap(mMessagesRepository::sendMessage)
                .doOnNext(mMessagesRepository::putMessage)
                .map(m -> (T) m);
    }
}
