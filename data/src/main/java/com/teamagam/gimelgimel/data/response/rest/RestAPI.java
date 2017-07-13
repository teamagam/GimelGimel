package com.teamagam.gimelgimel.data.response.rest;

import com.teamagam.gimelgimel.data.common.FilesDownloader;
import com.teamagam.gimelgimel.data.common.GsonFactory;
import com.teamagam.gimelgimel.data.common.OkHttpClientFactory;
import com.teamagam.gimelgimel.data.response.rest.adapter.factory.RxErrorHandlingCallAdapterFactory;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class RestAPI {

  private static final Logger sLogger = LoggerFactory.create(RestAPI.class.getSimpleName());
  private static final String FAKE_VALID_URL = "http://lies";

  private final APIUrlProvider mAPIUrlProvider;

  private GGMessagingAPI mMessagingAPI;

  private FilesDownloader.FilesDownloaderAPI mFilesDownloaderAPI;

  private IconsAPI mIconsAPI;

  @Inject
  public RestAPI(APIUrlProvider APIUrlProvider) {
    mAPIUrlProvider = APIUrlProvider;
    initializeAPIs();
  }

  public GGMessagingAPI getMessagingAPI() {
    return mMessagingAPI;
  }

  public FilesDownloader.FilesDownloaderAPI getFilesDownloaderAPI() {
    return mFilesDownloaderAPI;
  }

  public IconsAPI getIconsAPI() {
    return mIconsAPI;
  }

  private void initializeAPIs() {
    initializeMessagingAPI();
    initializeFilesDownloaderAPI();
    initializeIconsAPI();
  }

  private void initializeMessagingAPI() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(mAPIUrlProvider.getMessagingServerUrl())
        .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
        .addConverterFactory(GsonFactory.getMessagingGsonConverterFactory())
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

  private void initializeIconsAPI() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(mAPIUrlProvider.getMessagingServerUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClientFactory.create(sLogger, HttpLoggingInterceptor.Level.BODY))
        .build();
    mIconsAPI = retrofit.create(IconsAPI.class);
  }
}
