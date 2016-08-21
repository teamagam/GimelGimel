package com.teamagam.gimelgimel.domain.geo.repository;

import com.teamagam.gimelgimel.domain.geo.entity.GeoEntity;
import com.teamagam.gimelgimel.domain.geo.entity.VectorLayer;

import rx.Observable;

public interface GeoRepository {
    Observable<VectorLayer> getAllVectorLayers();

    Observable<VectorLayer> getVectorLayerById(String id);

    Observable<GeoEntity> getEntity(String layerId, String entityId);

    void addVectorLayer(VectorLayer vectorLayer);

    void addVectorLayers(VectorLayer[] vectorLayers);

    void addGeoEntityToVectorLayer(String layerId, GeoEntity geoEntity);

    void addGeoEntitiesToVectorLayer(String layerId, GeoEntity[] geoEntities);

    void clearVectorLayers();

    void clearGeoEntitiesFromVectorLayer(String layerId);

    void deleteVectorLayerById(String layerId);

    void deleteGeoEntityFromVectorLayer(String layerId, String entityId);
}
