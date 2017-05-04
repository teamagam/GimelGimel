package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class SelectedMessageRepository {

    private final ReplayRepository<Message> mSelectedMessageRepo;
    private Message mCurrentlySelected;

    @Inject
    public SelectedMessageRepository() {
        mSelectedMessageRepo = ReplayRepository.createReplayCount(0);
        mCurrentlySelected = null;
    }

    public Observable<Message> getSelectedMessageObservable() {
        return mSelectedMessageRepo.getObservable();
    }

    public Message getSelectedMessage() {
        return mCurrentlySelected;
    }

    public void select(Message message) {
        mSelectedMessageRepo.add(message);
        mCurrentlySelected = message;
    }
}
