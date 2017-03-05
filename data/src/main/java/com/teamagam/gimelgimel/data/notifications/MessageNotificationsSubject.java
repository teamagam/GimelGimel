package com.teamagam.gimelgimel.data.notifications;

import com.teamagam.gimelgimel.domain.notifications.entity.MessageNotification;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.utils.SerializedSubjectBuilder;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.SerializedSubject;

/**
 * {@link rx.subjects.PublishSubject} to emit new items when they arrive.
 * There is no replay of the last item.
 * To do that you can use {@link rx.subjects.ReplaySubject).createWithSize(1).
 */
public class MessageNotificationsSubject implements MessageNotifications {

    private SerializedSubject<MessageNotification, MessageNotification> mSubject;

    @Inject
    public MessageNotificationsSubject() {
        mSubject = new SerializedSubjectBuilder().build();
    }

    @Override
    public Observable<MessageNotification> getNotificationsObservable() {
        return mSubject;
    }

    @Override
    public void sending() {
        mSubject.onNext(MessageNotification.createSendingNotification());
    }

    @Override
    public void success() {
        mSubject.onNext(MessageNotification.createSuccessNotification());
    }

    @Override
    public void error() {
        mSubject.onNext(MessageNotification.createErrorNotification());
    }
}
