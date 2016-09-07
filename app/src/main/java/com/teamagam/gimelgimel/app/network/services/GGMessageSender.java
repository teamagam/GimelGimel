package com.teamagam.gimelgimel.app.network.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.message.model.MessageGeo;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageText;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageUserLocation;
import com.teamagam.gimelgimel.app.model.entities.GeoContent;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.network.rest.RestAPI;
import com.teamagam.gimelgimel.app.utils.PreferenceUtil;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;

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
        MessageText messageToSend = new MessageText(mSenderId, message);
        sendMessageAsync(messageToSend);
    }

    /**
     * Creates location {@link Message} with {@link MessageGeo} containing given
     * {@link PointGeometry} and asynchronously sends it
     *
     * @param pointGeometry the message's content location
     * @return Message - the message that was sent to the server.
     */
    public Message sendGeoMessageAsync(PointGeometry pointGeometry, String text, String type) {
        GeoContent location = new GeoContent(pointGeometry, text, type);
        Message messageToSend = new MessageGeo(mSenderId, location);
        sendMessageAsync(messageToSend);
        return messageToSend;
    }


    /**
     * Creates location {@link Message} with {@link MessageUserLocation} containing given
     * {@link LocationSample} and asynchronously sends it
     *
     * @param sample
     */
    public void sendUserLocationMessageAsync(LocationSample sample) {
        Message messageToSend = new MessageUserLocation(mSenderId, sample);
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
    private void sendMessageAsync(final Message message) {
        Call<Message> call = RestAPI.getInstance().getMessagingAPI().postMessage(message);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (!response.isSuccessful()) {
                    sLogger.d("Unsuccessful message post: " + response.errorBody());

                    fireOnFailureMessage(message);
                    return;
                }

                sLogger.d("message ID from DB: " + response.body().getMessageId());

                fireOnSuccessfulMessage(message);
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                sLogger.d("FAIL in sending message!!!");

                fireOnFailureMessage(message);
            }
        });
    }

    private void fireOnSuccessfulMessage(Message message) {
        for (MessageStatusListener listener : mListeners) {
            listener.onSuccessfulMessage(message);
        }
    }

    private void fireOnFailureMessage(Message message) {
        for (MessageStatusListener listener : mListeners) {
            listener.onFailureMessage(message);
        }
    }

    public interface MessageStatusListener {
        void onSuccessfulMessage(Message message);

        void onFailureMessage(Message message);
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
