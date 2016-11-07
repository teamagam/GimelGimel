package com.teamagam.gimelgimel.app.network.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.message.model.MessageUserLocationApp;
import com.teamagam.gimelgimel.app.message.model.contents.LocationSample;
import com.teamagam.gimelgimel.app.network.rest.RestAPI;
import com.teamagam.gimelgimel.app.utils.PreferenceUtil;
import com.teamagam.gimelgimel.domain.base.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Utility class handling different requests from GGMessaging server
 */
public class GGMessageSender {

    private static final Logger sLogger = LoggerFactory.create(GGMessageSender.class);

    public static String getUserName(Context context) {
        return ((GGApplication) context.getApplicationContext()).getPrefs().getString(
                R.string.user_name_text_key);
    }

    /**
     * Used to holds a strong reference to the listener
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final SenderIdUpdaterPreferenceChangeListener mSenderIdUpdaterPreferenceChangeListener;

    private final GGApplication mAppContext;

    private String mSenderId;
    private List<MessageStatusListener> mListeners;

    public GGMessageSender(Context context) {
        mAppContext = (GGApplication) context.getApplicationContext();
        PreferenceUtil prefs = mAppContext.getPrefs();
        mSenderId = prefs.getString(R.string.user_name_text_key);
        mSenderIdUpdaterPreferenceChangeListener = new SenderIdUpdaterPreferenceChangeListener();
        mListeners = new ArrayList<>();

        prefs.registerOnSharedPreferenceChangeListener(mSenderIdUpdaterPreferenceChangeListener);
    }

    public void sendTextMessageAsync(String message) {
        MessageTextApp messageToSend = new MessageTextApp(message);
        sendMessageAsync(messageToSend);
    }

    /**
     * Creates location {@link MessageApp} with {@link MessageGeoApp} containing given
     * {@link PointGeometryApp} and asynchronously sends it
     *
     * @param pointGeometry the message's content location
     * @return MessageApp - the message that was sent to the server.
     */
    public MessageApp sendGeoMessageAsync(PointGeometryApp pointGeometry, String text, String type) {
//        GeoContentApp location = new GeoContentApp(pointGeometry, text, type);
//        MessageApp messageToSend = new MessageGeoApp(location);
//        sendMessageAsync(messageToSend);
//        return messageToSend;
        return null;
    }


    /**
     * Creates location {@link MessageApp} with {@link MessageUserLocationApp} containing given
     * {@link LocationSample} and asynchronously sends it
     *
     * @param sample
     */
    public void sendUserLocationMessageAsync(LocationSample sample) {
        MessageApp messageToSend = new MessageUserLocationApp(sample);
        sendMessageAsync(messageToSend);
    }

    public void addListener(@Nullable MessageStatusListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(@Nullable MessageStatusListener listener) {
        mListeners.remove(listener);
    }

    /**
     * Asynchronously sends message to service
     *
     * @param message - the message to send
     */
    private void sendMessageAsync(final MessageApp message) {
        Call<MessageApp> call = RestAPI.getInstance().getMessagingAPI().postMessage(message);
        call.enqueue(new Callback<MessageApp>() {
            @Override
            public void onResponse(Call<MessageApp> call, Response<MessageApp> response) {
                if (!response.isSuccessful()) {
                    sLogger.d("Unsuccessful message post: " + response.errorBody());

                    fireOnFailureMessage(message);
                    return;
                }

                sLogger.d("message ID from DB: " + response.body().getMessageId());

                fireOnSuccessfulMessage(message);
            }

            @Override
            public void onFailure(Call<MessageApp> call, Throwable t) {
                sLogger.d("FAIL in sending message!!!");

                fireOnFailureMessage(message);
            }
        });
    }

    private void fireOnSuccessfulMessage(MessageApp message) {
        for (MessageStatusListener listener : mListeners) {
            listener.onSuccessfulMessage(message);
        }
    }

    private void fireOnFailureMessage(MessageApp message) {
        for (MessageStatusListener listener : mListeners) {
            listener.onFailureMessage(message);
        }
    }

    public interface MessageStatusListener {
        void onSuccessfulMessage(MessageApp message);

        void onFailureMessage(MessageApp message);
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
