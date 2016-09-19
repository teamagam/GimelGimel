package com.teamagam.gimelgimel.domain.geometries.repository;

import com.teamagam.gimelgimel.domain.geometries.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.geometries.entities.VectorLayer;

import java.util.Collection;

import rx.Observable;

public interface GeoEntityRepository {
    Observable<VectorLayer> getAllVectorLayers();

    Observable<VectorLayer> getVectorLayerById(String id);

    Observable<GeoEntity> getEntity(String layerId, String entityId);

    Observable<VectorLayer> addVectorLayer(VectorLayer vectorLayer);

    void addVectorLayers(Collection<VectorLayer> vectorLayers);

    void addGeoEntityToVectorLayer(String layerId, GeoEntity geoEntity);

    void addGeoEntitiesToVectorLayer(String layerId, Collection<GeoEntity> geoEntities);

    void clearVectorLayers();

    void clearGeoEntitiesFromVectorLayer(String layerId);

    void deleteVectorLayerById(String layerId);

    void deleteGeoEntityFromVectorLayer(String layerId, String entityId);
}
