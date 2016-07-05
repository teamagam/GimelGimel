package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.common.DataChangedObservable;
import com.teamagam.gimelgimel.app.common.DataChangedObserver;
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
        mMessageModel.setObserver(renotifyObserver);
        mSelectedMessageModel.setObserver(renotifyObserver);
        mMessagesReadStatusModel.setObserver(renotifyObserver);
    }

    public void select(DisplayMessage displayMessage) {
        Message message = displayMessage.getMessage();
        mMessagesReadStatusModel.markAsRead(message);
        mSelectedMessageModel.select(message);
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
                return createDisplayMessage(message, index);
            }

            private DisplayMessage createDisplayMessage(Message message, int index) {
                DisplayMessage dm = new DisplayMessage(message);
                if (isSelected(message, index)) {
                    dm.setSelected();
                } else {
                    dm.setUnselected();
                }
                if (isRead(message)) {
                    dm.setRead();
                } else {
                    dm.setUnread();
                }
                return dm;
            }

            private boolean isRead(Message message) {
                return mMessagesReadStatusModel.isRead(message);
            }

            private boolean isSelected(Message message, int index) {
                if (mSelectedMessageModel.isAnySelected()) {
                    return mSelectedMessageModel.getSelected() == message;
                }

                mSelectedMessageModel.select(message);
                return index == 0;
            }
        };
    }

    interface DisplayedMessagesRandomAccessor {
        int size();

        DisplayMessage get(int index);
    }

    private class RenotifyUpwardsDataChangedObserver implements DataChangedObserver {
        @Override
        public void onDataChanged() {
            MessagesViewModel.this.notifyObserver();
        }
    }
}
