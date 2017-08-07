package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.OutGoingMessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.OutGoingChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.MessagesEntityMapper;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import io.reactivex.Observable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DataOutGoingMessagesQueue implements OutGoingMessagesQueue {

  private Queue<OutGoingChatMessage> mMessagesQueue;
  private SubjectRepository<OutGoingChatMessage> mChatMessageSubject;
  private MessagesEntityMapper mMessagesEntityMapper;
  private OutGoingMessagesDao mOutGoingMessagesDao;

  public DataOutGoingMessagesQueue(OutGoingMessagesDao outGoingMessagesDao,
      MessagesEntityMapper messagesEntityMapper) {
    mMessagesEntityMapper = messagesEntityMapper;
    mOutGoingMessagesDao = outGoingMessagesDao;
    mChatMessageSubject = SubjectRepository.createSimpleSubject();
    mMessagesQueue = convertEntitiesIterable(mOutGoingMessagesDao.getMessagesByOutGoingId());
  }

  @Override
  public synchronized void addMessage(OutGoingChatMessage outGoingChatMessage) {
    mMessagesQueue.add(outGoingChatMessage);
    mChatMessageSubject.add(outGoingChatMessage);
    // TODO: 07/08/2017 make sure this is working!
    mOutGoingMessagesDao.insertMessage(mMessagesEntityMapper.mapToEntity(outGoingChatMessage));
  }

  @Override
  public synchronized OutGoingChatMessage getTopMessage() {
    return mMessagesQueue.peek();
  }

  @Override
  public synchronized boolean isEmpty() {
    return mMessagesQueue.isEmpty();
  }

  @Override
  public synchronized void removeTopMessage() {
    mMessagesQueue.poll();
    //// TODO: 07/08/2017 make sure this is working!
    mOutGoingMessagesDao.deleteTopMessage();
  }

  @Override
  public synchronized Observable<OutGoingChatMessage> getObservable() {
    Queue<OutGoingChatMessage> chatMessagesQueueCopy = new LinkedList<>(mMessagesQueue);
    return Observable.fromIterable(chatMessagesQueueCopy)
        .mergeWith(mChatMessageSubject.getObservable());
  }

  @Override
  public void switchTopMessageToQueueStart() {
    OutGoingChatMessage outGoingChatMessage = getTopMessage();
    removeTopMessage();
    addMessage(outGoingChatMessage);
  }

  private Queue<OutGoingChatMessage> convertEntitiesIterable(List<OutGoingChatMessageEntity> outGoingChatMessageEntityList) {
    Queue<OutGoingChatMessage> outGoingChatMessageQueueCopy = new LinkedList<>();
    for (OutGoingChatMessageEntity outGoingChatMessage : outGoingChatMessageEntityList) {
      outGoingChatMessageQueueCopy.add(mMessagesEntityMapper.mapToDomain(outGoingChatMessage));
    }
    return outGoingChatMessageQueueCopy;
  }
}