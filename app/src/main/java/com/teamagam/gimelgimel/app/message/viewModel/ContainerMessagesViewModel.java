package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.viewModel.BaseViewModel;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.model.MessageImageApp;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessageAppMapper;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.DisplayUnreadMessagesCountInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplayUnreadMessagesCountInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;


/**
 * Exposes functionality to display summary of the current messages status
 */
public class ContainerMessagesViewModel extends BaseViewModel<MessagesContainerFragment> {

    private static final AppLogger sLogger = AppLoggerFactory.create();

    @Inject
    Context mContext;

    @Inject
    MessageAppMapper mTransformer;

    @Inject
    DisplayUnreadMessagesCountInteractorFactory mDisplayUnreadMessagesCountInteractorFactory;

    @Inject
    DisplaySelectedMessageInteractorFactory mDisplaySelectedMessageInteractorFactory;

    private DisplayUnreadMessagesCountInteractor mDisplayUnreadMessagesCountInteractor;

    private DisplaySelectedMessageInteractor mDisplaySelectedMessageInteractor;

    private int mNumUnreadMessages;
    private String mTitle;
    private boolean mIsMessageSelected;

    @Inject
    public ContainerMessagesViewModel() {
        super();
        mIsMessageSelected = false;
    }

    @Bindable
    public String getUnreadMessageCount() {
        return Integer.toString(mNumUnreadMessages);
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

        mDisplaySelectedMessageInteractor = mDisplaySelectedMessageInteractorFactory.create(
                new DisplaySelectedMessageInteractor.Displayer() {
                    @Override
                    public void display(Message message) {
                        MessageApp messageApp = mTransformer.transformToModel(message);
                        sLogger.d(
                                "Displaying selected message detail view (fragment) for message id = " + messageApp.getMessageId());
                        updateTitle(messageApp);
                        showDetailFragment(messageApp);
                        mIsMessageSelected = true;
                        notifyChange();
                    }
                });
        mDisplaySelectedMessageInteractor.execute();
    }

    public boolean isMessageSelected() {
        return mIsMessageSelected;
    }

    private void updateTitle(MessageApp messageApp) {
        mTitle = createTitle(messageApp);
        notifyPropertyChanged(BR.title);
    }


    @Override
    public void stop() {
        super.stop();
        mDisplayUnreadMessagesCountInteractor.unsubscribe();
        mDisplaySelectedMessageInteractor.unsubscribe();
    }

    @Bindable
    public String getTitle() {
        if (mTitle != null) {
            return mTitle;
        } else {
            return mContext.getString(R.string.message_info_default);
        }
    }

    @NonNull
    private String createTitle(MessageApp mMessageSelected) {
        String type = mMessageSelected.getType();
        String senderId = mMessageSelected.getSenderId();
        Date createdAt = mMessageSelected.getCreatedAt();

        //get current data HH:mm:ss
        SimpleDateFormat sdf = new SimpleDateFormat(mContext.getString(R.string
                .message_list_item_time), Locale.ENGLISH);
        String date = (sdf.format(createdAt));

        return senderId + " sent: " + type + " message on " + date;
    }

    private void showDetailFragment(MessageApp message) {
        String type = message.getType();
        switch (type) {
            case MessageApp.TEXT:
                mView.showDetailTextFragment((MessageTextApp) message);
                break;
            case MessageApp.GEO:
                mView.showDetailGeoFragment((MessageGeoApp) message);
                break;
            case MessageApp.IMAGE:
                mView.showDetailImageFragment((MessageImageApp) message);
                break;
            default:
        }
    }
}
