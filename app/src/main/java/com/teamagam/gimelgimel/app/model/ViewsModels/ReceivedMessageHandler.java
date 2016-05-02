package com.teamagam.gimelgimel.app.model.ViewsModels;

/**
 * Created on 5/1/2016.
 * TODO: complete text
 */
public class ReceivedMessageHandler implements MessagePubSub.NewMessagesSubscriber {

    public static final String LOG_TAG = ReceivedMessageHandler.class.getSimpleName();

    /**
     * Singleton pattern
     */
    private static ReceivedMessageHandler sInstance = new ReceivedMessageHandler();

    public static ReceivedMessageHandler getInstance() {
        return sInstance;
    }

    private ShowMessagesSubscriber mShowSubscriber = null;
    private UpdateLocationMessagesSubscriber mLocationSubscriber = null;

    private boolean isSubscribed = false;

    public ReceivedMessageHandler() {
    }

    @Override
    public void onNewMessage(Message msg) {
        if (msg instanceof MessageText) {
            mShowSubscriber.onShowMessage(msg);
        } else if (msg instanceof MessageLatLong) {
            mShowSubscriber.onShowMessage(msg);
        } else if (msg instanceof MessageUserLocation) {
            mLocationSubscriber.onUpdateLocationMessage(msg);
        } else {
            throw new IllegalArgumentException("msg type was not recognized");
        }
    }


    public void subscribeShow(ShowMessagesSubscriber subscriber) {
        mShowSubscriber = subscriber;
        subscribeMessages();
    }

    /**
     * Stop notifying subscriber about new messages
     */
    public void unsubscribeShow() {
        mShowSubscriber = null;
        unsubscribeMessages();
    }

    public void subscribeLocation(UpdateLocationMessagesSubscriber subscriber) {
        mLocationSubscriber = subscriber;
        subscribeMessages();
    }

    /**
     * Stop notifying subscriber about new messages
     */
    public void unsubscribeLocation() {
        mLocationSubscriber = null;
        unsubscribeMessages();
    }


    private void subscribeMessages() {
        if (!isSubscribed) {
            MessagePubSub.getInstance().subscribe(this);
            isSubscribed = true;
        }
    }

    private void unsubscribeMessages() {
        if (mShowSubscriber == null && mLocationSubscriber == null) {
            MessagePubSub.getInstance().unsubscribe();
            isSubscribed = false;
        }
    }

    public interface ShowMessagesSubscriber {
        void onShowMessage(Message msg);
    }

    public interface UpdateLocationMessagesSubscriber {
        void onUpdateLocationMessage(Message msg);
    }
}
