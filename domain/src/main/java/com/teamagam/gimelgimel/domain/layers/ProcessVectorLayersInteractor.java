package com.teamagam.gimelgimel.domain.layers;

import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.layers.repository.AlertedVectorLayerRepository;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersVisibilityRepository;
import com.teamagam.gimelgimel.domain.messages.AlertMessageTextFormatter;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
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
  private final AlertedVectorLayerRepository mAlertedVectorLayerRepository;
  private final MessagesRepository mMessagesRepository;
  private final RetryWithDelay mRetryStrategy;
  private final AlertMessageTextFormatter mAlertMessageTextFormatter;

  @Inject
  ProcessVectorLayersInteractor(ThreadExecutor threadExecutor,
      LayersLocalCache layersLocalCache,
      VectorLayersRepository vectorLayerRepository,
      VectorLayersVisibilityRepository vectorLayersVisibilityRepository,
      AlertedVectorLayerRepository alertedVectorLayerRepository,
      MessagesRepository messagesRepository,
      RetryWithDelay retryStrategy,
      AlertMessageTextFormatter alertMessageTextFormatter) {
    super(threadExecutor);
    mLayersLocalCache = layersLocalCache;
    mVectorLayersRepository = vectorLayerRepository;
    mVectorLayersVisibilityRepository = vectorLayersVisibilityRepository;
    mAlertedVectorLayerRepository = alertedVectorLayerRepository;
    mMessagesRepository = messagesRepository;
    mRetryStrategy = retryStrategy;
    mAlertMessageTextFormatter = alertMessageTextFormatter;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest request =
        factory.create(mVectorLayersRepository.getVectorLayersObservable(),
            vlObservable -> vlObservable.flatMap(this::errorHandlingCache) // flatMap to hide errors
                .doOnNext(this::setVisible).filter(this::shouldAlert).doOnNext(this::alert));
    return Collections.singletonList(request);
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
    return observable -> observable.retryWhen(mRetryStrategy)
        .doOnError(e -> logFailure(vl, e))
        .onErrorReturn(e -> false)
        .filter(bool -> bool)
        .map(bool -> vl);
  }

  private void setVisible(VectorLayer vectorLayer) {
    mVectorLayersVisibilityRepository.addChange(
        new VectorLayerVisibilityChange(vectorLayer.getId(), true));
  }

  private boolean shouldAlert(VectorLayer vectorLayer) {
    return vectorLayer.isImportant() && !isAlreadyAlerted(vectorLayer);
  }

  private boolean isAlreadyAlerted(VectorLayer vectorLayer) {
    return mAlertedVectorLayerRepository.isAlerted(vectorLayer);
  }

  private void alert(VectorLayer vectorLayer) {
    Alert alert = createImportantAlert(vectorLayer);
    String alertText = mAlertMessageTextFormatter.format(alert, vectorLayer.getName());
    ChatMessage message = createMessage(alert, alertText);
    mMessagesRepository.putMessage(message);
    mAlertedVectorLayerRepository.markAsAlerted(vectorLayer);
  }

  private Alert createImportantAlert(VectorLayer vectorLayer) {
    String alertId = generateId();
    long time = generateFictiveCreationTime();

    return new Alert(alertId, VECTOR_LAYER_ALERT_SEVERITY, VECTOR_LAYER_ALERT_SOURCE, time,
        Alert.Type.VECTOR_LAYER);
  }

  private ChatMessage createMessage(Alert alert, String text) {
    String messageId = generateId();
    long time = generateFictiveCreationTime();

    return new ChatMessage(messageId, EMPTY_STRING, new Date(time), new AlertFeature(alert),
        new TextFeature(text));
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
