package com.teamagam.gimelgimel.data.geo.repository;

import com.teamagam.gimelgimel.data.geo.repository.inMemory.InMemoryGeoDataCache;
import com.teamagam.gimelgimel.domain.geo.entity.GeoEntity;
import com.teamagam.gimelgimel.domain.geo.entity.VectorLayer;
import com.teamagam.gimelgimel.domain.geo.repository.GeoRepository;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class GeoDataRepository implements GeoRepository {

    //private InMemoryGeoDataCache mCache;
    private List<VectorLayer> mVectorLayers;

    public GeoDataRepository(InMemoryGeoDataCache cache) {
        //mCache = cache;
        mVectorLayers = new ArrayList<>();
    }

    @Override
    public Observable<VectorLayer> getAllVectorLayers() {
//        return Observable.from(mCache.getAllVectorLayers());
        return Observable.from(mVectorLayers);
    }

    @Override
    public Observable<VectorLayer> getVectorLayerById(String id) {
//        return Observable.just(mCache.getVectorLayerById(id));
        return Observable.from(mVectorLayers)
                .filter(vectorLayer -> vectorLayer.getLayerId().equals(id));
    }

    @Override
    public Observable<GeoEntity> getEntity(String layerId, String entityId) {
//        return Observable.just(mCache.getEntity(layerId, entityId));
        return getVectorLayerById(layerId)
                .map(vectorLayer -> vectorLayer.getEntity(entityId));
    }

    @Override
    public void addVectorLayer(VectorLayer vectorLayer) {
//        mCache.addVectorLayer(vectorLayer);
        mVectorLayers.add(vectorLayer);
    }

    @Override
    public void addVectorLayers(VectorLayer[] vectorLayers) {
        Observable.from(vectorLayers)
                .subscribe(vectorLayer -> mVectorLayers.add(vectorLayer));
    }

    @Override
    public void addGeoEntityToVectorLayer(String layerId, GeoEntity geoEntity) {
        getVectorLayerById(layerId)
                .subscribe(vectorLayer -> vectorLayer.addEntity(geoEntity));
    }

    @Override
    public void addGeoEntitiesToVectorLayer(String layerId, GeoEntity[] geoEntities) {
        getVectorLayerById(layerId)
                .subscribe(vectorLayer ->
                        Observable.from(geoEntities)
                                .subscribe(vectorLayer::addEntity));
    }

    @Override
    public void clearVectorLayers() {
        mVectorLayers.clear();
    }

    @Override
    public void clearGeoEntitiesFromVectorLayer(String layerId) {
        getVectorLayerById(layerId)
                .subscribe(VectorLayer::removeAllEntities);
    }

    @Override
    public void deleteVectorLayerById(String layerId) {
        getVectorLayerById(layerId)
                .subscribe(vectorLayer -> mVectorLayers.remove(vectorLayer));
    }

    @Override
    public void deleteGeoEntityFromVectorLayer(String layerId, String entityId) {
        getVectorLayerById(layerId)
                .subscribe(vectorLayer -> vectorLayer.removeEntity(entityId));
    }
}
