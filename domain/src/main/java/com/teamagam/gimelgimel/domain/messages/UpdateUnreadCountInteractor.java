package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;
import io.reactivex.Observable;
import java.util.Collections;
import java.util.Date;
import javax.inject.Inject;

public class UpdateUnreadCountInteractor extends BaseDataInteractor {

  private final MessagesRepository mMessagesRepository;
  private final UnreadMessagesCountRepository mUnreadMessagesCountRepository;
  private final PreferencesUtils mPreferencesUtils;

  @Inject
  public UpdateUnreadCountInteractor(ThreadExecutor threadExecutor,
      MessagesRepository messagesRepository,
      UnreadMessagesCountRepository unreadMessagesCountRepository,
      PreferencesUtils preferencesUtils) {
    super(threadExecutor);
    mMessagesRepository = messagesRepository;
    mUnreadMessagesCountRepository = unreadMessagesCountRepository;
    mPreferencesUtils = preferencesUtils;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest<?> addNewMessage =
        factory.create(mMessagesRepository.getMessagesObservable(),
            this::updateUnreadCountRepository);
    return Collections.singletonList(addNewMessage);
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
    return mPreferencesUtils.isSelf(message.getSenderId());
  }
}
