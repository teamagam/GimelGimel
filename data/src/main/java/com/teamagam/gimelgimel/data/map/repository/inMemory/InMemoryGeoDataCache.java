package com.teamagam.gimelgimel.data.map.repository.inMemory;

import com.teamagam.gimelgimel.domain.map.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.VectorLayer;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class InMemoryGeoDataCache {

    private Map<String, VectorLayer> mVectorLayers;

    @Inject
    public InMemoryGeoDataCache() {
        mVectorLayers = new TreeMap<>();
    }

    public Observable<VectorLayer> getAllVectorLayers() {
        return Observable.from(mVectorLayers.values());
    }

    public Observable<VectorLayer> getVectorLayerById(final String id) {
        return Observable.just(mVectorLayers)
                .map(vectorLayerMap -> vectorLayerMap.containsKey(id))
                .flatMap(isExists -> isExists ?
                        Observable.just(mVectorLayers.get(id)) :
                        addVectorLayer(id));
    }

    public Observable<GeoEntity> getEntity(String layerId, String entityId) {
        return getVectorLayerById(layerId)
                .map(vectorLayer -> vectorLayer.getEntity(entityId));
    }

    public Observable<VectorLayer> addVectorLayer(String id) {
        return addVectorLayer(new VectorLayer(id));
    }

    public Observable<VectorLayer> addVectorLayer(VectorLayer vectorLayer) {
        return Observable.just(putVectorLayerToMap(vectorLayer));
    }

    private VectorLayer putVectorLayerToMap(VectorLayer vectorLayer) {
        mVectorLayers.put(vectorLayer.getLayerId(), vectorLayer);
        return vectorLayer;
    }

    public void addVectorLayers(Collection<VectorLayer> vectorLayers) {
        Observable.from(vectorLayers)
                .doOnNext(vectorLayer -> mVectorLayers.put(vectorLayer.getLayerId(), vectorLayer))
                .subscribe();
    }

    public void addGeoEntityToVectorLayer(String layerId, GeoEntity geoEntity) {
        getVectorLayerById(layerId)
                .subscribe(vectorLayer -> vectorLayer.addEntity(geoEntity));
    }

    public void addGeoEntitiesToVectorLayer(String layerId, Collection<GeoEntity> geoEntities) {
        getVectorLayerById(layerId)
                .doOnNext(vectorLayer -> vectorLayer.addEntities(geoEntities))
                .subscribe();
    }

    public void clearVectorLayers() {
        mVectorLayers.clear();
    }

    public void clearGeoEntitiesFromVectorLayer(String layerId) {
        getVectorLayerById(layerId)
                .subscribe(VectorLayer::removeAllEntities);
    }

    public void deleteVectorLayerById(String layerId) {
        getVectorLayerById(layerId)
                .subscribe(vectorLayer -> mVectorLayers.remove(vectorLayer));
    }

    public void deleteGeoEntityFromVectorLayer(String layerId, String entityId) {
        getVectorLayerById(layerId)
                .subscribe(vectorLayer -> vectorLayer.removeEntity(entityId));
    }
}
