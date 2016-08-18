package com.teamagam.gimelgimel.data.geo.repository.inMemory;

import com.teamagam.gimelgimel.domain.geo.entity.GeoEntity;
import com.teamagam.gimelgimel.domain.geo.entity.VectorLayer;

import java.util.ArrayList;
import java.util.List;

public class InMemoryGeoDataCache {
    private List<VectorLayer> mVectorLayers;

    public InMemoryGeoDataCache() {
        mVectorLayers = new ArrayList<>();
    }

    public VectorLayer[] getAllVectorLayers() {
        return null;
    }

    public VectorLayer getVectorLayerById(String id) {
        return null;
    }

    public GeoEntity getEntity(String layerId, String entityId) {
        return null;
    }

    public void addVectorLayer(VectorLayer vectorLayer) {

    }


    public void addGeoEntityToVectorLayer(String layerId, GeoEntity geoEntity) {

    }

    public void clearVectorLayers() {

    }

    public void clearGeoEntitesFromVectorLayer(String layerId) {

    }

    public void deleteVecotrLayerById(String id) {

    }

    public void deleteGeoEntityFromVectorLayer(String layerId, String entityId) {

    }
}
