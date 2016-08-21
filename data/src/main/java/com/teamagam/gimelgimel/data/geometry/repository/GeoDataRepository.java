package com.teamagam.gimelgimel.data.geometry.repository;

import com.teamagam.gimelgimel.data.geometry.repository.inMemory.InMemoryGeoDataCache;
import com.teamagam.gimelgimel.domain.geometries.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.geometries.entities.VectorLayer;
import com.teamagam.gimelgimel.domain.geometries.repository.GeoRepository;

import rx.Observable;

public class GeoDataRepository implements GeoRepository {

    private InMemoryGeoDataCache mCache;

    public GeoDataRepository(InMemoryGeoDataCache cache) {
        mCache = cache;
    }

    @Override
    public Observable<VectorLayer> getAllVectorLayers() {
        return mCache.getAllVectorLayers();
    }

    @Override
    public Observable<VectorLayer> getVectorLayerById(String id) {
        return mCache.getVectorLayerById(id);
    }

    @Override
    public Observable<GeoEntity> getEntity(String layerId, String entityId) {
        return mCache.getEntity(layerId, entityId);
    }

    @Override
    public void addVectorLayer(VectorLayer vectorLayer) {
        mCache.addVectorLayer(vectorLayer);
    }

    @Override
    public void addVectorLayers(VectorLayer[] vectorLayers) {
        mCache.addVectorLayers(vectorLayers);
    }

    @Override
    public void addGeoEntityToVectorLayer(String layerId, GeoEntity geoEntity) {
        mCache.addGeoEntityToVectorLayer(layerId, geoEntity);
    }

    @Override
    public void addGeoEntitiesToVectorLayer(String layerId, GeoEntity[] geoEntities) {
        mCache.addGeoEntitiesToVectorLayer(layerId, geoEntities);
    }

    @Override
    public void clearVectorLayers() {
        mCache.clearVectorLayers();
    }

    @Override
    public void clearGeoEntitiesFromVectorLayer(String layerId) {
        mCache.clearGeoEntitiesFromVectorLayer(layerId);
    }

    @Override
    public void deleteVectorLayerById(String layerId) {
        mCache.deleteVectorLayerById(layerId);
    }

    @Override
    public void deleteGeoEntityFromVectorLayer(String layerId, String entityId) {
        mCache.deleteGeoEntityFromVectorLayer(layerId, entityId);
    }
}
