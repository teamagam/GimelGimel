package com.teamagam.gimelgimel.app.network.services;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.network.rest.GGMessagingAPI;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yoni on 3/22/2016.
 * This class handles POST requests to the server using retrofit.
 */
public class GGMessageSender {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private GGMessagingAPI mGGMessagingApi;

    public static String BASE_URL;

    public GGMessageSender(Context context) {
        BASE_URL = context.getString(R.string.GG_api_base_url);

        //http logger for debugging
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        //JSON converter
        //for future more complex JSON converter
        //.registerTypeAdapter(MessageContent.class, ... )
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        mGGMessagingApi = retrofit.create(GGMessagingAPI.class);
    }

    public void sendMessage(Message message) {
        Call<Message> call = mGGMessagingApi.postMessage(message);
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
