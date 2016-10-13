package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;
import android.databinding.Bindable;

import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.messages.GetMessagesInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.SyncMessagesInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.SyncNumReadMessagesInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Exposes functionality to display summary of the current messages status
 */
public class ContainerMessagesViewModel extends SelectedMessageViewModel<MessagesContainerFragment>{

    //interactors
    @Inject
    SyncMessagesInteractorFactory syncMessagesInteractorFactory;

    @Inject
    SyncNumReadMessagesInteractorFactory syncNumReadMessagesInteractorFactory;

    @Inject
    GetMessagesInteractorFactory getMessagesInteractorFactory;

    @Inject
    Context mContext;

    private SyncInteractor mSyncMessagesInteractor;
    private SyncInteractor mSyncNumReadMessagesInteractor;

    private int mNumReadMessages;

    @Inject
    public ContainerMessagesViewModel() {
        super();
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
        super.start();
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

        mSyncNumReadMessagesInteractor = syncNumReadMessagesInteractorFactory.create(
                new SimpleSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        mNumReadMessages = integer;
                        notifyPropertyChanged(BR.unreadMessageCount);
                    }
                });
        mSyncNumReadMessagesInteractor.execute();

    }

    @Override
    protected boolean shouldNotifyOnSelectedMessage() {
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        mSyncMessagesInteractor.unsubscribe();
        mSyncNumReadMessagesInteractor.unsubscribe();
    }



    @Bindable
    public String getTitle() {
//        if (isAnyMessageSelected()) {
            String type = mMessageSelected.getType();
            String senderId = mMessageSelected.getSenderId();
            Date createdAt = mMessageSelected.getCreatedAt();

            //get current data HH:mm:ss
            SimpleDateFormat sdf = new SimpleDateFormat(mContext.getString(R.string
                    .message_list_item_time), Locale.ENGLISH);
            String date = (sdf.format(createdAt));

            return senderId + ": sent " + type + " message on " + date;
//        } else {
//            return mContext.getString(R.string.message_info_default);
//        }

    }

    private void newMessageArrived(Message message) {
        mTransformer.transformToModel(message);
    }

    @Override
    protected void updateSelectedMessage() {
        showDetailFragment();
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
