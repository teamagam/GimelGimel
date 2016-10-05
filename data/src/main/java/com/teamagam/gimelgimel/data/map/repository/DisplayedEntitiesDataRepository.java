package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * repository to be synced with the app for shown entities on the map
 */
@Singleton
public class DisplayedEntitiesDataRepository implements DisplayedEntitiesRepository {

    private Map<String, GeoEntity> mDisplayedEntitiesMap;

    private PublishSubject<GeoEntityNotification> mSubject;

    private Observable<GeoEntityNotification> mSharedObservable;

    @Inject
    public DisplayedEntitiesDataRepository() {
        mDisplayedEntitiesMap = new TreeMap<>();
        mSubject = PublishSubject.create();
        mSharedObservable = mSubject.share();
    }

    @Override
    public Observable<GeoEntityNotification> getSyncEntitiesObservable() {
        return mSharedObservable;
    }

    @Override
    public Observable<Collection<GeoEntity>> getDisplayedGeoEntitiesObservable() {
        Collection<GeoEntity> currentDisplayedSnapshot = new ArrayList<>(mDisplayedEntitiesMap.values());
        return Observable.just(currentDisplayedSnapshot);
    }

    @Override
    public void show(GeoEntity geoEntity) {
        mDisplayedEntitiesMap.put(geoEntity.getId(), geoEntity);

        GeoEntityNotification addNotification = GeoEntityNotification.createAdd(geoEntity);

        mSubject.onNext(addNotification);
    }

    @Override
    public void hide(GeoEntity geoEntity) {
        GeoEntity deletedEntity = mDisplayedEntitiesMap.remove(geoEntity.getId());

        if(deletedEntity != null){
            GeoEntityNotification removeNotification = GeoEntityNotification.createRemove(geoEntity);
            mSubject.onNext(removeNotification);
        }

    }
}
