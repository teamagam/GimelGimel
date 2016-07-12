package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.common.DataChangedObservable;
import com.teamagam.gimelgimel.app.common.DataChangedObserver;
import com.teamagam.gimelgimel.app.common.NotifyingDataChangedObservable;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;

import java.util.Date;

/**
 * Shared functionality of selected message view-model.
 * Verifies that a message is selected before querying for any selected-message data.
 * Notifies observers on data changed events if an abstract condition is met (implemented by extending classes)
 */
abstract class SelectedMessageViewModel extends NotifyingDataChangedObservable implements DataChangedObservable {

    protected final SelectedMessageModel mSelectedMessageModel;

    public SelectedMessageViewModel(SelectedMessageModel selectedMessageModel) {
        mSelectedMessageModel = selectedMessageModel;
        DelegatingDataChangedObserver mSelectedMessageChangedObserver = new DelegatingDataChangedObserver();
        mSelectedMessageModel.addObserver(mSelectedMessageChangedObserver);
    }

    public String getType() {
        validateSelectedMessage();
        return mSelectedMessageModel.getSelected().getType();
    }

    public String getSenderId() {
        validateSelectedMessage();
        return mSelectedMessageModel.getSelected().getSenderId();
    }

    public Date getDate() {
        validateSelectedMessage();
        return mSelectedMessageModel.getSelected().getCreatedAt();
    }

    protected void validateSelectedMessage() {
        if (!mSelectedMessageModel.isAnySelected()) {
            throw new NoSelectedMessageException();
        }
    }

    protected abstract boolean shouldNotifyObservers();

    public static class NoSelectedMessageException extends RuntimeException {
    }

    private class DelegatingDataChangedObserver implements DataChangedObserver {
        @Override
        public void onDataChanged() {
            if (shouldNotifyObservers()) {
                SelectedMessageViewModel.this.notifyObservers();
            }
        }
    }
}
