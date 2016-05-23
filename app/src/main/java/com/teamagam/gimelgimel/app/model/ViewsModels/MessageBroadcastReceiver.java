package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

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

    protected IntentFilter getIntentFilter() {
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

    public static void sendBroadcastMessage(Context context, Message msg) {
        Intent intent = new Intent(msg.getClass().getName());
        intent.putExtra(MessageBroadcastReceiver.MESSAGE, GsonUtil.toJson(msg));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void registerReceiver(Context context, MessageBroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, receiver.getIntentFilter());
    }

    public static void unregisterReceiver(Context context, MessageBroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    public interface NewMessageHandler {
        void onNewMessage(Message msg);
    }


}
