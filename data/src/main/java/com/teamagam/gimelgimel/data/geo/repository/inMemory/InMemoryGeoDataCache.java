package com.teamagam.gimelgimel.data.geo.repository.inMemory;

import com.teamagam.gimelgimel.domain.geo.entity.GeoEntity;
import com.teamagam.gimelgimel.domain.geo.entity.VectorLayer;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class InMemoryGeoDataCache {
    private List<VectorLayer> mVectorLayers;

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

    public void addVectorLayers(VectorLayer[] vectorLayers) {
        Observable.from(vectorLayers)
                .subscribe(vectorLayer -> mVectorLayers.add(vectorLayer));
    }


    public void addGeoEntityToVectorLayer(String layerId, GeoEntity geoEntity) {
        getVectorLayerById(layerId)
                .subscribe(vectorLayer -> vectorLayer.addEntity(geoEntity));
    }

    public void addGeoEntitiesToVectorLayer(String layerId, GeoEntity[] geoEntities) {
        getVectorLayerById(layerId)
                .subscribe(vectorLayer ->
                        Observable.from(geoEntities)
                                .subscribe(vectorLayer::addEntity));
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
