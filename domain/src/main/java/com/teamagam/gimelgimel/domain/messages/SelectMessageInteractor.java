package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import java.util.Collections;
import io.reactivex.Observable;

@AutoFactory
public class SelectMessageInteractor extends BaseDataInteractor {

  private static final Logger sLogger =
      LoggerFactory.create(SelectMessageInteractor.class.getSimpleName());

  private final MessagesRepository mMessagesRepository;
  private final String mMessageId;

  protected SelectMessageInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided MessagesRepository messagesRepository,
      String messageId) {
    super(threadExecutor);
    mMessagesRepository = messagesRepository;
    mMessageId = messageId;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return Collections.singletonList(buildSelectMessageRequest(factory));
  }

  private DataSubscriptionRequest buildSelectMessageRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mMessageId),
        messageIdObservable -> messageIdObservable.map(mMessagesRepository::getMessage)
            .doOnNext(this::select));
  }

  private void select(ChatMessage message) {
    if (message == null) {
      sLogger.w("No related message.");
    } else {
      mMessagesRepository.selectMessage(message);
    }
  }
}