package com.teamagam.gimelgimel.app.network.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageImage;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageLatLong;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageText;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageUserLocation;
import com.teamagam.gimelgimel.app.model.entities.ImageMetadata;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.network.rest.RestAPI;
import com.teamagam.gimelgimel.app.utils.PreferenceUtil;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Utility class handling different requests from GGMessaging server
 */
public class GGMessageSender {

    public static String getUserName(Context context) {
        return ((GGApplication) context.getApplicationContext()).getPrefs().getString(
                R.string.user_name_text_key);
    }

    private static final Logger sLogger = LoggerFactory.create(GGMessageSender.class);

    /**
     * Used to holds a strong reference to the listener
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final SenderIdUpdaterPreferenceChangeListener mSenderIdUpdaterPreferenceChangeListener;

    private final GGApplication mAppContext;

    private String mSenderId;

    public GGMessageSender(Context context) {
        mAppContext = (GGApplication) context.getApplicationContext();
        PreferenceUtil prefs = mAppContext.getPrefs();
        mSenderId = prefs.getString(R.string.user_name_text_key);
        mSenderIdUpdaterPreferenceChangeListener = new SenderIdUpdaterPreferenceChangeListener();
        prefs.registerOnSharedPreferenceChangeListener(mSenderIdUpdaterPreferenceChangeListener);
    }

    public void sendTextMessageAsync(String message) {
        MessageText messageToSend = new MessageText(mSenderId, message);
        GGMessageSender.sendMessageAsync(messageToSend);
    }

    /**
     * Creates location {@link Message} with {@link MessageLatLong} containing given
     * {@link PointGeometry} and asynchronously sends it
     *
     * @param pointGeometry the message's content location
     */
    public void sendLatLongMessageAsync(PointGeometry pointGeometry) {
        Message messageToSend = new MessageLatLong(mSenderId, pointGeometry);
        GGMessageSender.sendMessageAsync(messageToSend);
    }


    /**
     * Creates location {@link Message} with {@link MessageUserLocation} containing given
     * {@link LocationSample} and asynchronously sends it
     *
     * @param sample
     */
    public void sendUserLocationMessageAsync(LocationSample sample) {
        Message messageToSend = new MessageUserLocation(mSenderId, sample);
        GGMessageSender.sendMessageAsync(messageToSend);
    }


    public void sendImageMessageAsync(ImageMetadata meta) {
        Message messageToSend = new MessageImage(mSenderId, meta);
        GGMessageSender.sendMessageAsync(messageToSend);
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
                    sLogger.d("Unsuccessful message post: " + response.errorBody());
                    return;
                }

                sLogger.d("message ID from DB: " + response.body().getMessageId());
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                sLogger.d("FAIL in sending message!!!");
            }
        });
    }

    private class SenderIdUpdaterPreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                              String key) {
            if (key.equals(mAppContext.getString(R.string.user_name_text_key))) {
                mSenderId = sharedPreferences.getString(
                        mAppContext.getString(R.string.user_name_text_key), null);
            }
        }
    }
}
