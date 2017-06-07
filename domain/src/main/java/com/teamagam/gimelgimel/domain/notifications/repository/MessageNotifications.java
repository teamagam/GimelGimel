package com.teamagam.gimelgimel.domain.notifications.repository;

import com.teamagam.gimelgimel.domain.notifications.entity.MessageNotification;
import io.reactivex.Observable;

public interface MessageNotifications {

  Observable<MessageNotification> getNotificationsObservable();

  void sending();

  void success();

  void error();
}
