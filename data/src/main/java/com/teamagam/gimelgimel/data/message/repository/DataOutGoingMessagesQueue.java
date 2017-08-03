package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.OutGoingMessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.MessagesEntityMapper;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import io.reactivex.Observable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DataOutGoingMessagesQueue implements OutGoingMessagesQueue {

  private Queue<ChatMessage> mMessagesQueue;
  private SubjectRepository<ChatMessage> mChatMessageSubject;
  private MessagesEntityMapper mMessagesEntityMapper;
  private OutGoingMessagesDao mOutGoingMessagesDao;

  public DataOutGoingMessagesQueue(OutGoingMessagesDao outGoingMessagesDao,
      MessagesEntityMapper messagesEntityMapper) {
    mMessagesEntityMapper = messagesEntityMapper;
    mOutGoingMessagesDao = outGoingMessagesDao;
    mChatMessageSubject = SubjectRepository.createSimpleSubject();
    mMessagesQueue = convertEntitiesIterable(mOutGoingMessagesDao.getMessagesByDate());
  }

  @Override
  public synchronized void addMessage(ChatMessage chatMessage) {
    mMessagesQueue.add(chatMessage);
    mChatMessageSubject.add(chatMessage);
    //mOutGoingMessagesDao.insertMessage(mMessagesEntityMapper.mapToEntity(chatMessage));
  }

  @Override
  public synchronized ChatMessage getTopMessage() {
    return mMessagesQueue.peek();
  }

  @Override
  public synchronized boolean isEmpty() {
    return mMessagesQueue.isEmpty();
  }

  @Override
  public synchronized void removeTopMessage() {
    mMessagesQueue.poll();
    //todo: make a delete query!
  }

  @Override
  public synchronized Observable<ChatMessage> getObservable() {
    Queue<ChatMessage> chatMessagesQueueCopy = new LinkedList<>(mMessagesQueue);
    return Observable.fromIterable(chatMessagesQueueCopy)
        .mergeWith(mChatMessageSubject.getObservable());
  }

  @Override
  public void switchTopMessageToQueueStart() {
    ChatMessage chatMessage = getTopMessage();
    removeTopMessage();
    addMessage(chatMessage);
  }

  private Queue<ChatMessage> convertEntitiesIterable(List<ChatMessageEntity> chatMessageList) {
    Queue<ChatMessage> chatMessageQueueCopy = new LinkedList<>();
    for (ChatMessageEntity chatMessage : chatMessageList) {
      chatMessageQueueCopy.add(mMessagesEntityMapper.mapToDomain(chatMessage));
    }
    return chatMessageQueueCopy;
  }
}