package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * repository to be synced with the app for shown entities on the map
 */
@Singleton
public class DisplayedEntitiesDataRepository implements DisplayedEntitiesRepository {

    private List<GeoEntityNotification> displayedEntitiesMap;

    private PublishSubject<GeoEntityNotification> mSubject;

    private Observable<GeoEntityNotification> mSharedObservable;

    @Inject
    public DisplayedEntitiesDataRepository() {
        displayedEntitiesMap = new LinkedList<>();
        mSubject = PublishSubject.create();
        mSharedObservable = mSubject.share();
    }


    public Observable<GeoEntityNotification> getSyncEntitiesObservable() {
//        return mSubject;
        return mSharedObservable;
    }

    public Observable<GeoEntityNotification> getDisplayedVectorLayerObservable() {
        return Observable.from(displayedEntitiesMap)
                .mergeWith(mSharedObservable);
    }

    public void addEntity(GeoEntity geoEntity, String vectorLayerId) {
        GeoEntityNotification geoEntityNotification = createGeoEntityNotification(geoEntity, vectorLayerId,
                GeoEntityNotification.ADD);
        displayedEntitiesMap.add(geoEntityNotification);
        mSubject.onNext(geoEntityNotification);
    }

    public void removeEntity(String entityId, String vectorLayerId) {
        Observable.from(displayedEntitiesMap)
                .filter(geo -> geo.getVectorLayerId().equals(vectorLayerId))
                .filter(geo -> geo.getGeoEntity().getId().equals(entityId))
                .doOnNext(displayedEntitiesMap::remove)
                .map(geo -> createGeoEntityNotification(geo.getGeoEntity(), geo.getVectorLayerId(),
                        GeoEntityNotification.REMOVE))
                .doOnNext(geo -> mSubject.onNext(geo))
                .subscribe();
    }

    public void updateEntity(GeoEntity geoEntity, String vectorLayerId) {
        removeEntity(geoEntity.getId(), vectorLayerId);
        addEntity(geoEntity, vectorLayerId);
    }

    private GeoEntityNotification createGeoEntityNotification(GeoEntity geoEntity, String vectorLayerId, int action) {
        return new GeoEntityNotification(geoEntity, vectorLayerId, action);
    }
}
