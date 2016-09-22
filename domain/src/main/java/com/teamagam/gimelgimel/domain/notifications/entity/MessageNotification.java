package com.teamagam.gimelgimel.domain.notifications.entity;

import com.teamagam.gimelgimel.domain.messages.entity.Message;

/**
 * Data class for message notification
 */
public class MessageNotification {

    public static final int SENDING = 1;
    public static final int SUCCESS = 2;
    public static final int ERROR = 3;

    public static MessageNotification createSendingNotification(Message m) {
        return new MessageNotification(m, SENDING);
    }

    public static MessageNotification createSuccessNotification(Message m) {
        return new MessageNotification(m, SUCCESS);
    }

    public static MessageNotification createErrorNotification(Message m) {
        return new MessageNotification(m, ERROR);
    }

    private final Message mMessage;
    private final int mState;

    private MessageNotification(Message message, int state) {
        mMessage = message;
        mState = state;
    }

    public int getState() {
        return mState;
    }

    public Message getMessage() {
        return mMessage;
    }
}
