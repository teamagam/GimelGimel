package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.teamagam.gimelgimel.app.utils.GsonUtil;

/**
 * Created on 5/1/2016.
 * Broadcast receiver for handling messages in GG application
 * uses {@link GsonUtil} to serialize {@link Message}
 * can be extended in the future for more detailed onReceive behavior.
 */
public class MessageBroadcastReceiver extends BroadcastReceiver {

    public static final String MESSAGE = "message";

    protected NewMessageHandler mHandler;
    private Class mClass;

    public MessageBroadcastReceiver(NewMessageHandler handler, @Message.MessageType String type) {
        mHandler = handler;
        mClass = MessageJsonAdapter.sClassMessageMap.get(type);
    }

    public IntentFilter getIntentFilter() {
        return new IntentFilter(mClass.getName());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String msg_String = intent.getStringExtra(MessageBroadcastReceiver.MESSAGE);
        if (msg_String != null && mHandler != null) {
            Message msg = GsonUtil.fromJson(msg_String, Message.class);
            mHandler.onNewMessage(msg);
        }
    }

    public interface NewMessageHandler {
        void onNewMessage(Message msg);
    }


}
