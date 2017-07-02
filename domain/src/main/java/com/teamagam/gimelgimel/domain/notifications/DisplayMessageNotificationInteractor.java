package com.teamagam.gimelgimel.domain.notifications;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.notifications.entity.MessageNotification;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;

@AutoFactory
public class DisplayMessageNotificationInteractor extends BaseSingleDisplayInteractor {

  private final MessageNotifications mMessageNotifications;
  private final Displayer mDisplayer;

  protected DisplayMessageNotificationInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided MessageNotifications messageNotifications,
      Displayer displayer) {
    super(threadExecutor, postExecutionThread);
    mMessageNotifications = messageNotifications;
    mDisplayer = displayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.createSimple(mMessageNotifications.getNotificationsObservable(), this::display);
  }

  private void display(MessageNotification messageNotification) {
    switch (messageNotification.getState()) {
      case MessageNotification.SENDING:
        mDisplayer.displaySending();
        break;
      case MessageNotification.SUCCESS:
        mDisplayer.displaySent();
        break;
      case MessageNotification.ERROR:
        mDisplayer.displayError();
        break;
    }
  }

  public interface Displayer {

    void displaySending();

    void displaySent();

    void displayError();
  }
}
