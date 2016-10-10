package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import android.databinding.BaseObservable;

import com.teamagam.gimelgimel.app.common.DataChangedObservable;
import com.teamagam.gimelgimel.app.common.DataChangedObserver;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
import com.teamagam.gimelgimel.app.viewModels.ViewModel;

import java.util.Date;

/**
 * Shared functionality of selected message view-model.
 * Verifies that a message is selected before querying for any selected-message data.
 * Notifies observers on data changed events if an abstract condition is met (implemented by extending classes)
 */
abstract class SelectedMessageViewModel extends BaseObservable implements
        ViewModel{

    protected final SelectedMessageModel mSelectedMessageModel;

    public SelectedMessageViewModel(SelectedMessageModel selectedMessageModel) {
        mSelectedMessageModel = selectedMessageModel;
        SelectedMessageModelDataChangedObserver mSelectedMessageChangedObserver = new SelectedMessageModelDataChangedObserver();
        mSelectedMessageModel.addObserver(mSelectedMessageChangedObserver);
    }

    public SelectedMessageViewModel() {

    }

    public String getType() {
        validateSelectedMessage();
        return getSelectedMessage().getType();
    }

    public String getSenderId() {
        validateSelectedMessage();
        return getSelectedMessage().getSenderId();
    }

    public Date getDate() {
        validateSelectedMessage();
        return getSelectedMessage().getCreatedAt();
    }

    protected void validateSelectedMessage() {
        if (!mSelectedMessageModel.isAnySelected()) {
            throw new NoSelectedMessageException();
        }
    }

    protected abstract boolean shouldNotifyOnSelectedMessageModelChange();

    private Message getSelectedMessage() {
        return mSelectedMessageModel.getSelected();
    }

    static class NoSelectedMessageException extends RuntimeException {
    }

    private class SelectedMessageModelDataChangedObserver implements DataChangedObserver {
        @Override
        public void onDataChanged() {
            if (shouldNotifyOnSelectedMessageModelChange()) {
                SelectedMessageViewModel.this.notifyObservers();
            }
        }
    }
}
