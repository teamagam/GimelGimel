package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import io.reactivex.Observable;
import java.util.Collections;

public abstract class QueueMessageForSendingInteractor extends BaseDataInteractor {

  private final UserPreferencesRepository mUserPreferencesRepository;
  private OutGoingMessagesQueue mMessageQueue;

  public QueueMessageForSendingInteractor(ThreadExecutor threadExecutor,
      UserPreferencesRepository userPreferencesRepository,
      OutGoingMessagesQueue outGoingMessagesQueue) {
    super(threadExecutor);
    mUserPreferencesRepository = userPreferencesRepository;
    mMessageQueue = outGoingMessagesQueue;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest subscriptionRequest = factory.create(Observable.just(Constants.SIGNAL),
        objectObservable -> objectObservable.map(x -> createMessage())
            .doOnNext(mMessageQueue::addMessage));
    return Collections.singletonList(subscriptionRequest);
  }

  protected abstract OutGoingChatMessage createMessage(String senderId);

  private OutGoingChatMessage createMessage() {
    return createMessage(getSenderId());
  }

  private String getSenderId() {
    return mUserPreferencesRepository.getString(Constants.USERNAME_PREFERENCE_KEY);
  }
}