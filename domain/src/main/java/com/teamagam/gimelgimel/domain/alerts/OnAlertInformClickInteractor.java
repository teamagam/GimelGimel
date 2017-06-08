package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SelectEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;
import java.util.Collections;
import javax.inject.Named;
import rx.Observable;

@AutoFactory
public class OnAlertInformClickInteractor extends BaseDataInteractor {

  private final ObjectMessageMapper mMapper;
  private final UpdateLatestInformedAlertTimeInteractorFactory
      mUpdateLatestInformedAlertTimeInteractorFactory;
  private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
  private final SelectEntityInteractorFactory mSelectEntityInteractorFactory;
  private final SelectMessageInteractorFactory mSelectMessageInteractorFactory;
  private final ChatMessage mAlert;

  public OnAlertInformClickInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided @Named("Alert") ObjectMessageMapper alertMessageMapper,
      @Provided
          UpdateLatestInformedAlertTimeInteractorFactory updateLatestInformedAlertTimeInteractorFactory,
      @Provided
          com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
      @Provided
          com.teamagam.gimelgimel.domain.map.SelectEntityInteractorFactory selectEntityInteractorFactory,
      @Provided
          com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory selectMessageInteractorFactory,
      ChatMessage alert) {
    super(threadExecutor);
    mMapper = alertMessageMapper;
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
            .doOnNext(this::showInChatIfNecessary)
            .filter(alert -> alert.contains(GeoFeature.class))
            .map(alert -> alert.getFeatureByType(GeoFeature.class))
            .doOnNext(this::goToAlertLocation)
            .doOnNext(this::selectEntity)

    );

    return Collections.singletonList(dataSubscriptionRequest);
  }

  private void updateLastInformedAlertTime(ChatMessage alert) {
    AlertFeature feature = alert.getFeatureByType(AlertFeature.class);
    mUpdateLatestInformedAlertTimeInteractorFactory.create(feature).execute();
  }

  private void showInChatIfNecessary(ChatMessage alert) {
    mSelectMessageInteractorFactory.create(alert.getMessageId()).execute();
  }

  private void goToAlertLocation(GeoFeature geo) {
    Geometry geometry = geo.getGeoEntity().getGeometry();
    mGoToLocationMapInteractorFactory.create(geometry).execute();
  }

  private void selectEntity(GeoFeature geo) {
    mSelectEntityInteractorFactory.create(geo.getGeoEntity().getId()).execute();
  }
}
