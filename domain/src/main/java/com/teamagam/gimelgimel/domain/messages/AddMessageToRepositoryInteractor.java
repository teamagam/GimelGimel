package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;
import java.util.Collections;
import java.util.Date;
import rx.Observable;

@AutoFactory
public class AddMessageToRepositoryInteractor extends BaseDataInteractor {

  private final MessagesRepository mMessagesRepository;
  private final UnreadMessagesCountRepository mUnreadMessagesCountRepository;
  private final PreferencesUtils mPreferencesUtils;
  private final ChatMessage mMessage;

  public AddMessageToRepositoryInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided MessagesRepository messagesRepository,
      @Provided UnreadMessagesCountRepository unreadMessagesCountRepository,
      @Provided PreferencesUtils preferencesUtils,
      ChatMessage message) {
    super(threadExecutor);
    mMessagesRepository = messagesRepository;
    mUnreadMessagesCountRepository = unreadMessagesCountRepository;
    mPreferencesUtils = preferencesUtils;
    mMessage = message;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest<?> addNewMessage =
        factory.create(Observable.just(mMessage), this::buildObservable);
    return Collections.singletonList(addNewMessage);
  }

  private Observable<?> buildObservable(Observable<ChatMessage> observable) {
    observable = putMessageToRepository(observable);
    return updateUnreadCountRepository(observable);
  }

  private Observable<ChatMessage> putMessageToRepository(Observable<ChatMessage> observable) {
    return observable.doOnNext(mMessagesRepository::putMessage);
  }

  private Observable<?> updateUnreadCountRepository(Observable<ChatMessage> observable) {
    return observable.doOnNext(message -> {
      if (shouldHandleMessageAsUnread(message)) {
        mUnreadMessagesCountRepository.addNewUnreadMessage(message.getCreatedAt());
      }
    });
  }

  private boolean shouldHandleMessageAsUnread(ChatMessage message) {
    return !isFromSelf(message) && !alreadyRead(message);
  }

  private boolean alreadyRead(ChatMessage message) {
    Date currentTimestamp = mUnreadMessagesCountRepository.getLastVisitTimestamp();
    Date messageDate = message.getCreatedAt();
    return currentTimestamp.after(messageDate) || currentTimestamp.equals(messageDate);
  }

  private boolean isFromSelf(ChatMessage message) {
    return mPreferencesUtils.isMessageFromSelf(message.getSenderId());
  }
}
