package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.common.DataChangedObservable;
import com.teamagam.gimelgimel.app.common.DataChangedObserver;
import com.teamagam.gimelgimel.app.common.NotifyingDataChangedObservable;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;

import java.util.Date;

/**
 * Exposes basic message details
 */
abstract class MessageDetailViewModel extends NotifyingDataChangedObservable implements DataChangedObservable {

    protected final SelectedMessageModel mSelectedMessageModel;
    private final DataChangedObserver mSelectedMessageChangedObserver;

    public MessageDetailViewModel(SelectedMessageModel selectedMessageModel) {
        mSelectedMessageModel = selectedMessageModel;
        mSelectedMessageChangedObserver = new DelegatingDataChangedObserver();
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
        if (!isSelectedMessageOfType(getExpectedMessageType())) {
            throw new IncompatibleMessageType();
        }
    }

    protected abstract
    @Message.MessageType
    String getExpectedMessageType();

    private boolean shouldNotifyObservers() {
        return isSelectedMessageOfType(getExpectedMessageType());
    }

    private boolean isSelectedMessageOfType(@Message.MessageType String messageType) {
        return mSelectedMessageModel.getSelected().getType().equals(messageType);
    }

    public class NoSelectedMessageException extends RuntimeException {
    }

    public class IncompatibleMessageType extends RuntimeException {
    }

    private class DelegatingDataChangedObserver implements DataChangedObserver {
        @Override
        public void onDataChanged() {
            if (shouldNotifyObservers()) {
                MessageDetailViewModel.this.notifyObservers();
            }
        }
    }
}
