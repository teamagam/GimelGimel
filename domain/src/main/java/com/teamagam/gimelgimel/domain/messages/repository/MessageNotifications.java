package com.teamagam.gimelgimel.domain.messages.repository;

import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.notifications.entity.MessageNotification;

import rx.Observable;

/**
 * Handles message-state notifications.
 * Exposes an observable that emit items whenever a new notification is received
 */
public interface MessageNotifications {

    Observable<MessageNotification> getNotificationsObservable();

    void sending(Message m);

    void success(Message m);

    void error(Message m);
}
