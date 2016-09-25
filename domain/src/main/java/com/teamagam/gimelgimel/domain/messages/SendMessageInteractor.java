package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import rx.Observable;

/**
 * Basic message sending interactor logic
 */
public abstract class SendMessageInteractor<T extends Message> extends CreateMessageInteractor<T> {

    private final MessagesRepository mMessagesRepository;
    private final MessageNotifications mMessageNotifications;

    protected SendMessageInteractor(
            ThreadExecutor threadExecutor,
            UserPreferencesRepository userPreferences,
            MessagesRepository messagesRepository,
            MessageNotifications messageNotifications) {
        super(threadExecutor, userPreferences);
        mMessagesRepository = messagesRepository;
        mMessageNotifications = messageNotifications;
    }


    @Override
    protected Observable<T> buildUseCaseObservable() {
        return super.buildUseCaseObservable()
                .doOnNext(mMessageNotifications::sending)
                .flatMap(mMessagesRepository::sendMessage)
                .doOnNext(mMessagesRepository::putMessage)
                .doOnNext(mMessageNotifications::success)
                .map(m -> (T) m)
                .doOnError(t -> {
                    if (getMessage() != null) {
                        mMessageNotifications.error(getMessage());
                    }
                });
    }
}
