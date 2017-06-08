package com.teamagam.gimelgimel.domain.layers;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersVisibilityRepository;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import rx.Observable;

@AutoFactory
public class ProcessNewVectorLayerInteractor extends BaseDataInteractor {

  private static final Logger sLogger =
      LoggerFactory.create(ProcessNewVectorLayerInteractor.class.getSimpleName());
  private static final int MINIMUM_OFFSET = 1;
  private static final String EMPTY_SENDER_ID = "";
  private static final int VECTOR_LAYER_ALERT_SEVERITY = 1;
  private static final String VECTOR_LAYER_ALERT_SOURCE = "SELF_GENERATED";
  private final LayersLocalCache mLayersLocalCache;
  private final VectorLayer mVectorLayer;
  private final VectorLayersRepository mVectorLayerRepository;
  private final VectorLayersVisibilityRepository mVectorLayersVisibilityRepository;
  private final MessagesRepository mMessagesRepository;
  private final com.teamagam.gimelgimel.domain.messages.AddMessageToRepositoryInteractorFactory
      mAddMessageToRepositoryInteractorFactory;

  ProcessNewVectorLayerInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided LayersLocalCache layersLocalCache,
      @Provided VectorLayersRepository vectorLayerRepository,
      @Provided VectorLayersVisibilityRepository vectorLayersVisibilityRepository,
      @Provided MessagesRepository messagesRepository,
      @Provided
          com.teamagam.gimelgimel.domain.messages.AddMessageToRepositoryInteractorFactory addMessageToRepositoryInteractorFactory,
      VectorLayer vectorLayer) {
    super(threadExecutor);
    mLayersLocalCache = layersLocalCache;
    mVectorLayerRepository = vectorLayerRepository;
    mVectorLayersVisibilityRepository = vectorLayersVisibilityRepository;
    mMessagesRepository = messagesRepository;
    mVectorLayer = vectorLayer;
    mAddMessageToRepositoryInteractorFactory = addMessageToRepositoryInteractorFactory;
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
    String id = vl.getId();
    return mVectorLayerRepository.contains(id)
        && mVectorLayerRepository.get(id).getVersion() >= vl.getVersion();
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
    } else if (mVectorLayer.getUrl() != null) {
      return mLayersLocalCache.cache(mVectorLayer);
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
      ChatMessage message = createImportantVLAlertMessage(mVectorLayer);
      mAddMessageToRepositoryInteractorFactory.create(message).execute();
    }
  }

  private ChatMessage createImportantVLAlertMessage(VectorLayer vectorLayer) {
    String messageId = UUID.randomUUID().toString();
    long createdAtTime = generateFictiveCreationTime();

    return new ChatMessage(messageId, EMPTY_SENDER_ID, new Date(createdAtTime),
        new TextFeature("Vector Layer Updated",
            "The layer " + vectorLayer.getName() + " has been updated."),
        new AlertFeature(messageId, VECTOR_LAYER_ALERT_SEVERITY, VECTOR_LAYER_ALERT_SOURCE,
            createdAtTime));
  }

  private long generateFictiveCreationTime() {
    ChatMessage lastMessage = mMessagesRepository.getLastMessage();
    if (lastMessage != null) {
      return lastMessage.getCreatedAt().getTime() + MINIMUM_OFFSET;
    }
    return new Date().getTime();
  }

  private void logFailure(Throwable throwable) {
    sLogger.w("Couldn't cache layer " + mVectorLayer, throwable);
  }
}
