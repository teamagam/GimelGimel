package com.teamagam.gimelgimel.domain.layers;

import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
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
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import javax.inject.Inject;

public class ProcessVectorLayersInteractor extends BaseDataInteractor {

  private static final Logger sLogger =
      LoggerFactory.create(ProcessVectorLayersInteractor.class.getSimpleName());
  private static final int MINIMUM_OFFSET = 1;
  private static final String EMPTY_STRING = "";
  private static final int VECTOR_LAYER_ALERT_SEVERITY = 1;
  private static final String VECTOR_LAYER_ALERT_SOURCE = "SELF_GENERATED";
  private final LayersLocalCache mLayersLocalCache;
  private final VectorLayersRepository mVectorLayersRepository;
  private final VectorLayersVisibilityRepository mVectorLayersVisibilityRepository;
  private final MessagesRepository mMessagesRepository;
  private final AlertsRepository mAlertsRepository;

  @Inject
  ProcessVectorLayersInteractor(ThreadExecutor threadExecutor,
      LayersLocalCache layersLocalCache,
      VectorLayersRepository vectorLayerRepository,
      VectorLayersVisibilityRepository vectorLayersVisibilityRepository,
      MessagesRepository messagesRepository,
      AlertsRepository alertsRepository) {
    super(threadExecutor);
    mLayersLocalCache = layersLocalCache;
    mVectorLayersRepository = vectorLayerRepository;
    mVectorLayersVisibilityRepository = vectorLayersVisibilityRepository;
    mMessagesRepository = messagesRepository;
    mAlertsRepository = alertsRepository;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest request =
        factory.create(mVectorLayersRepository.getVectorLayersObservable(), getTransformer());
    return Collections.singletonList(request);
  }

  private ObservableTransformer<VectorLayer, VectorLayer> getTransformer() {
    return vlObservable -> vlObservable.flatMap(this::errorHandlingCache) // flatMap to hide errors
        .doOnNext(this::setVisible).doOnNext(this::alertIfNeeded);
  }

  private Observable<VectorLayer> errorHandlingCache(VectorLayer vl) {
    return Observable.just(vl).map(this::cache) // map to wait for caching
        .compose(errorHandling(vl));
  }

  private boolean cache(VectorLayer vectorLayer) {
    if (!mLayersLocalCache.isCached(vectorLayer)) {
      if (vectorLayer.getUrl() != null) {
        URI uri = mLayersLocalCache.cache(vectorLayer);
        sLogger.d(String.format("Vector layer %s is cached at %s", vectorLayer, uri));
      } else {
        throw new RuntimeException(
            String.format("VectorLayer '%s' is not cached but URL was not supplied.",
                vectorLayer.getName()));
      }
    }
    return true;
  }

  private ObservableTransformer<Boolean, VectorLayer> errorHandling(VectorLayer vl) {
    return observable -> observable.retryWhen(getRetryStrategy())
        .doOnError(e -> logFailure(vl, e))
        .onErrorReturn(e -> false)
        .filter(bool -> bool)
        .map(bool -> vl);
  }

  private RetryWithDelay getRetryStrategy() {
    return new RetryWithDelay(Constants.LAYER_CACHING_RETRIES,
        Constants.LAYER_CACHING_RETRIES_DELAY_MS);
  }

  private void setVisible(VectorLayer vectorLayer) {
    mVectorLayersVisibilityRepository.addChange(
        new VectorLayerVisibilityChange(vectorLayer.getId(), true));
  }

  private void alertIfNeeded(VectorLayer vectorLayer) {
    if (vectorLayer.isImportant()) {
      Alert alert = createImportantAlert(vectorLayer);
      ChatMessage message = createMessage(alert);

      //mAlertsRepository.addAlert(alert);
      mMessagesRepository.putMessage(message);
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

  private void logFailure(VectorLayer vectorLayer, Throwable throwable) {
    sLogger.w("An error occurred during processing VectorLayer: " + vectorLayer.getName(),
        throwable);
  }
}
