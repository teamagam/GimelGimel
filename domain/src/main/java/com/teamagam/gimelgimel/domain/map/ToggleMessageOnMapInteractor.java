package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import io.reactivex.Observable;
import java.util.Collections;

@AutoFactory
public class ToggleMessageOnMapInteractor extends BaseDataInteractor {

  private DisplayedEntitiesRepository mDisplayedEntitiesRepository;
  private MessagesRepository mMessagesRepository;
  private GeoEntitiesRepository mGeoEntitiesRepository;
  private String mMessageId;

  protected ToggleMessageOnMapInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided DisplayedEntitiesRepository displayedEntitiesRepository,
      @Provided MessagesRepository messagesRepository,
      @Provided GeoEntitiesRepository geoEntitiesRepository,
      String messageId) {
    super(threadExecutor);
    mDisplayedEntitiesRepository = displayedEntitiesRepository;
    mMessagesRepository = messagesRepository;
    mGeoEntitiesRepository = geoEntitiesRepository;
    mMessageId = messageId;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return Collections.singletonList(factory.create(Observable.just(mMessageId),
        observable -> observable.map(mMessagesRepository::getMessage)
            .map(msg -> msg.getFeatureByType(GeoFeature.class))
            .filter(geoFeature -> geoFeature != null)
            .map(GeoFeature::getGeoEntity)
            .doOnNext(mGeoEntitiesRepository::add)
            .doOnNext(this::toggleGeoEntityOnMap)));
  }

  private void toggleGeoEntityOnMap(GeoEntity geoEntity) {
    if (mDisplayedEntitiesRepository.isShown(geoEntity)) {
      mDisplayedEntitiesRepository.hide(geoEntity);
    } else {
      mDisplayedEntitiesRepository.show(geoEntity);
    }
  }
}