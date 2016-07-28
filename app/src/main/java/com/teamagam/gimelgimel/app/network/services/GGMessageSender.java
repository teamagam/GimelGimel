package com.teamagam.gimelgimel.app.network.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageGeo;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageText;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageUserLocation;
import com.teamagam.gimelgimel.app.model.entities.GeoContent;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.network.rest.RestAPI;
import com.teamagam.gimelgimel.app.utils.PreferenceUtil;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Utility class handling different requests from GGMessaging server
 */
public class GGMessageSender {

    private static List<MessageStatusListener> mListeners = new ArrayList<>();
    private static final Logger sLogger = LoggerFactory.create(GGMessageSender.class);

    public static void addListener(@Nullable MessageStatusListener listener) {
        mListeners.add(listener);
    }

    public static void removeListener(@Nullable MessageStatusListener listener) {
        mListeners.remove(listener);
    }

    public static String getUserName(Context context) {
        return ((GGApplication) context.getApplicationContext()).getPrefs().getString(
                R.string.user_name_text_key);
    }

    private static void fireOnSuccessfulMessage(Message message) {
        for (MessageStatusListener listener : mListeners) {
            listener.onSuccessfulMessage(message);
        }
    }

    private static void fireOnFailureMessage(Message message) {
        for (MessageStatusListener listener : mListeners) {
            listener.onFailureMessage(message);
        }
    }

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
     * Creates location {@link Message} with {@link MessageGeo} containing given
     * {@link PointGeometry} and asynchronously sends it
     *
     * @param pointGeometry the message's content location
     * @return Message - the message that was sent to the server.
     */
    public Message sendGeoMessageAsync(PointGeometry pointGeometry, String text, String type) {
        GeoContent location = new GeoContent(pointGeometry, text, type);
        Message messageToSend = new MessageGeo(mSenderId, location);
        GGMessageSender.sendMessageAsync(messageToSend);
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
        GGMessageSender.sendMessageAsync(messageToSend);
    }

    /**
     * Asynchronously sends message to service
     *
     * @param message - the message to send
     */
    public static void sendMessageAsync(final Message message) {
        Call<Message> call = RestAPI.getInstance().getMessagingAPI().postMessage(message);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (!response.isSuccessful()) {
                    sLogger.d("Unsuccessful message post: " + response.errorBody());

                    GGMessageSender.fireOnFailureMessage(message);
                    return;
                }

                sLogger.d("message ID from DB: " + response.body().getMessageId());

                GGMessageSender.fireOnSuccessfulMessage(message);
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                sLogger.d("FAIL in sending message!!!");

                GGMessageSender.fireOnFailureMessage(message);
            }
        });
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
