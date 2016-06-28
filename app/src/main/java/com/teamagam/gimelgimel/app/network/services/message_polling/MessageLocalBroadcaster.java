package com.teamagam.gimelgimel.app.network.services.message_polling;

import android.content.Context;
import android.util.Log;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageBroadcastReceiver;
import com.teamagam.gimelgimel.app.utils.PreferenceUtil;

/**
 * Wraps Local-broadcasting functionality
 */
public class MessageLocalBroadcaster implements IMessageBroadcaster {

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
        if (message.getSenderId().equals(PreferenceUtil.getUserName(mContext))) {
            return;
        }

        Log.v(MessageLocalBroadcaster.class.getSimpleName(), "Broadcasting message with ID: " + message.getMessageId());

        MessageBroadcastReceiver.sendBroadcastMessage(mContext, message);
    }
}
