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
        mSelectedMessageRepo = ReplayRepository.createReplayCount(1);
        mCurrentlySelected = null;
    }

    public Observable<Message> getSelectedMessageObservable() {
        return mSelectedMessageRepo.getObservable();
    }

    public void select(Message message) {
        if (!isCurrentlySelected(message)) {
            mCurrentlySelected = message;
            mSelectedMessageRepo.add(message);
        }
    }

    private boolean isCurrentlySelected(Message message) {
        return mCurrentlySelected != null && message.equals(mCurrentlySelected);
    }
}
