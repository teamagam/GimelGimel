package com.teamagam.gimelgimel.app.network.services.message_polling;

import android.content.Context;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageBroadcastReceiver;
import com.teamagam.gimelgimel.app.utils.NetworkUtil;

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
        if (message.getSenderId().equals(NetworkUtil.getMac())) {
            return;
        }

        MessageBroadcastReceiver.sendBroadcastMessage(mContext, message);
    }
}
