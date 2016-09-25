package com.teamagam.gimelgimel.domain.notifications;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.notifications.entity.MessageNotification;

import rx.Observable;
import rx.Subscriber;

/**
 * Sync Interactor for Message notifications based on {@link MessageNotification}
 */
@AutoFactory
public class SyncMessageNotificationInteractor extends SyncInteractor<MessageNotification>{

    private final MessageNotifications mMessageNotifications;

    protected SyncMessageNotificationInteractor(@Provided ThreadExecutor threadExecutor,
                                                @Provided PostExecutionThread postExecutionThread,
                                                @Provided MessageNotifications messageNotifications,
                                                Subscriber<MessageNotification> useCaseSubscriber) {
        super(threadExecutor, postExecutionThread, useCaseSubscriber);
        mMessageNotifications = messageNotifications;
    }

    @Override
    protected Observable<MessageNotification> buildUseCaseObservable() {
        return mMessageNotifications.getNotificationsObservable();
    }
}
