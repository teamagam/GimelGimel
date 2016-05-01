package com.teamagam.gimelgimel.app.network.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton class for exposing different remote APIs
 */
public class RestAPI {
    //TODO: config. use a configuration file
    private static final String MESSAGING_API_BASE_URL = "http://ggmessaging.herokuapp.com";
//    private static final String MESSAGING_API_BASE_URL = "http://localhost:3000/";

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
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


            //JSON converter
            //for future more complex JSON converter
            //.registerTypeAdapter(MessageContentXXX.class, ... )
            Gson gson = new GsonBuilder().create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MESSAGING_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();

            return retrofit.create(GGMessagingAPI.class);
        }

    }
}
