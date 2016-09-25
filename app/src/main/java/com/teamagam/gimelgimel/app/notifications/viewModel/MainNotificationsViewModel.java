package com.teamagam.gimelgimel.app.notifications.viewModel;

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
 * Created on 9/22/2016.
 * TODO: complete text
 */
@PerActivity
public class MainNotificationsViewModel {

    private final static int ERROR_COLOR = R.color.red;
    private final static int SUCCESS_COLOR = R.color.green;
    private final static int SENDING_COLOR = R.color.colorPrimary;

    private SyncMessageNotificationInteractor mInteractor;
    private SyncMessageNotificationInteractorFactory mInteractorFactory;
    private IMessageNotificationView mView;

    @Inject
    public MainNotificationsViewModel(SyncMessageNotificationInteractorFactory interactorFactory) {
        mInteractorFactory = interactorFactory;
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
        String msgText = null;
        int color = 0;

        String msgType;
        if (messageNotification.getMessage() instanceof MessageImage) {
            msgType = "image";
        } else if (messageNotification.getMessage() instanceof MessageGeo) {
            msgType = "point";
        } else {
            msgType = "message";
        }

        switch (messageNotification.getState()) {
            case MessageNotification.ERROR:
                msgText = "Problem occurred with sending " + msgType;
                color = ERROR_COLOR;
                break;
            case MessageNotification.SENDING:
                msgText = "Sending " + msgType + " started";
                color = SENDING_COLOR;
                break;
            case MessageNotification.SUCCESS:
                msgText = msgType + " sent successfully";
                color = SUCCESS_COLOR;
                break;
            default:
        }
        mView.showMessageNotification(msgText, color);
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
