package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.VectorLayer;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.ReplaySubject;

/**
  * repository to be synced with the app for shown entities on the map
 */
@Singleton
public class DisplayedEntitiesDataRepository implements DisplayedEntitiesRepository{

    private VectorLayer displayedVectorLayer;

    private ReplaySubject<GeoEntityNotification> mSubject;

    @Inject
    public DisplayedEntitiesDataRepository() {
        displayedVectorLayer = new VectorLayer("displayed_layer");
        mSubject = ReplaySubject.create();
//        mSubject.groupBy(notification ->  notification.getGeoEntity().getId())
//                .flatMap(groupedObservable -> groupedObservable.take(1))
//                .filter(notification ->  notification.getAction() != GeoEntityNotification.REMOVE);
        mSubject.filter(notification ->  notification.getAction() != GeoEntityNotification.ADD);
        mSubject.map(notification ->  null );

    }


    public Observable<GeoEntityNotification> getSyncEntitiesObservable() {
        return mSubject;
    }

    public Observable<VectorLayer> getDisplayedVectorLayerObservable() {
        return Observable.just(displayedVectorLayer);
    }

    public void addEntity(GeoEntity geoEntity, String vectorLayerId){
        displayedVectorLayer.addEntity(geoEntity);
        mSubject.onNext(createGeoEntityNotification(geoEntity, vectorLayerId,
                GeoEntityNotification.ADD));
    }

    public void removeEntity(String entityId, String vectorLayerId){
        GeoEntity geoEntity = displayedVectorLayer.removeEntity(entityId);
        mSubject.onNext(createGeoEntityNotification(geoEntity, vectorLayerId,
                GeoEntityNotification.REMOVE));
    }

    public void updateEntity(GeoEntity geoEntity, String vectorLayerId){
        removeEntity(geoEntity.getId(), vectorLayerId);
        addEntity(geoEntity, vectorLayerId);
    }

    private GeoEntityNotification createGeoEntityNotification(GeoEntity geoEntity, String vectorLayerId, int action) {
        return new GeoEntityNotification(geoEntity, vectorLayerId, action);
    }
}
