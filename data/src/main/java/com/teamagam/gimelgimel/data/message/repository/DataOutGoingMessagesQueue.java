package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.OutGoingMessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.OutGoingChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.OutGoingMessagesEntityMapper;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import io.reactivex.Observable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.inject.Inject;

public class DataOutGoingMessagesQueue implements OutGoingMessagesQueue {

  private SubjectRepository<OutGoingChatMessage> mChatMessageSubject;
  private OutGoingMessagesEntityMapper mMessagesEntityMapper;
  private OutGoingMessagesDao mOutGoingMessagesDao;

  @Inject
  public DataOutGoingMessagesQueue(OutGoingMessagesDao outGoingMessagesDao,
      OutGoingMessagesEntityMapper messagesEntityMapper) {
    mMessagesEntityMapper = messagesEntityMapper;
    mOutGoingMessagesDao = outGoingMessagesDao;
    mChatMessageSubject = SubjectRepository.createSimpleSubject();
  }

  @Override
  public synchronized void addMessage(OutGoingChatMessage outGoingChatMessage) {
    mOutGoingMessagesDao.insertMessage(mMessagesEntityMapper.mapToEntity(outGoingChatMessage));
    mChatMessageSubject.add(outGoingChatMessage);
  }

  @Override
  public synchronized OutGoingChatMessage getTopMessage() {
    return mMessagesEntityMapper.mapToDomain(mOutGoingMessagesDao.getTopMessage());
  }

  @Override
  public synchronized void removeTopMessage() {
    mOutGoingMessagesDao.deleteTopMessage();
  }

  @Override
  public synchronized Observable<OutGoingChatMessage> getOutGoingChatMessagesObservable() {
    Queue<OutGoingChatMessage> chatMessagesQueueCopy =
        convertEntitiesIterable(mOutGoingMessagesDao.getMessagesByOutGoingId());
    return Observable.fromIterable(chatMessagesQueueCopy)
        .mergeWith(mChatMessageSubject.getObservable());
  }

  private Queue<OutGoingChatMessage> convertEntitiesIterable(List<OutGoingChatMessageEntity> outGoingChatMessageEntityList) {
    Queue<OutGoingChatMessage> outGoingChatMessageQueueCopy = new LinkedList<>();
    for (OutGoingChatMessageEntity outGoingChatMessage : outGoingChatMessageEntityList) {
      outGoingChatMessageQueueCopy.add(mMessagesEntityMapper.mapToDomain(outGoingChatMessage));
    }
    return outGoingChatMessageQueueCopy;
  }
}