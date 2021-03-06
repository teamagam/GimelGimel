package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import io.reactivex.Observable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SelectedMessageRepository {

  private final SubjectRepository<ChatMessage> mSelectedMessageRepo;
  private ChatMessage mCurrentlySelected;

  @Inject
  public SelectedMessageRepository() {
    mSelectedMessageRepo = SubjectRepository.createSimpleSubject();
    mCurrentlySelected = null;
  }

  public Observable<ChatMessage> getSelectedMessageObservable() {
    return mSelectedMessageRepo.getObservable();
  }

  public ChatMessage getSelectedMessage() {
    return mCurrentlySelected;
  }

  public void select(ChatMessage message) {
    mSelectedMessageRepo.add(message);
    mCurrentlySelected = message;
  }
}
