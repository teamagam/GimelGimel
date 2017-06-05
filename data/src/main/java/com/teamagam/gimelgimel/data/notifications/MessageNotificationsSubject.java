package com.teamagam.gimelgimel.data.notifications;

import com.teamagam.gimelgimel.domain.notifications.entity.MessageNotification;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.utils.SerializedSubjectBuilder;
import io.reactivex.subjects.Subject;
import javax.inject.Inject;
import io.reactivex.Observable;

public class MessageNotificationsSubject implements MessageNotifications {

  private Subject<MessageNotification> mSubject;

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
