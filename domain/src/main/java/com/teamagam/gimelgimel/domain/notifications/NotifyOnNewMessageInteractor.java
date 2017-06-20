package com.teamagam.gimelgimel.domain.notifications;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;

@AutoFactory
public class NotifyOnNewMessageInteractor extends BaseSingleDisplayInteractor {

  private final PreferencesUtils mPreferencesUtils;
  private final MessagesRepository mMessagesRepository;
  private final NotificationDisplayer mNotificationDisplayer;

  protected NotifyOnNewMessageInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided PreferencesUtils preferencesUtils,
      @Provided MessagesRepository messagesRepository,
      NotificationDisplayer notificationDisplayer) {
    super(threadExecutor, postExecutionThread);
    mPreferencesUtils = preferencesUtils;
    mMessagesRepository = messagesRepository;
    mNotificationDisplayer = notificationDisplayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.create(mMessagesRepository.getMessagesObservable(),
        messagesObservable -> messagesObservable.filter(this::shouldNotify),
        mNotificationDisplayer::notifyNewMessage);
  }

  private boolean shouldNotify(ChatMessage message) {
    return !mPreferencesUtils.isOnlyAlertsMode() || message.contains(AlertFeature.class);
  }

  public interface NotificationDisplayer {
    void notifyNewMessage(ChatMessage chatMessage);
  }
}
