package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.BaseMessageGeo;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;

/**
 * Created on 11/6/2016.
 */
@AutoFactory
public class ToggleMessageOnMapInteractor extends DoInteractor {

    private DisplayedEntitiesRepository mDisplayedEntitiesRepository;
    private MessagesRepository mMessagesRepository;
    private GeoEntitiesRepository mGeoEntitiesRepository;
    private String mMessageId;

    protected ToggleMessageOnMapInteractor(
            @Provided ThreadExecutor threadExecutor,
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
    protected Observable buildUseCaseObservable() {
        return rx.Observable.just(mMessageId)
                .flatMap(mMessagesRepository::getMessage)
                .map(msg -> (BaseMessageGeo) msg)
                .map(BaseMessageGeo::getGeoEntity)
                .doOnNext(mGeoEntitiesRepository::add)
                .doOnNext(this::toggleGeoEntityOnMap);
    }

    private void toggleGeoEntityOnMap(GeoEntity geoEntity) {
        if (mDisplayedEntitiesRepository.isShown(geoEntity)) {
            mDisplayedEntitiesRepository.hide(geoEntity);
        } else {
            mDisplayedEntitiesRepository.show(geoEntity);
        }
    }
}