package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SelectEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;
import java.util.Collections;
import javax.inject.Named;
import rx.Observable;

@AutoFactory
public class OnAlertInformClickInteractor extends BaseDataInteractor {

  private final ObjectMessageMapper mMapper;
  private final MessagesRepository mMessagesRepository;
  private final UpdateLatestInformedAlertTimeInteractorFactory
      mUpdateLatestInformedAlertTimeInteractorFactory;
  private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
  private final SelectEntityInteractorFactory mSelectEntityInteractorFactory;
  private final SelectMessageInteractorFactory mSelectMessageInteractorFactory;
  private final Alert mAlert;

  public OnAlertInformClickInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided @Named("Alert") ObjectMessageMapper alertMessageMapper,
      @Provided MessagesRepository messagesRepository,
      @Provided
          UpdateLatestInformedAlertTimeInteractorFactory updateLatestInformedAlertTimeInteractorFactory,
      @Provided
          com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
      @Provided
          com.teamagam.gimelgimel.domain.map.SelectEntityInteractorFactory selectEntityInteractorFactory,
      @Provided
          com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory selectMessageInteractorFactory,
      Alert alert) {
    super(threadExecutor);
    mMapper = alertMessageMapper;
    mMessagesRepository = messagesRepository;
    mUpdateLatestInformedAlertTimeInteractorFactory =
        updateLatestInformedAlertTimeInteractorFactory;
    mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
    mSelectEntityInteractorFactory = selectEntityInteractorFactory;
    mSelectMessageInteractorFactory = selectMessageInteractorFactory;
    mAlert = alert;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest dataSubscriptionRequest = factory.create(Observable.just(mAlert),
        alertObservable -> alertObservable.doOnNext(this::updateLastInformedAlertTime)
            .map(this::getMessage)
            .doOnNext(this::showInChatIfNecessary)
            .filter(message -> message.contains(GeoFeature.class))
            .map(message -> message.getFeatureByType(GeoFeature.class))
            .doOnNext(this::goToAlertLocation)
            .doOnNext(this::selectEntity)

    );

    return Collections.singletonList(dataSubscriptionRequest);
  }

  private ChatMessage getMessage(Alert alert) {
    return mMessagesRepository.getMessage(mMapper.getMessageId(alert.getId()));
  }

  private void updateLastInformedAlertTime(Alert alert) {
    mUpdateLatestInformedAlertTimeInteractorFactory.create(alert).execute();
  }

  private void showInChatIfNecessary(ChatMessage message) {
    mSelectMessageInteractorFactory.create(message.getMessageId()).execute();
  }

  private void goToAlertLocation(GeoFeature geo) {
    Geometry geometry = geo.getGeoEntity().getGeometry();
    mGoToLocationMapInteractorFactory.create(geometry).execute();
  }

  private void selectEntity(GeoFeature geo) {
    mSelectEntityInteractorFactory.create(geo.getGeoEntity().getId()).execute();
  }
}
