package com.teamagam.gimelgimel.app.network.rest;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageJsonAdapter;
import com.teamagam.gimelgimel.app.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton class for exposing different remote APIs
 */
public class RestAPI {

    private static RestAPI sInstance = new RestAPI();

    public static RestAPI getInstance() {
        return sInstance;
    }

    private RestAPI() {
    }

    public GGMessagingAPI getMessagingAPI() {
        return GGMessagingLazyInitializer.GGMessagingAPI;
    }

    /**
     * This class is used for lazy initialization of GGMessaging.
     * Efficiently prevents synchronization problems with instantiation of a single object.
     */
    private static class GGMessagingLazyInitializer {
        public static final GGMessagingAPI GGMessagingAPI = initializeMessagingAPI();

        private static GGMessagingAPI initializeMessagingAPI() {
            //http logger for debugging
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(
                    new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            Log.v("GGMessagingHttpClient", message);
                        }
                    });
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(
                    interceptor).build();

            // The following code creates a new Gson instance that will convert all fields from lower
            // case with underscores to camel case and vice versa. It also registers a type adapter for
            // the Message class. This DateTypeAdapter will be used anytime Gson encounters a Date field.
            // The gson instance is passed as a parameter to GsonConverter, which is a wrapper
            // class for converting types.
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .registerTypeAdapter(Message.class, new MessageJsonAdapter())
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.MESSAGING_SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();

            return retrofit.create(GGMessagingAPI.class);
        }
    }
}
