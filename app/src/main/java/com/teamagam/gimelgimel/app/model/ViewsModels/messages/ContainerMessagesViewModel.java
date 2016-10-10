package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.common.DataChangedObservable;
import com.teamagam.gimelgimel.app.common.DataChangedObserver;
import com.teamagam.gimelgimel.app.model.entities.messages.MessagesModel;
import com.teamagam.gimelgimel.app.model.entities.messages.MessagesReadStatusModel;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
import com.teamagam.gimelgimel.app.viewModels.ViewModel;

/**
 * Exposes functionality to display summary of the current messages status
 */
public class ContainerMessagesViewModel extends SelectedMessageViewModel implements ViewModel {

    private MessagesReadStatusModel mMessagesReadStatusModel;
    private MessagesModel mMessageModel;

    public ContainerMessagesViewModel(
            SelectedMessageModel selectedMessageModel,
            MessagesReadStatusModel messagesReadStatusModel,
            MessagesModel messagesModel) {
        super(selectedMessageModel);
        mMessagesReadStatusModel = messagesReadStatusModel;
        mMessageModel = messagesModel;

        enableObserverNotifyingOnModelChanges();
    }

    public boolean isAnyMessageSelected() {
        return mSelectedMessageModel.isAnySelected();
    }

    public int getUnreadMessageCount() {
        return mMessageModel.size() - mMessagesReadStatusModel.getCount();
    }

    public Object getMessageContent() {
        validateSelectedMessage();
        return mSelectedMessageModel.getSelected().getContent();
    }

    @Override
    protected boolean shouldNotifyOnSelectedMessageModelChange() {
        return true;
    }

    private void enableObserverNotifyingOnModelChanges() {
        DataChangedObserver messageRelatedModelChangedObserver = new DataChangedObserver() {
            @Override
            public void onDataChanged() {
                ContainerMessagesViewModel.this.notifyObservers();
            }
        };

        mMessageModel.addObserver(messageRelatedModelChangedObserver);
        mMessagesReadStatusModel.addObserver(messageRelatedModelChangedObserver);
    }
}
