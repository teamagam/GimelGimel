package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.base.repository.SingleReplayRepository;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class SelectedMessageRepository {

    private final SingleReplayRepository<Message> mSelectedMessageRepo;
    private Message mCurrentlySelected;

    @Inject
    public SelectedMessageRepository() {
        mSelectedMessageRepo = new SingleReplayRepository<>();
        mCurrentlySelected = null;
    }

    public Observable<Message> getSelectedMessageObservable() {
        return mSelectedMessageRepo.getObservable();
    }

    public void select(Message message) {
        if (!isCurrentlySelected(message)) {
            mCurrentlySelected = message;
            mSelectedMessageRepo.setValue(message);
        }
    }

    private boolean isCurrentlySelected(Message message) {
        return mCurrentlySelected != null && message.equals(mCurrentlySelected);
    }
}
