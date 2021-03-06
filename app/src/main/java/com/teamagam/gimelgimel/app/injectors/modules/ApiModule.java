package com.teamagam.gimelgimel.app.injectors.modules;

import android.os.Handler;
import android.os.HandlerThread;
import com.teamagam.gimelgimel.data.common.FilesDownloader;
import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.dynamicLayers.remote.DataDynamicLayerRemoteSourceHandler;
import com.teamagam.gimelgimel.data.dynamicLayers.remote.DynamicLayersAPI;
import com.teamagam.gimelgimel.data.icons.IconsDataFetcher;
import com.teamagam.gimelgimel.data.response.poller.MessageLongPoller;
import com.teamagam.gimelgimel.data.response.poller.RepeatedBackoffMessagePolling;
import com.teamagam.gimelgimel.data.response.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.data.response.rest.IconsAPI;
import com.teamagam.gimelgimel.data.response.rest.RestAPI;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.DynamicLayerRemoteSourceHandler;
import com.teamagam.gimelgimel.domain.icons.IconsFetcher;
import com.teamagam.gimelgimel.domain.messages.poller.IMessagePoller;
import com.teamagam.gimelgimel.domain.messages.poller.IPolledMessagesProcessor;
import com.teamagam.gimelgimel.domain.messages.poller.PolledMessagesProcessor;
import com.teamagam.gimelgimel.domain.messages.poller.strategy.BackoffStrategy;
import com.teamagam.gimelgimel.domain.messages.poller.strategy.ExponentialBackoffStrategy;
import com.teamagam.gimelgimel.domain.messages.poller.strategy.RepeatedBackoffTaskRunner;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.teamagam.gimelgimel.domain.config.Constants.RETRIES;
import static com.teamagam.gimelgimel.domain.config.Constants.RETRIES_DELAY_MS;

@Module
public class ApiModule {

  @Provides
  @Singleton
  GGMessagingAPI provideGGMessagingAPI(RestAPI restAPI) {
    return restAPI.getMessagingAPI();
  }

  @Provides
  @Singleton
  IconsAPI provideIconsAPI(RestAPI restAPI) {
    return restAPI.getIconsAPI();
  }

  @Provides
  @Singleton
  DynamicLayersAPI provideDynamicLayerAPI(RestAPI restAPI) {
    return restAPI.getDynamicLayersAPI();
  }

  @Provides
  @Singleton
  IconsFetcher provideIconsFetcher(IconsDataFetcher fetcher) {
    return fetcher;
  }

  @Provides
  @Singleton
  FilesDownloader.FilesDownloaderAPI provideFilesDownloaderAPI(RestAPI restAPI) {
    return restAPI.getFilesDownloaderAPI();
  }

  @Provides
  @Singleton
  @Named("message poller")
  RepeatedBackoffTaskRunner provideRepeatedBackoffTaskRunner(RepeatedBackoffMessagePolling repeatedBackoffMessagePolling) {
    return repeatedBackoffMessagePolling;
  }

  @Provides
  @Singleton
  IPolledMessagesProcessor providePolledMessagesProcessor(PolledMessagesProcessor polledMessagesProcessor) {
    return polledMessagesProcessor;
  }

  @Provides
  @Singleton
  IMessagePoller provideMessagePoller(MessageLongPoller poller) {
    return poller;
  }

  @Provides
  @Singleton
  @Named("message poller")
  BackoffStrategy provideBackoffStrategy() {
    return new ExponentialBackoffStrategy(Constants.POLLING_EXP_BACKOFF_BASE_INTERVAL_MILLIS,
        Constants.POLLING_EXP_BACKOFF_MULTIPLIER, Constants.POLLING_EXP_BACKOFF_MAX_BACKOFF_MILLIS);
  }

  @Provides
  @Singleton
  @Named("message poller")
  Handler providePollingMessageHandler() {
    HandlerThread ht = new HandlerThread("messaging");
    ht.start();
    return new Handler(ht.getLooper());
  }

  @Provides
  RetryWithDelay provideApiRetryStrategy(ThreadExecutor threadExecutor) {
    return new RetryWithDelay(RETRIES, RETRIES_DELAY_MS, threadExecutor);
  }

  @Provides
  DynamicLayerRemoteSourceHandler provideDynamicLayerRemoteSourceHandler(
      DataDynamicLayerRemoteSourceHandler dataDynamicLayerRemoteSourceHandler) {
    return dataDynamicLayerRemoteSourceHandler;
  }
}
