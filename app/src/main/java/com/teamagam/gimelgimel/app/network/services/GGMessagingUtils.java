package com.teamagam.gimelgimel.app.network.services;

import android.util.Log;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageJsonAdapter;
import com.teamagam.gimelgimel.app.network.rest.RestAPI;
import com.teamagam.gimelgimel.app.utils.NetworkUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Utility class handling different requests from GGMessaging server
 */
public class GGMessagingUtils {

    private static final String LOG_TAG = GGMessagingUtils.class.getSimpleName();

    /**
     * Creates {@link Message} with containing given content
     * and asynchronously sends it
     *
     * @param content the message content
     * @param type - the type of message to send
     */
    //todo: think about how to implement this factory message. switch case? is this solution OK?
    public static void sendMessageAsync(Object content, @Message.MessageType String type){
        String senderId = NetworkUtil.getMac();
        Message messageToSend = MessageJsonAdapter.createCustomMessage(content, type, senderId);
        GGMessagingUtils.sendMessageAsync(messageToSend);
    }

    /**
     * Asynchronously sends message to service
     *
     * @param message - the message to send
     */
    public static void sendMessageAsync(Message message) {
        Call<Message> call = RestAPI.getInstance().getMessagingAPI().postMessage(message);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (!response.isSuccessful()) {
                    Log.d(LOG_TAG, "Unsuccessful message post: " + response.errorBody());
                    return;
                }

                Log.d(LOG_TAG, "message ID from DB: " + response.body().getMessageId());
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d(LOG_TAG, "FAIL in sending message!!!");
            }
        });
    }
}
