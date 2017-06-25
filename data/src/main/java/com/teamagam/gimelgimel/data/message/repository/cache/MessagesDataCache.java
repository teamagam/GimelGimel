package com.teamagam.gimelgimel.data.message.repository.cache;

import com.teamagam.gimelgimel.data.message.repository.cache.room.MessagesEntityConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.domain.messages.cache.MessagesCache;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import io.reactivex.Observable;

public class MessagesDataCache implements MessagesCache {

  private MessagesDao mDao;
  private MessagesEntityConverter mConverter;

  public MessagesDataCache(MessagesDao dao, MessagesEntityConverter converter) {
    mDao = dao;
    mConverter = converter;
  }

  @Override
  public Observable<ChatMessage> getMessages() {
    return mDao.getMessages().map(mConverter::convertToDomain).toObservable();
  }

  @Override
  public void insertMessage(ChatMessage message) {
    ChatMessageEntity entity = mConverter.convertToEntity(message);

    mDao.insertMessage(entity);
  }

  @Override
  public ChatMessage getMessageById(String messageId) {
    return mConverter.convertToDomain(mDao.getMessageById(messageId));
  }

  @Override
  public ChatMessage getLastMessage() {
    return mConverter.convertToDomain(mDao.getLastMessage());
  }
}
