package com.teamagam.gimelgimel.domain.notifications.repository;

import com.teamagam.gimelgimel.domain.notifications.entity.MessageNotification;
import rx.Observable;

/**
 * Handles message-state notifications.
 * Exposes an observable that emit items whenever a new notification is received
 */
public interface MessageNotifications {

  Observable<MessageNotification> getNotificationsObservable();

  void sending();

  void success();

  void error();
}
