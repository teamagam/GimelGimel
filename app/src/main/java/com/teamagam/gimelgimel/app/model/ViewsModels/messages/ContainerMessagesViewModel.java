package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import android.databinding.Bindable;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.entities.messages.MessagesModel;
import com.teamagam.gimelgimel.app.viewModels.ViewModel;
import com.teamagam.gimelgimel.domain.messages.GetMessagesInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.SyncMessagesInteractorFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

/**
 * Exposes functionality to display summary of the current messages status
 */
public class ContainerMessagesViewModel extends SelectedMessageViewModel implements ViewModel {

//    private MessagesReadStatusModel mMessagesReadStatusModel;
    private MessagesModel mMessageModel;

    private com.teamagam.gimelgimel.app.model.ViewsModels.Message mMessageSelected;

    //interactors
    @Inject
    SyncSelectedMessageInteractorFactory syncSelectedMessageInteractorFactory;

    @Inject
    SyncMessagesInteractorFactory syncMessagesInteractorFactory;

    @Inject
    GetMessagesInteractorFactory getMessagesInteractorFactory;

    public ContainerMessagesViewModel(
//            SelectedMessageModel selectedMessageModel,
//            MessagesReadStatusModel messagesReadStatusModel,
//            MessagesModel messagesModel
    ) {
        super();
        //            super(selectedMessageModel);
//        mMessagesReadStatusModel = messagesReadStatusModel;
//        mMessageModel = messagesModel;

        getMessagesInteractorFactory.create(this).execute();
        syncMessagesInteractorFactory.create(this).execute();
        syncSelectedMessageInteractorFactory.create(this).execute();
//            enableObserverNotifyingOnModelChanges();
    }

    public boolean isAnyMessageSelected() {
        return mSelectedMessageModel.isAnySelected();
    }

    @Bindable
    int getUnreadMessageCount() {
        return mMessageModel.size() - mMessageModel.getReadCount();
    }

    Object getMessageContent() {
        validateSelectedMessage();
        return mSelectedMessageModel.getSelected().getContent();
    }

    @Override
    protected boolean shouldNotifyOnSelectedMessageModelChange() {
        return true;
    }

    public com.teamagam.gimelgimel.app.model.ViewsModels.Message getSelectedMessage() {
        return mMessageSelected;
    }

    private void updateContainerTitle() {
        notifyPropertyChanged(com.teamagam.gimelgimel.BR.title);
        notifyPropertyChanged(com.teamagam.gimelgimel.BR.numRead);
    }

    public String getTitle() {
        if (isAnyMessageSelected()) {
            String type = mMessageSelected.getType();
            String senderId = mMessageSelected.getSenderId();
            Date createdAt = mMessageSelected.getCreatedAt();

            //get current data HH:mm:ss
            SimpleDateFormat sdf = new SimpleDateFormat(R.string.message_list_item_time);
            String date = (sdf.format(createdAt));

            return senderId + ": sent " + type + " message on " + date;
        } else {
            return null;
        }

    }

}
