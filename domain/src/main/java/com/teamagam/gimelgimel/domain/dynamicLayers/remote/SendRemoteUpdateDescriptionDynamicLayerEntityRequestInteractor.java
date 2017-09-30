package com.teamagam.gimelgimel.domain.dynamicLayers.remote;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import io.reactivex.Observable;
import java.util.List;

@AutoFactory
public class SendRemoteUpdateDescriptionDynamicLayerEntityRequestInteractor
    extends BaseSingleDataInteractor {

  private final DynamicLayerRemoteSourceHandler mDynamicLayerRemoteSourceHandler;
  private final DynamicLayersRepository mDynamicLayersRepository;
  private final String mDynamicLayerId;
  private final String mEntityDescription;
  private DynamicEntity mDynamicEntity;

  protected SendRemoteUpdateDescriptionDynamicLayerEntityRequestInteractor(
      @Provided ThreadExecutor threadExecutor,
      @Provided DynamicLayerRemoteSourceHandler dynamicLayerRemoteSourceHandler,
      @Provided DynamicLayersRepository dynamicLayersRepository,
      DynamicEntity dynamicEntity,
      String entityDescription,
      String layerId) {
    super(threadExecutor);
    mDynamicLayerRemoteSourceHandler = dynamicLayerRemoteSourceHandler;
    mDynamicLayersRepository = dynamicLayersRepository;
    mEntityDescription = entityDescription;
    mDynamicLayerId = layerId;
    mDynamicEntity = dynamicEntity;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mDynamicLayerId),
        idObservable -> idObservable.doOnNext(this::createAndSendUpdatedDynamicLayer));
  }

  private void createAndSendUpdatedDynamicLayer(String layerId) {
    DynamicLayer dynamicLayer = mDynamicLayersRepository.getById(layerId);
    List<DynamicEntity> dynamicEntityList = dynamicLayer.getEntities();
    DynamicEntity dynamicEntityToReplace =
        getDynamicEntityToReplace(dynamicEntityList, mDynamicEntity.getId());
    DynamicEntity newDynamicEntity =
        new DynamicEntity(dynamicEntityToReplace.getGeoEntity(), mEntityDescription);
    dynamicEntityList.set(dynamicEntityList.indexOf(dynamicEntityToReplace), newDynamicEntity);
    DynamicLayer updatedDynamicLayer =
        new DynamicLayer(dynamicLayer.getId(), dynamicLayer.getName(),
            dynamicLayer.getDescription(), 0, dynamicLayer.getEntities());
    mDynamicLayerRemoteSourceHandler.updateDescription(updatedDynamicLayer);
  }

  private DynamicEntity getDynamicEntityToReplace(List<DynamicEntity> dynamicEntityList,
      String dynamicEntityToReplaceId) {
    for (DynamicEntity dynamicEntity : dynamicEntityList) {
      if (dynamicEntity.getId() == dynamicEntityToReplaceId) {
        return dynamicEntity;
      }
    }
    return null;
  }
}
