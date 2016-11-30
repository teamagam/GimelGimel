package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.notifications.SyncMessageNotificationInteractor;
import com.teamagam.gimelgimel.domain.notifications.SyncMessageNotificationInteractorFactory;
import com.teamagam.gimelgimel.domain.notifications.entity.MessageNotification;

import javax.inject.Inject;

/**
 * Displaying messages notifications view-model
 */
@PerActivity
public class MainNotificationsViewModel {

    private final static int ERROR_COLOR = R.color.message_notification_error;
    private final static int SUCCESS_COLOR = R.color.message_notification_success;
    private final static int SENDING_COLOR = R.color.message_notification_sending;

    private SyncMessageNotificationInteractor mInteractor;
    private SyncMessageNotificationInteractorFactory mInteractorFactory;
    private IMessageNotificationView mView;
    private Context mContext;

    @Inject
    public MainNotificationsViewModel(SyncMessageNotificationInteractorFactory interactorFactory
            , Context context) {
        mInteractorFactory = interactorFactory;
        mContext = context;
    }

    public void setView(IMessageNotificationView view) {
        mView = view;
    }

    public void start() {
        mInteractor = mInteractorFactory.create(new MessageNotificationSubscriber());
        mInteractor.execute();
    }

    public void stop() {
        mInteractor.unsubscribe();
    }

    private void showNotificationMessage(MessageNotification messageNotification) {
        String msgType = getMessageType(messageNotification);
        String msgText = getMessageText(messageNotification, msgType);
        int color = getMessageColor(messageNotification);
        mView.showMessageNotification(msgText, color);
    }

    private int getMessageColor(MessageNotification messageNotification) {
        int color = -1;
        switch (messageNotification.getState()) {
            case MessageNotification.ERROR:
                color = ERROR_COLOR;
                break;
            case MessageNotification.SENDING:
                color = SENDING_COLOR;
                break;
            case MessageNotification.SUCCESS:
                color = SUCCESS_COLOR;
                break;
            default:
        }
        return color;
    }

    private String getMessageText(MessageNotification messageNotification,
                                  String msgType) {
        String msgText = null;
        switch (messageNotification.getState()) {
            case MessageNotification.ERROR:
                msgText = mContext.getString(R.string.message_notification_error, msgType);
                break;
            case MessageNotification.SENDING:
                msgText = mContext.getString(R.string.message_notification_sending, msgType);
                break;
            case MessageNotification.SUCCESS:
                msgText = mContext.getString(R.string.message_notification_success, msgType);
                break;
            default:
        }
        return msgText;
    }

    @NonNull
    private String getMessageType(MessageNotification messageNotification) {
        String msgType;
        if (messageNotification.getMessage() instanceof MessageImage) {
            msgType = mContext.getString(R.string.message_notification_image_name);
        } else if (messageNotification.getMessage() instanceof MessageGeo) {
            msgType = mContext.getString(R.string.message_notification_geo_name);
        } else {
            msgType = mContext.getString(R.string.message_notification_text_name);
        }
        return msgType;
    }

    public interface IMessageNotificationView {
        void showMessageNotification(String msgText, int msgColor);
    }

    private class MessageNotificationSubscriber extends SimpleSubscriber<MessageNotification> {
        @Override
        public void onNext(MessageNotification messageNotification) {
            showNotificationMessage(messageNotification);
        }
    }
}
