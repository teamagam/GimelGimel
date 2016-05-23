package com.teamagam.gimelgimel.app.network.rest;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Singleton class for exposing different remote APIs
 */
public class RestStorageAPI {

    //TODO: config. use a configuration file (all constants)
    private static final String STORAGE_API_BASE_URL = "http://CV2-PC:3000/";

    private static RestStorageAPI sInstance = new RestStorageAPI();

    public static RestStorageAPI getInstance() {
        return sInstance;
    }

    private RestStorageAPI() {
    }

    public GGStorageAPI getStorageAPI() {
        return GGStorageLazyInitializer.sGGStorageAPI;
    }

    /**
     * This class is used for lazy initialization of GGImageSender.
     * Efficiently prevents synchronization problems with instantiation of a single object.
     */
    private static class GGStorageLazyInitializer {
        public static final GGStorageAPI sGGStorageAPI = initializeStorageAPI();

        private static GGStorageAPI initializeStorageAPI() {
            //http logger for debugging
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(
                    new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            Log.v("GGStorageHttpClient", message);
                        }
                    });
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(
                    interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(STORAGE_API_BASE_URL)
                    .client(client)
                    .build();

            return retrofit.create(GGStorageAPI.class);
        }
    }
}
