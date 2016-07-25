package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.common.DataChangedObservable;
import com.teamagam.gimelgimel.app.common.DataChangedObserver;
import com.teamagam.gimelgimel.app.common.DataRandomAccessor;
import com.teamagam.gimelgimel.app.common.NotifyingDataChangedObservable;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.entities.messages.MessagesModel;
import com.teamagam.gimelgimel.app.model.entities.messages.MessagesReadStatusModel;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;

/**
 * Messages view-model for messages presentation use-case
 */
public class MessagesViewModel extends NotifyingDataChangedObservable implements DataChangedObservable {

    private MessagesModel mMessageModel;
    private SelectedMessageModel mSelectedMessageModel;
    private MessagesReadStatusModel mMessagesReadStatusModel;

    public MessagesViewModel(
            MessagesModel messagesModel,
            SelectedMessageModel selectedMessageModel,
            MessagesReadStatusModel messagesReadStatusModel) {
        mMessageModel = messagesModel;
        mSelectedMessageModel = selectedMessageModel;
        mMessagesReadStatusModel = messagesReadStatusModel;

        RenotifyUpwardsDataChangedObserver renotifyObserver = new RenotifyUpwardsDataChangedObserver();
        mMessageModel.addObserver(renotifyObserver);
        mSelectedMessageModel.addObserver(renotifyObserver);
        mMessagesReadStatusModel.addObserver(renotifyObserver);
    }

    public void select(DisplayMessage displayMessage) {
        Message message = displayMessage.getMessage();
        markAsSelectedIfNotAlreadySelected(message);
        markAsReadIfNotAlreadyRead(message);
    }

    private void markAsReadIfNotAlreadyRead(Message message) {
        if (!mMessagesReadStatusModel.isRead(message)) {
            mMessagesReadStatusModel.markAsRead(message);
        }
    }

    private void markAsSelectedIfNotAlreadySelected(Message message) {
        if (mSelectedMessageModel.getSelected() != message) {
            mSelectedMessageModel.select(message);
        }
    }

    public DisplayedMessagesRandomAccessor getDisplayedMessagesRandomAccessor() {
        return new DisplayedMessagesRandomAccessor() {
            @Override
            public int size() {
                return mMessageModel.size();
            }

            @Override
            public DisplayMessage get(int index) {
                Message message = mMessageModel.get(index);
                return createDisplayMessage(message);
            }

            private DisplayMessage createDisplayMessage(Message message) {
                boolean isRead = isRead(message);
                boolean isSelected = isSelected(message);
                return new DisplayMessage.DisplayMessageBuilder().setMessage(message).setIsSelected(
                        isSelected).setIsRead(isRead).build();
            }

            private boolean isRead(Message message) {
                return mMessagesReadStatusModel.isRead(message);
            }

            private boolean isSelected(Message message) {
                return mSelectedMessageModel.getSelected() == message;
            }
        };
    }

    public interface DisplayedMessagesRandomAccessor extends DataRandomAccessor<DisplayMessage> {
    }

    private class RenotifyUpwardsDataChangedObserver implements DataChangedObserver {
        @Override
        public void onDataChanged() {
            MessagesViewModel.this.notifyObservers();
        }
    }
}
