package com.teamagam.gimelgimel.data.response.rest;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.geogson.core.gson.GeometryAdapterFactory;
import com.teamagam.gimelgimel.data.common.FilesDownloader;
import com.teamagam.gimelgimel.data.common.OkHttpClientFactory;
import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.response.adapters.MessageJsonAdapter;
import com.teamagam.gimelgimel.data.response.adapters.MessageListJsonAdapter;
import com.teamagam.gimelgimel.data.response.entity.ServerResponse;
import com.teamagam.gimelgimel.data.response.rest.adapter.factory.RxErrorHandlingCallAdapterFactory;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class RestAPI {

  private static final Logger sLogger = LoggerFactory.create(RestAPI.class.getSimpleName());
  private static final String FAKE_VALID_URL = "http://lies";

  private GGMessagingAPI mMessagingAPI;

  private FilesDownloader.FilesDownloaderAPI mFilesDownloaderAPI;

  @Inject
  public RestAPI() {
    initializeAPIs();
  }

  public GGMessagingAPI getMessagingAPI() {
    return mMessagingAPI;
  }

  public FilesDownloader.FilesDownloaderAPI getFilesDownloaderAPI() {
    return mFilesDownloaderAPI;
  }

  private void initializeAPIs() {
    initializeMessagingAPI();
    initializeFilesDownloaderAPI();
  }

  private void initializeMessagingAPI() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.MESSAGING_SERVER_URL)
        .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
        //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(getGsonConverterFactory())
        .client(OkHttpClientFactory.create(sLogger, HttpLoggingInterceptor.Level.BODY))
        .build();
    mMessagingAPI = retrofit.create(GGMessagingAPI.class);
  }

  private void initializeFilesDownloaderAPI() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(
        FAKE_VALID_URL)    //Base url must be supplied, it won't be used by the API
        .client(OkHttpClientFactory.create(sLogger, HttpLoggingInterceptor.Level.HEADERS)).build();
    mFilesDownloaderAPI = retrofit.create(FilesDownloader.FilesDownloaderAPI.class);
  }

  private Gson createMessagingGson() {
    MessageJsonAdapter messageJsonAdapter = new MessageJsonAdapter();
    return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapterFactory(new GeometryAdapterFactory())
        .registerTypeAdapter(ServerResponse.class, messageJsonAdapter)
        .registerTypeAdapter(List.class, new MessageListJsonAdapter(messageJsonAdapter))
        .create();
  }

  private GsonConverterFactory getGsonConverterFactory() {
    Gson gson = createMessagingGson();
    return GsonConverterFactory.create(gson);
  }
}
