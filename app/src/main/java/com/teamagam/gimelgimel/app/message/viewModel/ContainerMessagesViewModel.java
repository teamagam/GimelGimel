package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;
import android.databinding.Bindable;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessageModelMappper;
import com.teamagam.gimelgimel.app.viewModels.BaseViewModel;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.messages.GetMessagesInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.SyncMessagesInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.SyncNumReadMessagesInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.SyncSelectedMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Exposes functionality to display summary of the current messages status
 */
public class ContainerMessagesViewModel extends BaseViewModel<MessagesContainerFragment>{

    //interactors
    @Inject
    SyncSelectedMessageInteractorFactory syncSelectedMessageInteractorFactory;

    @Inject
    SyncMessagesInteractorFactory syncMessagesInteractorFactory;

    @Inject
    SyncNumReadMessagesInteractorFactory syncNumReadMessagesInteractorFactory;

    @Inject
    GetMessagesInteractorFactory getMessagesInteractorFactory;

    @Inject
    MessageModelMappper mTransformer;

    @Inject
    Context mContext;

    private SyncInteractor mSyncMessagesInteractor;
    private SyncInteractor mSyncSelectedMessageInteractor;
    private SyncInteractor mSyncNumReadMessagesInteractor;

    private int mNumReadMessages;
    private com.teamagam.gimelgimel.app.model.ViewsModels.Message mMessageSelected;

    @Inject
    public ContainerMessagesViewModel() {
        super();
    }

    public boolean isAnyMessageSelected() {
        return mMessageSelected != null;
    }

    @Bindable
    public int getUnreadMessageCount() {
        return mNumReadMessages;
    }

    public com.teamagam.gimelgimel.app.model.ViewsModels.Message getSelectedMessage() {
        return mMessageSelected;
    }

    @Override
    public void start() {
        getMessagesInteractorFactory.create(new SimpleSubscriber<Message>() {
            @Override
            public void onNext(Message message) {
                newMessageArrived(message);
            }
        }).execute();

        mSyncMessagesInteractor = syncMessagesInteractorFactory.create(
                new SimpleSubscriber<Message>() {
                    @Override
                    public void onNext(Message message) {
                        newMessageArrived(message);
                    }
                }
        );
        mSyncMessagesInteractor.execute();
        mSyncSelectedMessageInteractor = syncSelectedMessageInteractorFactory.create(
                new SimpleSubscriber<Message>() {
                    @Override
                    public void onNext(Message message) {
                        updateSelectedMessage(message);
                    }
                });
        mSyncSelectedMessageInteractor.execute();

        mSyncNumReadMessagesInteractor = syncNumReadMessagesInteractorFactory.create(
                new SimpleSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        mNumReadMessages = integer;
                        notifyPropertyChanged(com.teamagam.gimelgimel.BR.unreadMessageCount);
                    }
                });
        mSyncNumReadMessagesInteractor.execute();

    }

    @Override
    public void stop() {
        mSyncMessagesInteractor.unsubscribe();
        mSyncSelectedMessageInteractor.unsubscribe();
        mSyncNumReadMessagesInteractor.unsubscribe();
    }

    @Bindable
    public String getTitle() {
        if (isAnyMessageSelected()) {
            String type = mMessageSelected.getType();
            String senderId = mMessageSelected.getSenderId();
            Date createdAt = mMessageSelected.getCreatedAt();

            //get current data HH:mm:ss
            SimpleDateFormat sdf = new SimpleDateFormat(mContext.getString(R.string
                    .message_list_item_time), Locale.ENGLISH);
            String date = (sdf.format(createdAt));

            return senderId + ": sent " + type + " message on " + date;
        } else {
            return mContext.getString(R.string.message_info_default);
        }

    }

    private void newMessageArrived(Message message) {
        mTransformer.transformToModel(message);
    }

    private void updateSelectedMessage(Message message) {
        mMessageSelected = mTransformer.transformToModel(message);
        showDetailFragment();
        updateContainerTitle();
    }

    private void updateContainerTitle() {
        notifyPropertyChanged(com.teamagam.gimelgimel.BR.title);
    }

    private void showDetailFragment() {
        String type = mMessageSelected.getType();
        switch (type) {
            case com.teamagam.gimelgimel.app.model.ViewsModels.Message.TEXT:
                mView.showDetailTextFragment();
                break;
            case com.teamagam.gimelgimel.app.model.ViewsModels.Message.GEO:
                mView.showDetailGeoFragment();
                break;
            case com.teamagam.gimelgimel.app.model.ViewsModels.Message.IMAGE:
                mView.showDetailImageFragment();
                break;
            default:
        }
    }
}
