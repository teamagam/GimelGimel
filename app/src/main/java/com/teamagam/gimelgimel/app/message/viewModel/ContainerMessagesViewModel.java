package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;
import android.databinding.Bindable;

import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.messages.SyncNumReadMessagesInteractorFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Exposes functionality to display summary of the current messages status
 */
public class ContainerMessagesViewModel extends SelectedMessageViewModel<MessagesContainerFragment> {

    @Inject
    Context mContext;

    //interactors
    @Inject
    SyncNumReadMessagesInteractorFactory syncNumReadMessagesInteractorFactory;

    private SyncInteractor mSyncNumReadMessagesInteractor;

    private int mNumReadMessages;

    @Inject
    public ContainerMessagesViewModel() {
        super();
        mNumReadMessages = 0;
    }

    @Bindable
    public String getUnreadMessageCount() {
        return Integer.toString(mNumReadMessages);
    }

    public MessageApp getSelectedMessage() {
        return mMessageSelected;
    }

    @Override
    public void start() {
        super.start();
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

    @Override
    protected void updateSelectedMessage() {
        showDetailFragment();
    }

    private void showDetailFragment() {
        String type = mMessageSelected.getType();
        switch (type) {
            case MessageApp.TEXT:
                mView.showDetailTextFragment();
                break;
            case MessageApp.GEO:
                mView.showDetailGeoFragment();
                break;
            case MessageApp.IMAGE:
                mView.showDetailImageFragment();
                break;
            default:
        }
    }
}
