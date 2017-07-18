package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.message.repository.cloud.CloudMessagesSource;
import com.teamagam.gimelgimel.data.response.adapters.ServerDataMapper;
import com.teamagam.gimelgimel.domain.messages.MessageSender;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import io.reactivex.Observable;
import javax.inject.Inject;

public class MessageSenderData implements MessageSender {

  private final CloudMessagesSource mSource;
  private ServerDataMapper mServerDataMapper;

  @Inject
  public MessageSenderData(CloudMessagesSource mSource, ServerDataMapper serverDataMapper) {
    this.mSource = mSource;
    this.mServerDataMapper = serverDataMapper;
  }

  @Override
  public Observable<ChatMessage> sendMessage(ChatMessage message) {
    return mSource.sendMessage(mServerDataMapper.transformToData(message))
        .map(mServerDataMapper::transform);
  }
}
