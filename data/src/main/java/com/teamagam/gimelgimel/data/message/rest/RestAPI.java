package com.teamagam.gimelgimel.data.message.rest;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.message.adapters.MessageJsonAdapter;
import com.teamagam.gimelgimel.data.message.adapters.MessageListJsonAdapter;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.rest.adapter.factory.RxErrorHandlingCallAdapterFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAPI {

    GGMessagingAPI mMessagingAPI;

    public RestAPI() {
        initializeAPIs();
    }

    private void initializeAPIs() {
        initializeMessagingAPI();
    }

    private void initializeMessagingAPI() {
        //todo: add logger
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> {});

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(Constants.CONNECTION_SERVER_TIME_OUT_SECONDS, TimeUnit.SECONDS)
                .build();
        Gson gson = createMessagingGson();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MESSAGING_SERVER_URL)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))

                .client(client)
                .build();

        mMessagingAPI = retrofit.create(GGMessagingAPI.class);
    }

    private Gson createMessagingGson() {
        // The following code creates a new Gson instance that will convert all fields from lower
        // case with underscores to camel case and vice versa. It also registers a type adapter for
        // the Message class. This DateTypeAdapter will be used anytime Gson encounters a Date field.
        // The gson instance is passed as a parameter to GsonConverter, which is a wrapper
        // class for converting types.
        MessageJsonAdapter messageJsonAdapter = new MessageJsonAdapter();
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(MessageData.class, messageJsonAdapter)
                .registerTypeAdapter(List.class,
                        new MessageListJsonAdapter(messageJsonAdapter))
                .create();
    }


    public GGMessagingAPI getMessagingAPI() {
        return mMessagingAPI;
    }
}
