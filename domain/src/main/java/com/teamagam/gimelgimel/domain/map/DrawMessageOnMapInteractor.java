package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.BaseMessageGeo;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;

/**
 * Created on 11/6/2016.
 */
@AutoFactory
public class DrawMessageOnMapInteractor extends DoInteractor {

    private DisplayedEntitiesRepository mDisplayedEntitiesRepository;
    private MessagesRepository mMessagesRepository;
    private GeoEntitiesRepository mGeoEntitiesRepository;
    private String mMessageId;

    protected DrawMessageOnMapInteractor(
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
                .map(BaseMessageGeo::extractGeoEntity)
                .doOnNext(mGeoEntitiesRepository::add)
                .filter(mDisplayedEntitiesRepository::isNotShown)
                .doOnNext(mDisplayedEntitiesRepository::show);
    }

}
