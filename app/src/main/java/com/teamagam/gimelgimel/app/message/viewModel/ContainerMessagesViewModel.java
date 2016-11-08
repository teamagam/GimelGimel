package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;
import android.databinding.Bindable;

import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.domain.messages.DisplayUnreadMessagesCountInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplayUnreadMessagesCountInteractorFactory;

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

    @Inject
    DisplayUnreadMessagesCountInteractorFactory mDisplayUnreadMessagesCountInteractorFactory;

    private DisplayUnreadMessagesCountInteractor mDisplayUnreadMessagesCountInteractor;

    private int mNumUnreadMessages;

    @Inject
    public ContainerMessagesViewModel() {
        super();
    }

    @Bindable
    public String getUnreadMessageCount() {
        return Integer.toString(mNumUnreadMessages);
    }

    public MessageApp getSelectedMessage() {
        return mMessageSelected;
    }

    @Override
    public void start() {
        super.start();
        mDisplayUnreadMessagesCountInteractor = mDisplayUnreadMessagesCountInteractorFactory.create(
                new DisplayUnreadMessagesCountInteractor.Renderer() {
                    @Override
                    public void renderUnreadMessagesCount(int unreadMessagesCount) {
                        mNumUnreadMessages = unreadMessagesCount;
                        notifyPropertyChanged(BR.unreadMessageCount);
                    }
                });
        mDisplayUnreadMessagesCountInteractor.execute();
    }

    @Override
    protected boolean shouldNotifyOnSelectedMessage() {
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        mDisplayUnreadMessagesCountInteractor.unsubscribe();
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
