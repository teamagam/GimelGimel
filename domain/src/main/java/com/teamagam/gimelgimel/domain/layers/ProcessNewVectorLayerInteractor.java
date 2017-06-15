package com.teamagam.gimelgimel.domain.layers;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.AddAlertToRepositoryInteractorFactory;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
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
import com.teamagam.gimelgimel.domain.messages.AddMessageToRepositoryInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import io.reactivex.Observable;
import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static com.teamagam.gimelgimel.domain.config.Constants.SIGNAL;

@AutoFactory
public class ProcessNewVectorLayerInteractor extends BaseDataInteractor {

  private static final Logger sLogger =
      LoggerFactory.create(ProcessNewVectorLayerInteractor.class.getSimpleName());
  private static final int MINIMUM_OFFSET = 1;
  private static final String EMPTY_STRING = "";
  private static final int VECTOR_LAYER_ALERT_SEVERITY = 1;
  private static final String VECTOR_LAYER_ALERT_SOURCE = "SELF_GENERATED";
  private final LayersLocalCache mLayersLocalCache;
  private final VectorLayer mVectorLayer;
  private final VectorLayersRepository mVectorLayerRepository;
  private final VectorLayersVisibilityRepository mVectorLayersVisibilityRepository;
  private final MessagesRepository mMessagesRepository;
  private final AddMessageToRepositoryInteractorFactory mAddMessageToRepositoryInteractorFactory;
  private final AddAlertToRepositoryInteractorFactory mAddAlertToRepositoryInteractorFactory;

  ProcessNewVectorLayerInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided LayersLocalCache layersLocalCache,
      @Provided VectorLayersRepository vectorLayerRepository,
      @Provided VectorLayersVisibilityRepository vectorLayersVisibilityRepository,
      @Provided MessagesRepository messagesRepository,
      @Provided
          com.teamagam.gimelgimel.domain.messages.AddMessageToRepositoryInteractorFactory addMessageToRepositoryInteractorFactory,
      @Provided
          com.teamagam.gimelgimel.domain.alerts.AddAlertToRepositoryInteractorFactory addAlertToRepositoryInteractorFactory,
      VectorLayer vectorLayer) {
    super(threadExecutor);
    mLayersLocalCache = layersLocalCache;
    mVectorLayerRepository = vectorLayerRepository;
    mVectorLayersVisibilityRepository = vectorLayersVisibilityRepository;
    mMessagesRepository = messagesRepository;
    mAddMessageToRepositoryInteractorFactory = addMessageToRepositoryInteractorFactory;
    mAddAlertToRepositoryInteractorFactory = addAlertToRepositoryInteractorFactory;
    mVectorLayer = vectorLayer;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest dataSubscriptionRequest = factory.create(Observable.just(SIGNAL),
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
    return Observable.just(SIGNAL)
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
      Alert alert = createImportantAlert(mVectorLayer);
      ChatMessage message = createMessage(alert);

      mAddAlertToRepositoryInteractorFactory.create(alert).execute();
      mAddMessageToRepositoryInteractorFactory.create(message).execute();
    }
  }

  private Alert createImportantAlert(VectorLayer vectorLayer) {
    String alertId = generateId();
    long time = generateFictiveCreationTime();

    return new Alert(alertId, VECTOR_LAYER_ALERT_SEVERITY, vectorLayer.getName(),
        VECTOR_LAYER_ALERT_SOURCE, time, Alert.Type.VECTOR_LAYER);
  }

  private ChatMessage createMessage(Alert alert) {
    String messageId = generateId();
    long time = generateFictiveCreationTime();

    return new ChatMessage(messageId, EMPTY_STRING, new Date(time), new AlertFeature(alert));
  }

  private long generateFictiveCreationTime() {
    ChatMessage lastMessage = mMessagesRepository.getLastMessage();
    if (lastMessage != null) {
      return lastMessage.getCreatedAt().getTime() + MINIMUM_OFFSET;
    }
    return new Date().getTime();
  }

  private String generateId() {
    return UUID.randomUUID().toString();
  }

  private void logFailure(Throwable throwable) {
    sLogger.w("Couldn't cache layer " + mVectorLayer, throwable);
  }
}
