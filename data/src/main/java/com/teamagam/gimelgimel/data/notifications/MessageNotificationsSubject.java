package com.teamagam.gimelgimel.data.notifications;

import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.notifications.entity.MessageNotification;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * {@link rx.subjects.PublishSubject} to emit new items when they arrive.
 * There is no replay of the last item.
 * To do that you can use {@link rx.subjects.ReplaySubject).createWithSize(1).
 */
public class MessageNotificationsSubject implements MessageNotifications {

    private PublishSubject<MessageNotification> mSubject;

    @Inject
    public MessageNotificationsSubject() {
        mSubject = PublishSubject.create();
    }


    @Override
    public Observable<MessageNotification> getNotificationsObservable() {
        return mSubject;
    }

    @Override
    public void sending(Message m) {
        mSubject.onNext(MessageNotification.createSendingNotification(m));

    }

    @Override
    public void success(Message m) {
        mSubject.onNext(MessageNotification.createSuccessNotification(m));
    }

    @Override
    public void error(Message m) {
        mSubject.onNext(MessageNotification.createErrorNotification(m));
    }

}
