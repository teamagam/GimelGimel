package com.teamagam.gimelgimel.data.message.repository.cache;

import com.teamagam.gimelgimel.data.message.repository.cache.room.MessagesEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.domain.messages.cache.MessagesCache;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import io.reactivex.Observable;

public class MessagesDataCache implements MessagesCache {

  private MessagesDao mDao;
  private MessagesEntityMapper mMapper;

  public MessagesDataCache(MessagesDao dao, MessagesEntityMapper mapper) {
    mDao = dao;
    mMapper = mapper;
  }

  @Override
  public Observable<ChatMessage> getMessages() {
    return mDao.getMessages().map(mMapper::convertToDomain).toObservable();
  }

  @Override
  public void insertMessage(ChatMessage message) {
    ChatMessageEntity entity = mMapper.convertToEntity(message);

    mDao.insertMessage(entity);
  }

  @Override
  public ChatMessage getMessageById(String messageId) {
    return mMapper.convertToDomain(mDao.getMessageById(messageId));
  }

  @Override
  public ChatMessage getLastMessage() {
    return mMapper.convertToDomain(mDao.getLastMessage());
  }
}
