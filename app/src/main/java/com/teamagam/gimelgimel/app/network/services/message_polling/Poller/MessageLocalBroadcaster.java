package com.teamagam.gimelgimel.app.network.services.message_polling.poller;

import android.content.Context;

import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageBroadcastReceiver;
import com.teamagam.gimelgimel.app.network.services.GGMessageSender;

/**
 * Wraps Local-broadcasting functionality
 */
public class MessageLocalBroadcaster implements IMessageBroadcaster {

    private static final Logger sLogger = LoggerFactory.create(
            MessageLocalBroadcaster.class);

    private Context mContext;

    public MessageLocalBroadcaster(Context context) {
        mContext = context;
    }

    @Override
    public void broadcast(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        //Do not broadcast messages from self
        if (message.getSenderId().equals(GGMessageSender.getUserName(mContext))) {
            return;
        }

        sLogger.v("Broadcasting message with ID: " + message.getMessageId());

        MessageBroadcastReceiver.sendBroadcastMessage(mContext, message);
    }
}
