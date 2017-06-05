package com.teamagam.gimelgimel.domain.layers;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.ProcessIncomingAlertMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.alerts.entity.VectorLayerAlert;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersVisibilityRepository;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageAlert;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import rx.Observable;

@AutoFactory
public class ProcessNewVectorLayerInteractor extends BaseDataInteractor {

  public static final int MINIMUM_OFFSET = 1;
  protected static final String EMPTY_SENDER_ID = "";
  private static final Logger sLogger =
      LoggerFactory.create(ProcessNewVectorLayerInteractor.class.getSimpleName());
  private final LayersLocalCache mLayersLocalCache;
  private final VectorLayer mVectorLayer;
  private final VectorLayersRepository mVectorLayerRepository;
  private final VectorLayersVisibilityRepository mVectorLayersVisibilityRepository;
  private final MessagesRepository mMessagesRepository;
  private final ProcessIncomingAlertMessageInteractorFactory
      mProcessIncomingAlertMessageInteractorFactory;
  private final URL mUrl;

  ProcessNewVectorLayerInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided LayersLocalCache layersLocalCache,
      @Provided VectorLayersRepository vectorLayerRepository,
      @Provided VectorLayersVisibilityRepository vectorLayersVisibilityRepository,
      @Provided MessagesRepository messagesRepository,
      @Provided com.teamagam.gimelgimel.domain.alerts.
          ProcessIncomingAlertMessageInteractorFactory processIncomingAlertMessageInteractorFactory,
      VectorLayer vectorLayer,
      URL url) {
    super(threadExecutor);
    mLayersLocalCache = layersLocalCache;
    mVectorLayerRepository = vectorLayerRepository;
    mVectorLayersVisibilityRepository = vectorLayersVisibilityRepository;
    mMessagesRepository = messagesRepository;
    mProcessIncomingAlertMessageInteractorFactory = processIncomingAlertMessageInteractorFactory;
    mVectorLayer = vectorLayer;
    mUrl = url;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest dataSubscriptionRequest = factory.create(Observable.just(null),
        objectObservable -> objectObservable.flatMap(x -> processIfNeeded()));
    return Collections.singletonList(dataSubscriptionRequest);
  }

  private Observable<URI> processIfNeeded() {
    if (isOutdatedVectorLayer(mVectorLayer)) {
      sLogger.d("Not processing following vector layer because it's outdated: " + mVectorLayer);
      return Observable.empty();
    }
    sLogger.d("Processing vector layer " + mVectorLayer);
    return buildProcessObservable();
  }

  private boolean isOutdatedVectorLayer(VectorLayer vl) {
    String vlId = vl.getId();
    return mVectorLayerRepository.contains(vlId)
        && mVectorLayerRepository.get(vlId).getVersion() >= vl.getVersion();
  }

  private Observable<URI> buildProcessObservable() {
    return Observable.just(null)
        .flatMap(x -> cacheLayer())
        .doOnNext(uri -> sLogger.d("Vector layer " + mVectorLayer + " is cached at " + uri))
        .doOnNext(uri -> addToRepository())
        .doOnNext(uri -> setVisible())
        .doOnNext(uri -> alertIfNeeded())
        .retryWhen(new RetryWithDelay(Constants.LAYER_CACHING_RETRIES,
            Constants.LAYER_CACHING_RETRIES_DELAY_MS))
        .doOnError(this::logFailure)
        .onErrorResumeNext(Observable.empty());
  }

  private Observable<URI> cacheLayer() {
    if (mLayersLocalCache.isCached(mVectorLayer)) {
      return Observable.just(mLayersLocalCache.getCachedURI(mVectorLayer));
    } else if (mUrl != null) {
      return mLayersLocalCache.cache(mVectorLayer, mUrl);
    }
    throw new RuntimeException(
        String.format("VectorLayer '%s' is not cached but URL was not supplied.",
            mVectorLayer.getName()));
  }

  private void addToRepository() {
    mVectorLayerRepository.put(mVectorLayer);
  }

  private void setVisible() {
    mVectorLayersVisibilityRepository.addChange(
        new VectorLayerVisibilityChange(mVectorLayer.getId(), true));
  }

  private void alertIfNeeded() {
    if (mVectorLayer.isImportant()) {
      MessageAlert ma = createImportantVLAlertMessage(mVectorLayer);
      mProcessIncomingAlertMessageInteractorFactory.create(ma).execute();
    }
  }

  private MessageAlert createImportantVLAlertMessage(VectorLayer vectorLayer) {
    String messageId = UUID.randomUUID().toString();
    long createdAtTime = generateFictiveCreationTime();
    VectorLayerAlert vla = new VectorLayerAlert(messageId, createdAtTime, vectorLayer);

    return new MessageAlert(messageId, EMPTY_SENDER_ID, new Date(createdAtTime), vla);
  }

  private long generateFictiveCreationTime() {
    Message lastMessage = mMessagesRepository.getLastMessage();
    if (lastMessage != null) {
      return lastMessage.getCreatedAt().getTime() + MINIMUM_OFFSET;
    }
    return new Date().getTime();
  }

  private void logFailure(Throwable throwable) {
    sLogger.w("Couldn't cache layer " + mVectorLayer, throwable);
  }
}
