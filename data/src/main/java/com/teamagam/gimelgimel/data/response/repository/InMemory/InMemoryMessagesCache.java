package com.teamagam.gimelgimel.data.response.repository.InMemory;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import io.reactivex.Observable;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InMemoryMessagesCache {

  private final Map<String, ChatMessage> mMessagesById;
  private final SubjectRepository<ChatMessage> mMessagesReplayRepo;
  private final SubjectRepository<Integer> mNumMessagesRepo;
  private ChatMessage mLastMessage;
  private int mNumMessages;

  @Inject
  InMemoryMessagesCache() {
    mMessagesById = new HashMap<>();

    mMessagesReplayRepo = SubjectRepository.createReplayAll();
    mNumMessagesRepo = SubjectRepository.createReplayCount(1);

    mLastMessage = null;

    mNumMessages = 0;
    mNumMessagesRepo.add(mNumMessages);
  }

  public void addMessage(ChatMessage message) {
    mMessagesById.put(message.getMessageId(), message);
    mNumMessagesRepo.add(++mNumMessages);
    mMessagesReplayRepo.add(message);
    updateLastMessage(message);
  }

  public ChatMessage getMessageById(String id) {
    if (mMessagesById.containsKey(id)) {
      return mMessagesById.get(id);
    }
    return null;
  }

  public ChatMessage getLastMessage() {
    return mLastMessage;
  }

  public Observable<ChatMessage> getMessagesObservable() {
    return mMessagesReplayRepo.getObservable();
  }

  public Observable<Integer> getNumMessagesObservable() {
    return mNumMessagesRepo.getObservable();
  }

  private void updateLastMessage(ChatMessage message) {
    if (isOlderThanLast(message)) {
      mLastMessage = message;
    }
  }

  private boolean isOlderThanLast(ChatMessage message) {
    return mLastMessage == null || message.getCreatedAt().getTime() > mLastMessage.getCreatedAt()
        .getTime();
  }
}
