package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.message.repository.cloud.CloudMessagesSource;
import com.teamagam.gimelgimel.data.response.adapters.ServerDataMapper;
import com.teamagam.gimelgimel.domain.messages.cache.MessagesCache;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.ConfirmMessageRead;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import io.reactivex.Observable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MessagesDataRepository implements MessagesRepository {

  private final CloudMessagesSource mSource;
  private final MessagesCache mCache;
  private final SelectedMessageRepository mSelectedRepo;
  @Inject
  ServerDataMapper mServerDataMapper;

  @Inject
  public MessagesDataRepository(CloudMessagesSource cloudMessagesSource,
      MessagesCache messagesCache,
      SelectedMessageRepository selectedMessageRepository) {
    mSource = cloudMessagesSource;
    mCache = messagesCache;
    mSelectedRepo = selectedMessageRepository;
  }

  @Override
  public Observable<ChatMessage> getMessagesObservable() {
    return mCache.getMessages();
  }

  @Override
  public Observable<ChatMessage> getSelectedMessageObservable() {
    return mSelectedRepo.getSelectedMessageObservable();
  }

  @Override
  public ChatMessage getSelectedMessage() {
    return mSelectedRepo.getSelectedMessage();
  }

  @Override
  public void putMessage(ChatMessage message) {
    mCache.insertMessage(message);
  }

  @Override
  public ChatMessage getMessage(String messageId) {
    return mCache.getMessageById(messageId);
  }

  @Override
  public ChatMessage getLastMessage() {
    return mCache.getLastMessage();
  }

  @Override
  public void informReadMessage(ConfirmMessageRead confirm) {
    mSource.informReadMessage(mServerDataMapper.transformToData(confirm));
  }

  @Override
  public void selectMessage(ChatMessage message) {
    mSelectedRepo.select(message);
  }
}