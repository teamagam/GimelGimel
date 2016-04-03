package com.teamagam.gimelgimel.app.network.services;

import android.support.annotation.Nullable;
import android.util.Log;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.network.rest.RestAPI;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Utility class handling different requests from GGMessaging server
 */
public class GGMessagingUtils {

    private static final String LOG_TAG = GGMessagingUtils.class.getSimpleName();

    /**
     * Asynchronously sends message to service
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

    /**
     * Synchronously gets messages from server with date constraint
     * @param fromDateAsMs - the date (in ms) filter to be used.
     * @return messages with date gte fromDateAsMs
     */
    @Nullable
    public static List<Message> getMessagesSync(long fromDateAsMs) {
        //Construct remote API call
        Call<List<Message>> messagesCall;

        if (fromDateAsMs == 0) {
            messagesCall = RestAPI.getInstance().getMessagingAPI().getMessages();
        } else {
            messagesCall = RestAPI.getInstance().getMessagingAPI().getMessagesFromDate(
                    fromDateAsMs);
        }

        List<Message> messages = null;
        try {
            //Synchronous execution of remote API call
            messages = messagesCall.execute().body();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with message polling ", e);
        }
        return messages;
    }
}
