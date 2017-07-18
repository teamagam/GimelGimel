package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

@AutoFactory
public class QueueTextMessageForSendingInteractor extends QueueMessageForSendingInteractor {

  private String mText;

  protected QueueTextMessageForSendingInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided UserPreferencesRepository userPreferences,
      @Provided OutGoingMessagesQueue outGoingMessagesQueue,
      String text) {
    super(threadExecutor, userPreferences, outGoingMessagesQueue);
    mText = text;
  }

  @Override
  protected ChatMessage createMessage(String senderId) {
    return new ChatMessage(null, senderId, null, new TextFeature(mText.trim()));
  }
}