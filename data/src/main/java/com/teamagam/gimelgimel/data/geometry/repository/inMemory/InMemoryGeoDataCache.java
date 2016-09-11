package com.teamagam.gimelgimel.data.geometry.repository.inMemory;

import com.teamagam.gimelgimel.domain.geometries.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.geometries.entities.VectorLayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class InMemoryGeoDataCache {

    private List<VectorLayer> mVectorLayers;

    @Inject
    public InMemoryGeoDataCache() {
        mVectorLayers = new ArrayList<>();
    }

    public Observable<VectorLayer> getAllVectorLayers() {
        return Observable.from(mVectorLayers);
    }

    public Observable<VectorLayer> getVectorLayerById(String id) {
        return Observable.from(mVectorLayers)
                .filter(vectorLayer -> vectorLayer.getLayerId().equals(id));
    }

    public Observable<GeoEntity> getEntity(String layerId, String entityId) {
        return getVectorLayerById(layerId)
                .map(vectorLayer -> vectorLayer.getEntity(entityId));
    }

    public void addVectorLayer(VectorLayer vectorLayer) {
        mVectorLayers.add(vectorLayer);
    }

    public void addVectorLayers(Collection<VectorLayer> vectorLayers) {
        mVectorLayers.addAll(vectorLayers);
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
