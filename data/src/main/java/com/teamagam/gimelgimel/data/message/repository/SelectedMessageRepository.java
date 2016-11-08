package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.domain.messages.entity.Message;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;

@Singleton
public class SelectedMessageRepository {

    private final PublishSubject<Message> mSelectedSubject;
    private final Observable<Message> mSelectedObservable;
    private Message mCurrentlySelected;

    @Inject
    public SelectedMessageRepository() {
        mSelectedSubject = PublishSubject.create();
        mSelectedObservable = mSelectedSubject.share().replay(1).autoConnect();
        mCurrentlySelected = null;
    }

    public Observable<Message> getSelectedMessageObservable() {
        return mSelectedObservable;
    }

    public void select(Message message) {
        if (!isCurrentlySelected(message)) {
            mCurrentlySelected = message;
            mSelectedSubject.onNext(message);
        }
    }

    private boolean isCurrentlySelected(Message message) {
        return mCurrentlySelected != null && message.equals(mCurrentlySelected);
    }
}
