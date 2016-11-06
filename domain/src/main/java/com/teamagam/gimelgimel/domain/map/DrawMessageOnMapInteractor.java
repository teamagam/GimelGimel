package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;

import rx.Observable;

/**
 * Created on 11/6/2016.
 * TODO: complete text
 */
@AutoFactory
public class DrawMessageOnMapInteractor extends DoInteractor {

    private DisplayedEntitiesRepository mDisplayedEntitiesRepository;
    private GeoEntitiesRepository mGeoEntitiesRepository;
    private String mMessageId;

    protected DrawMessageOnMapInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided DisplayedEntitiesRepository displayedEntitiesRepository,
            @Provided GeoEntitiesRepository geoEntitiesRepository,
            String messageId) {
        super(threadExecutor);
        mDisplayedEntitiesRepository = displayedEntitiesRepository;
        mGeoEntitiesRepository = geoEntitiesRepository;
        mMessageId = messageId;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return rx.Observable.just(mMessageId)
                .map(mGeoEntitiesRepository::get)
                .filter(mDisplayedEntitiesRepository::isNotShown)
                .doOnNext(mDisplayedEntitiesRepository::show);
    }
}
