package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import java.util.Collections;
import rx.Observable;

public abstract class SendMessageInteractor extends BaseDataInteractor {

  private final UserPreferencesRepository mUserPreferencesRepository;
  private final MessageNotifications mMessageNotifications;
  private final MessagesRepository mMessagesRepository;

  public SendMessageInteractor(ThreadExecutor threadExecutor,
      UserPreferencesRepository userPreferencesRepository,
      MessageNotifications messageNotifications,
      MessagesRepository messagesRepository) {
    super(threadExecutor);
    mUserPreferencesRepository = userPreferencesRepository;
    mMessageNotifications = messageNotifications;
    mMessagesRepository = messagesRepository;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {

    DataSubscriptionRequest subscriptionRequest = factory.create(Observable.just(null),
        objectObservable -> objectObservable.map(x -> createMessage())
            .doOnNext(m -> mMessageNotifications.sending())
            .flatMap(mMessagesRepository::sendMessage)
            .doOnNext(m -> mMessageNotifications.success())
            .doOnError(t -> mMessageNotifications.error())

    );
    return Collections.singletonList(subscriptionRequest);
  }

  protected abstract ChatMessage createMessage(String senderId);

  private ChatMessage createMessage() {
    return createMessage(getSenderId());
  }

  private String getSenderId() {
    return mUserPreferencesRepository.getString(Constants.USERNAME_PREFERENCE_KEY);
  }
}