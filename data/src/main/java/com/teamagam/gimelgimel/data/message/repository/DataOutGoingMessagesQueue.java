package com.teamagam.gimelgimel.data.message.repository;

import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.OutGoingMessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.OutGoingMessagesEntityMapper;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import io.reactivex.Observable;
import java.util.List;
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
    List<OutGoingChatMessage> chatMessagesQueueCopy =
        Lists.transform(mOutGoingMessagesDao.getMessagesByOutGoingId(),
            mMessagesEntityMapper::mapToDomain);
    return Observable.fromIterable(chatMessagesQueueCopy).

        mergeWith(mChatMessageSubject.getObservable());
  }
}