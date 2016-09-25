package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.data.map.repository.inMemory.InMemoryGeoDataCache;
import com.teamagam.gimelgimel.domain.map.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.VectorLayer;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntityRepository;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class GeoDataRepository implements GeoEntityRepository{

    private InMemoryGeoDataCache mCache;

    private DisplayedEntitiesDataRepository mDisplayedData;

    @Inject
    public GeoDataRepository(InMemoryGeoDataCache cache, DisplayedEntitiesDataRepository displayed) {
        mCache = cache;
        mDisplayedData = displayed;
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
    public Observable<VectorLayer> addVectorLayer(VectorLayer vectorLayer) {
        return mCache.addVectorLayer(vectorLayer);
    }

    @Override
    public void addVectorLayers(Collection<VectorLayer> vectorLayers) {
        mCache.addVectorLayers(vectorLayers);
    }

    @Override
    public void addGeoEntityToVectorLayer(String layerId, GeoEntity geoEntity) {
        mCache.addGeoEntityToVectorLayer(layerId, geoEntity);
        mDisplayedData.addEntity(geoEntity, layerId);
    }

    @Override
    public void addGeoEntitiesToVectorLayer(String layerId, Collection<GeoEntity> geoEntities) {
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
